package com.jd.o2o.vipcart.common.plugins.log.track;

import com.jd.o2o.vipcart.common.plugins.log.support.spel.LogExpressionEvaluator;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;

/**
 * 日志处理切面
 * Created by liuhuiqing on 2015/8/18.
 */
@Aspect
public class LoggerTrackAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerTrackAspect.class);
    /**
     * SPEL表达式解析对象
     */
    private final LogExpressionEvaluator evaluator = new LogExpressionEvaluator();

    /**
     * 增加日志前缀
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* *(..)) && @annotation(com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackable)")
    public Object loggerTrackable(final ProceedingJoinPoint point) throws Throwable {
        boolean isSuccess = LoggerTrackFactory.setTrackLog(buildTrackBean(point));
        Object obj = null;
        //执行业务处理逻辑
        try {
            obj = point.proceed();
        } finally {
            if (isSuccess) {
                LoggerTrackFactory.resetTrackLog();
            }
        }
        return obj;
    }

    private TrackBean buildTrackBean(ProceedingJoinPoint point) {
        TrackBean trackBean = new TrackBean();
        String key = null;
        try {
            Method method = MethodSignature.class.cast(point.getSignature()).getMethod();
            LoggerTrackable annot = method.getAnnotation(LoggerTrackable.class);
            if(annot != null){
                key = annot.key();
            }
            if (StringUtils.isNotEmpty(key)) {
                Object target = point.getThis();
                Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
                if (targetClass == null && target != null) {
                    targetClass = target.getClass();
                }
                EvaluationContext evaluationContext = evaluator.createEvaluationContext(method, point.getArgs(), target, targetClass);
                Object value = evaluator.key(key, method, evaluationContext);
                if (value != null) {
                    trackBean.setKey(new StringBuilder().append(trimSPELKey(key)).append("=").append(value).toString());
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("解析spel表达式[%s]出现异常,param=[%s]", key, point.toLongString()), e);
        }
        return trackBean;
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
        if (key.startsWith("#")) {
            key = key.substring(1);
        }
        return key;
    }

}
