package com.jd.o2o.vipcart.common.plugins.log.impl;

import com.jd.o2o.vipcart.common.domain.enums.OperEnum;
import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.plugins.log.*;
import com.jd.o2o.vipcart.common.plugins.log.support.LogOperation;
import com.jd.o2o.vipcart.common.plugins.log.support.spel.LogExpressionEvaluator;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.common.utils.SpringUtils;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.expression.EvaluationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 日志处理切面
 * Created by liuhuiqing on 2015/8/18.
 */
@Aspect
public class LoggableAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggableAspect.class);
    /**
     * 默认日志记录服务
     */
    private LoggableService defaultLoggableService;
    /**
     * 缓存异步处理线程池
     */
    private final transient ExecutorService executorService = Executors.newFixedThreadPool(5);
    /**
     * SPEL表达式解析对象
     */
    private final LogExpressionEvaluator evaluator = new LogExpressionEvaluator();

    /**
     * 调用服务方法或直接冲缓存中获取数据
     * 不要修改方法签名，否则可能返回值可能不兼容
     *
     * @param point Joint point
     * @return The result of call
     * @throws Throwable
     */
    @Around("execution(* *(..)) && @annotation(com.jd.o2o.vipcart.common.plugins.log.Loggable)")
    public Object loggable(final ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        //执行业务处理逻辑
        Object obj = point.proceed();
        final long costTime = System.currentTimeMillis() - startTime;
        try {
            final Method method = MethodSignature.class.cast(point.getSignature()).getMethod();
            final Loggable annot = method.getAnnotation(Loggable.class);

            if(!isRecordLog(getMethodName(annot),obj)){
                return obj;
            }
            LogBean logBean = buildLogBean(point, method, annot, method.getParameterAnnotations(), obj, costTime);
            if (logBean == null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("方法入参未能成功生成logbean对象，不能进行操作记录：[%s]", point.toLongString()));
                }
                return obj;
            }
            final boolean sync = annot.sync();
            if (sync) {
                toExecute(logBean, method, annot);
            } else {
                final LogBean log = logBean;
                executorService.submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        toExecute(log, method, annot);
                        return null;
                    }
                });
            }
        } catch (Exception e) {
            String errMsg = String.format("执行日志记录操作出现异常[%s]", point.toLongString());
            LOGGER.error(errMsg, e);
        }
        return obj;
    }

    /**
     * 获得方法名称
     * @param annot
     * @return
     */
    private String getMethodName(Loggable annot){
        if(annot == null){
            return null;
        }
        if(StringUtils.isNotEmpty(annot.successMethod())){
            return annot.successMethod();
        }
        if(annot.fields()!=null){
            String f = "successMethod=";
            for(String field:annot.fields()){
                field.replaceAll(" ","");
                if(field.startsWith(f)){
                    return field.replaceFirst(f,"");
                }
            }
        }
        return null;
    }

    /**
     * 是否将缓存结果添加到缓存中去
     *
     * @param methodName
     * @param result
     * @return
     */
    private boolean isRecordLog(String methodName, Object result) {
        boolean isSuccess = true;
        if (StringUtils.isNotEmpty(methodName) && result != null) {
            try {
                Method m = result.getClass().getMethod(methodName);
                isSuccess = Boolean.class.cast(m.invoke(result, null));
            } catch (Exception e) {
                LOGGER.warn(String.format("执行业务返回对象结果判断方法[%s](结果数据是否需要日志记录)出现错误", methodName), e);
            }
        }
        return isSuccess;
    }

    /**
     * 构建日志信息对象
     *
     * @param method
     * @param annot
     * @param annotations
     * @param result
     * @param costTime
     * @return
     */
    private LogBean buildLogBean(ProceedingJoinPoint point, Method method, Loggable annot, Annotation[][] annotations, Object result, Long costTime) {
        Object[] args = point.getArgs();
        int argsLen = args == null ? 0 : args.length;
        Map<String, String> paramMap = buildParamMap(args, annotations);

        Object target = point.getThis();
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        if (targetClass == null && target != null) {
            targetClass = target.getClass();
        }
        LogOperationContext opContext = new LogOperationContext(
                new LogOperation(annot.operationKey(), annot.secondaryKeys(),
                        annot.maskException()), method, args, target, targetClass);

        LogBean logBean = BeanHelper.mapToModel(paramMap, LogBean.class);
        buildLogBean(logBean,annot.fields(),opContext);
        logBean.setCostTime(costTime);
        String className = method.getDeclaringClass().getName();
        String methodMsg = new StringBuilder(className).append(".").append(method.getName()).toString();
        if (StringUtils.isEmpty(logBean.getFlowNo())) {
            logBean.setFlowNo(String.valueOf(System.currentTimeMillis()));//todo folwNo
        }
        if (StringUtils.isEmpty(logBean.getBusiNo())) {
            logBean.setBusiNo(methodMsg);
        }
        if (StringUtils.isEmpty(logBean.getBusiName())) {
            logBean.setBusiName(methodMsg);
        }
        if (StringUtils.isEmpty(logBean.getTableName())) {
            String tableName = annot.tableName();
            if (StringUtils.isEmpty(tableName)) {
                String tname = className;
                if (targetClass != null) {
                    tname = targetClass.getName();
                }
                tableName = buildTableNameFromClass(tname);
            }
            logBean.setTableName(tableName);
        }

        String ok = opContext.generateOperationKey();
        if(StringUtils.isNotEmpty(ok)){
            logBean.setOperationKey(ok);
        }
        if (StringUtils.isEmpty(logBean.getOperationKey())) {
            logBean.setOperationKey(buildOperationKey(annot.operationKey(), result, paramMap));
        }
        if (StringUtils.isEmpty(logBean.getSecondaryKeys())) {
            Map<String, String> secMap = opContext.generateSecondaryKeys();
            for (Map.Entry<String, String> entry : secMap.entrySet()) {
                String sec = entry.getValue();
                if (StringUtils.isEmpty(sec)) {
                    entry.setValue(paramMap.get(entry.getKey()));
                }
            }
            logBean.setSecondaryKeys(buildSecondKeys(secMap, paramMap));
        }
        StringBuilder argsTypeBuilder = new StringBuilder();
        StringBuilder argsValueBuilder = new StringBuilder();
        for (int i = 0; i < argsLen; i++) {
            if (i > 0) {
                argsTypeBuilder.append(",");
                argsValueBuilder.append("|<-->|");
            }
            String type = args[i].getClass().getName();
            if (type.startsWith("[L")) {
                type = type.substring(2).replaceAll(";", "");
            }
            argsTypeBuilder.append(type);
            argsValueBuilder.append(JsonUtils.toJson(args[i]));
        }
        logBean.setParamTypes(argsTypeBuilder.toString());
        logBean.setParamValues(argsValueBuilder.toString());
        OperEnum operEnum = annot.operationType();
        if (operEnum == null) {
            operEnum = OperEnum.FIND;
        }
        logBean.setOperationType(operEnum.getCode());
        return logBean;
    }

    /**
     * Fields属性值赋值
     * @param logBean
     * @param fieldStrings
     * @param opContext
     */
    private void buildLogBean(LogBean logBean,String[] fieldStrings,LogOperationContext opContext){
        if(fieldStrings == null || fieldStrings.length==0){
            return;
        }
        Field[] fields = LogBean.class.getDeclaredFields();
        String spilt = "=";
        for(Field field:fields){
            try {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                Object obj = field.get(logBean);
                if(obj == null){
                    String fieldName = field.getName();
                    for(String fs:fieldStrings){
                        String[] ss = fs.replaceAll(" ","").split(spilt);
                        if(ss.length > 1 && ss[0].equals(fieldName)){
                            field.set(logBean,opContext.generateFieldValue(fs.substring(fieldName.length()+spilt.length())));
                            break;
                        }
                    }

                }
            } catch (IllegalAccessException e) {
                LOGGER.error("构建LogBean实体对象属性"+field.getName()+"出现异常",e);
            }
        }
    }


    /**
     * 构建参数属性对象
     *
     * @param args
     * @param annotations
     * @return
     */
    private Map<String, String> buildParamMap(Object[] args, Annotation[][] annotations) {
        Map<String, String> paramMap = new HashMap<String, String>();
        if (args == null) {
            return paramMap;
        }
        int index = 0;
        for (Object arg : args) {
            int len = annotations[index].length;
            boolean isPut = false;
            if (len > 0) {
                for (int i = 0; i < len; i++) {
                    Annotation annotation = annotations[index][i];
                    if (annotation.annotationType().isAssignableFrom(Named.class)) {
                        Named named = (Named) annotation;
                        paramMap.put(named.value(), JsonUtils.toJson(arg));
                        isPut = true;
                        break;
                    }
                }
            }
            if (!isPut) {
                Map<String, String> map = BeanHelper.modelToMap(arg);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    if (!paramMap.containsKey(key)) {
                        paramMap.put(key, entry.getValue());
                    }
                }
            }
            index++;
        }
        return paramMap;
    }

    /**
     * 构建操作关键字
     *
     * @param operationKey
     * @param result
     * @param paramMap
     * @return
     */
    private String buildOperationKey(String operationKey, Object result, Map<String, String> paramMap) {
        if (StringUtils.isEmpty(operationKey)) {
            operationKey = "id";
        }
        String value = paramMap.get(operationKey);
        if (StringUtils.isEmpty(value)) {
            try {
                Object obj = BeanHelper.getFieldValue(result, operationKey);
                if (obj == null) {
                    value = "";
                }
            } catch (IllegalAccessException e) {
                LOGGER.error(String.format("记录操作日志获取operationKey=[%s]出现异常", operationKey), e);
            }
        }
        return new StringBuilder(trimSPELKey(operationKey)).append("=").append(value).toString();
    }

    /**
     * SPEL表达式取最后属性名称
     *
     * @param key
     * @return
     */
    private String trimSPELKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        int len = key.lastIndexOf(".");
        if (len > -1 && len < key.length()) {
            return key.substring(len + 1);
        }
        if(key.startsWith("#")){
            key = key.substring(1);
        }
        return key;
    }

    /**
     * 构建tableName
     *
     * @param className 从className中截取
     * @return
     */
    private String buildTableNameFromClass(String className) {
        int dotIndex = className.lastIndexOf(".");
        int sIndex = className.lastIndexOf("Service");
        int startIndex = 0;
        int lastIndex = className.length();
        if (dotIndex > -1) {
            startIndex = dotIndex + 1;
        }
        if (sIndex > -1 && sIndex > startIndex) {
            lastIndex = sIndex;
        }
        return className.substring(startIndex, lastIndex);
    }

    /**
     * 构建第二关键字信息
     *
     * @param secMap
     * @param paramMap
     * @return
     */
    private String buildSecondKeys(Map<String, String> secMap, Map<String, String> paramMap) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Map.Entry<String, String> entry : secMap.entrySet()) {
            String key = trimSPELKey(entry.getKey());
            String value = entry.getValue();
            if (StringUtils.isEmpty(value)) {
                value = paramMap.get(key);
            }
            value = value == null ? "" : value;
            if (count > 0) {
                sb.append(" and ");
            }
            count++;
            sb.append(key).append("=").append(value);
        }
        return sb.toString();
    }

    /**
     * 执行记录log操作
     *
     * @param value
     * @param method
     * @param annot
     */
    private void toExecute(LogBean value, Method method, Loggable annot) {
        String msg = null;
        try {
            msg = new StringBuilder(method.getDeclaringClass().getName()).append(".")
                    .append(String.format("%s: %s to loggable", new Object[]{method.getName(), JsonUtils.toJson(value)}))
                    .toString();
            if (annot.storeType() == LogTypeEnum.DB) {
                LoggableService loggableService = findLoggableService(annot.handlerId());
                if (loggableService != null) {
                    OperEnum operEnum = annot.operationType();
                    if (operEnum == OperEnum.DELETE || operEnum == OperEnum.EDIT) {
                        value.setPid(getPid(loggableService, value));
                    }
                    loggableService.log(value);
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.warn("没有找到LoggableService接口的实现类对象，日志默认将以文件形式输出");
                    }
                }
            }
            LOGGER.info(msg);
        } catch (Exception e) {
            String errMsg = String.format("执行日志记录操作出现异常[%s]", msg);
            LOGGER.error(errMsg, e);
            if (!annot.maskException()) {
                throw new BaseMsgException(errMsg);
            }
        }
    }

    /**
     * 查询上一次操作记录id
     *
     * @param logBean
     * @return
     */
    private Long getPid(LoggableService loggableService, LogBean logBean) {
        if (loggableService == null) {
            return null;
        }
        try {
            LogBean query = new LogBean();
            query.setBusiNo(logBean.getBusiNo());
            List<LogBean> logBeanList = loggableService.findListLogBean(query);
            if (CollectionUtils.isNotEmpty(logBeanList)) {
                Collections.sort(logBeanList, new Comparator<LogBean>() {
                    @Override
                    public int compare(LogBean o1, LogBean o2) {
                        //降序排列
                        return o2.getId().compareTo(o1.getId());
                    }
                });
                return logBeanList.get(0).getId();
            }
        } catch (Exception e) {
            LOGGER.warn(String.format("查询上次日志记录服务出现异常logBean=[%s]", logBean), e);
        }
        return null;
    }

    /**
     * 更加指定beanName获得日志服务实例
     *
     * @param beanName
     * @return
     */
    private LoggableService findLoggableService(String beanName) {
        LoggableService loggableService = null;
        if (StringUtils.isNotEmpty(beanName)) {
            loggableService = SpringUtils.getBean(beanName);
        }
        if (loggableService != null) {
            return loggableService;
        }
        if (this.defaultLoggableService != null) {
            return this.defaultLoggableService;
        }
        return SpringUtils.getBean(LoggableService.class);
    }

    public void setDefaultLoggableService(LoggableService defaultLoggableService) {
        this.defaultLoggableService = defaultLoggableService;
    }

    public LogExpressionEvaluator getEvaluator() {
        return evaluator;
    }

    /**
     * 日志SPEL上下文
     *
     * @param <T>
     */
    private class LogOperationContext<T extends LogOperation> {
        private final T operation;
        private final Object target;
        private final Method method;
        private final Object[] args;
        private final EvaluationContext evalContext;

        public LogOperationContext(T operation, Method method, Object[] args, Object target, Class<?> targetClass) {
            this.operation = operation;
            this.target = target;
            this.method = method;
            this.args = args;
            this.evalContext = evaluator.createEvaluationContext(method, args, target, targetClass);
        }

        /**
         * 生成OperationKey
         */
        protected String generateOperationKey() {
            Object value = null;
            String operationKey = this.operation.getOperationKey();
            try {
                if (!StringUtils.isEmpty(operationKey)) {
                    value = evaluator.key(operationKey, this.method, this.evalContext);
                }
            } catch (Exception e) {
            }
            if (value == null) {
                return "";
            }
            return new StringBuilder(trimSPELKey(operationKey)).append("=").append(JsonUtils.toJson(value)).toString();
        }

        /**
         * 生成SecondaryKeys
         */
        protected Map<String, String> generateSecondaryKeys() {
            Map<String, String> map = new HashMap<String, String>();
            String[] secondaryKeys = this.operation.getSecondaryKeys();
            if (secondaryKeys != null) {
                for (String secondaryKey : secondaryKeys) {
                    Object value = null;
                    try {
                        value = evaluator.key(secondaryKey, this.method, this.evalContext);
                    } catch (Exception e) {
                    }
                    if (value == null) {
                        map.put(secondaryKey, "");
                    } else {
                        map.put(secondaryKey, JsonUtils.toJson(value));
                    }
                }
            }
            return map;
        }

        /**
         * 获得指定属性值
         */
        protected Object generateFieldValue(String operationKey) {
            Object value = null;
            try {
                if (!StringUtils.isEmpty(operationKey)) {
                    value = evaluator.key(operationKey, this.method, this.evalContext);
                }
            } catch (Exception e) {
                value = operationKey;
            }
            return value;
        }
    }
}
