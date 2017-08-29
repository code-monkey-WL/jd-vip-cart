package com.jd.o2o.vipcart.common.utils;

import com.jd.o2o.vipcart.common.domain.BaseBean;
import com.jd.o2o.vipcart.common.domain.criteria.SortCriteria;
import com.jd.o2o.vipcart.common.domain.enums.SortEnum;
import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * javaBean公共处理方法
 * add by liuhuiqing
 */
public class BeanHelper extends BeanUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanHelper.class);

    /**
     * 将数组对象转换成指定类型的数组对象
     *
     * @param sourceList 原数组对象
     * @param target     要转换的目标数组对象类型
     * @param <T>
     * @return
     */
    public static <T> Collection<T> copyTo(Collection<?> sourceList, Class<T> target) {
        List<T> list = new ArrayList<T>();
        if (CollectionUtils.isEmpty(sourceList)) {
            return list;
//            return Collections.emptyList();// jsf调用saf服务时，该方法生成的数组反序列化会有问题 add by liuhuiqing
        }
        try {
            for (Object o : sourceList) {
                list.add(copyTo(o, target));
            }
        } catch (Exception e) {
            LOGGER.error("数组复制出现错误source=[{}],targetType=[{}],errMsg=[{}]", new Object[]{sourceList, target, e.getMessage()});
            throw new BaseMsgException(e.getMessage());
        }
        return list;
    }

    /**
     * 对象类型转换
     *
     * @param sourceObj 原对象
     * @param target    要转换成的目标对象类型
     * @param <T>
     * @return
     */
    public static <T> T copyTo(Object sourceObj, Class<T> target) {
        if (sourceObj == null) {
            return null;
        }
        try {
            T e = target.newInstance();
            BeanUtils.copyProperties(sourceObj, e);
            return e;
        } catch (Exception e) {
            LOGGER.error("对象复制出现错误source=[{}],targetType=[{}],errMsg=[{}]", new Object[]{sourceObj, target, e.getMessage()});
            throw new BaseMsgException(e.getMessage());
        }
    }

    /**
     * 将对象转换成map
     *
     * @param obj
     * @param prefix 生成map的key前缀
     * @param suffix 生成map的key后缀
     * @return Map<String,Object>
     * @author liuhuiqing
     * @date 2013-9-27 下午4:08:57
     */
    public static Map<String, Object> modelToMap(Object obj, String prefix, String suffix) {
        Map<String, Object> result = new HashMap<String, Object>();
        if(obj == null){
            return result;
        }
        if(obj instanceof Map){
            return (Map<String, Object>) obj;
        }
        Class<? extends Object> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fs = clazz.getDeclaredFields();
            for (int i = 0; i < fs.length; i += 1) {
                if (Modifier.isStatic(fs[i].getModifiers())) {
                    continue;
                }
                boolean isAccessible = fs[i].isAccessible();
                if(!isAccessible){
                    fs[i].setAccessible(true);
                }
                try {
                    Object value = fs[i].get(obj);
                    String key = new StringBuffer(StringUtils.isBlank(prefix) ? ""
                            : prefix).append(fs[i].getName()).append(
                            StringUtils.isBlank(suffix) ? "" : suffix).toString();
                    if (value != null && !result.containsKey(key)) {
                        result.put(key, value);
                    }
                } catch (Exception e) {
                    LOGGER.error("对象转map出现错误source=[{}],prefix=[{}],suffix=[{}],errMsg=[{}]", new Object[]{obj, prefix, suffix, e.getMessage()});
                    throw new BaseMsgException(e.getMessage());
                }
                if(!isAccessible){
                    fs[i].setAccessible(isAccessible);
                }
            }
        }
        return result;
    }

    /**
     * 将javabean对象转换成map<String,String>对象
     *
     * @param obj
     * @return
     */
    public static Map<String, String> modelToMap(Object obj) {
        Map<String, String> result = new HashMap<String, String>();
        if(obj == null){
            return result;
        }
        if(obj instanceof Map){
            return (Map<String, String>) obj;
        }
        Class<? extends Object> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fs = clazz.getDeclaredFields();
            for (int i = 0; i < fs.length; i += 1) {
                if (Modifier.isStatic(fs[i].getModifiers())) {
                    continue;
                }
                boolean isAccessible = fs[i].isAccessible();
                if(!isAccessible){
                    fs[i].setAccessible(true);
                }
                try {
                    Object value = fs[i].get(obj);
                    if (value != null) {
                        if(value instanceof Date){
                            value = DateFormatUtils.format((Date)value,DateFormatUtils.DATE_MODEL_2);
                        }
                        result.put(fs[i].getName(), String.valueOf(value));
                    }
                } catch (Exception e) {
                    LOGGER.error("对象转map出现错误source=[{}],errMsg=[{}]", new Object[]{obj, e.getMessage()});
                    throw new BaseMsgException(e.getMessage());
                }
                if(!isAccessible){
                    fs[i].setAccessible(isAccessible);
                }
            }
        }
        return result;
    }
    public static <T> T mapToModel(Map<String, String> map, Class<T> clazz) {
        return mapToModel(map,clazz,null);
    }
    /**
     * 将map转换成指定类型的对象
     * @param map
     * @param clazz 类型必须有无参构造方法
     * @param excludes 不需要转换的字段集合
     * @return Object
     * @author liuhuiqing
     * @date 2013-9-27 下午4:09:23
     */
    public static <T> T mapToModel(Map<String, String> map, Class<T> clazz,String[] excludes) {
        try {
            Set<String> keySet = map.keySet();
            if(excludes!=null && excludes.length > 0){
                keySet.removeAll(Arrays.asList(excludes));
            }
            T result = clazz.newInstance();
            for (String attr : keySet) {
                Field field = null;
                try {
                    field = clazz.getDeclaredField(attr);
                } catch (NoSuchFieldException e) {
                    continue;
                }
                boolean isAccessible = field.isAccessible();
                if(!isAccessible){
                    field.setAccessible(true);
                }
                Class<?> typeClazz = field.getType();
                Object o = map.get(attr);
                if (o == null) continue;
                if (typeClazz.equals(String.class)) {
                    String s = o.toString();
                    field.set(result, s);
                } else if (typeClazz.equals(Integer.class)) {
                    field.set(result, Integer.parseInt(o.toString()));
                } else if (typeClazz.equals(Long.class)) {
                    field.set(result, Long.parseLong(o.toString()));
                } else if (typeClazz.equals(Date.class)) {
                    String dateType = DateFormatUtils.getFormat(o.toString());
                    if (dateType != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat(dateType);
                        Date date = sdf.parse(o.toString());
                        field.set(result, date);
                    }
                } else if (typeClazz.equals(Float.class)) {
                    field.set(result, Float.parseFloat(o.toString()));
                } else if (typeClazz.equals(Double.class)) {
                    field.set(result, Double.parseDouble(o.toString()));
                }
                if(!isAccessible){
                    field.setAccessible(isAccessible);
                }
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("map转对象出现错误source=[{}],targetType=[{}],errMsg=[{}]", new Object[]{map, clazz, e.getMessage()});
            throw new BaseMsgException(e.getMessage());
        }
    }

    /**
     * 得到指定类型的指定位置的泛型实参
     *
     * @param clazz
     * @param index
     * @param <T>
     * @return
     */
    public static <T> Class<T> findParameterizedType(Class<?> clazz, int index) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Type parameterizedType = clazz.getGenericSuperclass();
            if(parameterizedType == null){
                return null;
            }
            //CGLUB subclass target object(泛型在父类上)
            if (!(parameterizedType instanceof ParameterizedType)) {
                continue;
            }
            Type[] actualTypeArguments = ((ParameterizedType) parameterizedType).getActualTypeArguments();
            if (actualTypeArguments == null || actualTypeArguments.length == 0 || index > actualTypeArguments.length) {
                return null;
            }
            return (Class<T>) actualTypeArguments[index];
        }
        return null;
    }

    /**
     * 数组排序：数组里待排序对象属性类型必须实现Comparable接口
     * 排序对象属性值为null的排在后面
     * @param list         待排序数组
     * @param sortCriteria 排序规则
     * @param <T>
     */
    public static <T> void sort(final List<T> list, final SortCriteria sortCriteria) {
        if (CollectionUtils.isEmpty(list)
                || sortCriteria == null
                || CollectionUtils.isEmpty(sortCriteria.getSortBeanList())) {
            return;
        }
        final Class clazz = list.get(0).getClass();
        Collections.sort(list, new Comparator<T>() {
            public int compare(T a, T b) {
                int r = 0;
                try {
                    for (SortCriteria.SortBean sortBean : sortCriteria.getSortBeanList()) {
                        Field field = clazz.getDeclaredField(sortBean.getName());
                        boolean isAccessible = field.isAccessible();
                        if(!isAccessible){
                            field.setAccessible(true);
                        }
                        Object av = field.get(a);
                        Object bv = field.get(b);
                        if(!isAccessible){
                            field.setAccessible(isAccessible);
                        }
                        if(av==null){
                            return 1;
                        }
                        if(bv==null){
                            return -1;
                        }
                        if (!(av instanceof Comparable)) {
                            throw new BaseMsgException(String.format("属性[%s]的类型[%s]没有实现Comparable接口",new Object[]{sortBean.getName(),field.getType().getName()}));
                        }
                        Comparable ca = (Comparable) av;
                        Comparable cb = (Comparable) bv;
                        if (sortBean.getSortEnum() == SortEnum.DESC) {
                            r = cb.compareTo(ca);
                        } else {
                            r = ca.compareTo(cb);
                        }
                        if (r != 0) {
                            return r;
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("数组排序错误source=[{}],sort=[{}],errMsg=[{}]", new Object[]{list, sortCriteria, e.getMessage()});
                    throw new BaseMsgException(e.getMessage());
                }
                return r;
            }
        });
    }

    /**
     * 对象拷贝(或直接用org.apache.commons.lang.SerializationUtils.clone();)
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T extends Serializable> T clone(T obj) {
        // 拷贝产生的对象
        T clonedObj = null;
        try {
            // 读取对象字节数据
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            // 分配内存空间，写入原始对象，生成新对象
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            //返回新对象，并做类型转换
            clonedObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            LOGGER.error("数据复制出现错误source=[{}],errMsg=[{}]", new Object[]{obj, e.getMessage()});
            throw new BaseMsgException(e.getMessage());
        }
        return clonedObj;
    }

    /**
     * 获取 目标对象
     * @param proxyObject 代理对象
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxyObject) throws Exception {

        if(!AopUtils.isAopProxy(proxyObject)) {
            return proxyObject;//不是代理对象
        }
        if(AopUtils.isJdkDynamicProxy(proxyObject)) {
            return getJdkDynamicProxyTargetObject(proxyObject);
        } else { //cglib
            return getCglibProxyTargetObject(proxyObject);
        }
    }

    /**
     * 从对象中取出指定字段属性的值
     * @param obj 查找对象
     * @param fieldName 对象属性名称
     * @param <T> 对象属性对应的值
     * @return
     * @throws IllegalAccessException
     */
    public static  <T> T getFieldValue(Object obj,String fieldName) throws IllegalAccessException {
        if(obj == null){
            return null;
        }
        Field field = ReflectionUtils.findField(obj.getClass(),fieldName);
        if(field == null){
            return null;
        }
        field.setAccessible(true);
        return (T) field.get(obj);
    }

    /**
     * 字符串转对象类型（仅限原生类型，时间对象的转换）
     * @param value
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T primitiveToObject(String value, Class<T> target){
        if(target.isPrimitive()){
            if(target.isAssignableFrom(Integer.TYPE)){
                return (T) Integer.valueOf(value);
            }
            if(target.isAssignableFrom(Long.TYPE)){
                return (T) Long.valueOf(value);
            }
            if(target.isAssignableFrom(Double.TYPE)){
                return (T) Double.valueOf(value);
            }
            if(target.isAssignableFrom(Float.TYPE)){
                return (T) Float.valueOf(value);
            }
            if(target.isAssignableFrom(Boolean.TYPE)){
                return (T) Boolean.valueOf(value);
            }
            if(target.isAssignableFrom(Byte.TYPE)){
                return (T) Byte.valueOf(value);
            }
            if(target.isAssignableFrom(Short.TYPE)){
                return (T) Short.valueOf(value);
            }
        }
        if(target.isAssignableFrom(Integer.class)){
            return (T) Integer.valueOf(value);
        }
        if(target.isAssignableFrom(Long.class)){
            return (T) Long.valueOf(value);
        }
        if(target.isAssignableFrom(Double.class)){
            return (T) Double.valueOf(value);
        }
        if(target.isAssignableFrom(Float.class)){
            return (T) Float.valueOf(value);
        }
        if(target.isAssignableFrom(String.class)){
            return (T) value;
        }
        if(target.isAssignableFrom(Boolean.class)){
            return (T) Boolean.valueOf(value);
        }
        if(target.isAssignableFrom(Byte.class)){
            return (T) Byte.valueOf(value);
        }
        if(target.isAssignableFrom(Short.class)){
            return (T) Short.valueOf(value);
        }
        if(target.isAssignableFrom(Date.class)){
            try {
                return (T)DateFormatUtils.parse(value);
            } catch (ParseException e) {
                throw new BaseMsgException(e.getMessage());
            }
        }
        throw new BaseMsgException("不支持的对象类型转换");
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

        return target;
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();
        return target;
    }

    /**
     * 获得指定类型的注解信息
     * @param ae
     * @param annotationType
     * @param <T>
     * @return
     */
    public static <T extends Annotation> Collection<T> getAnnotations(AnnotatedElement ae, Class<T> annotationType) {
        Collection<T> anns = new ArrayList<T>(2);

        // look at raw annotation
        T ann = ae.getAnnotation(annotationType);
        if (ann != null) {
            anns.add(ann);
        }
        // scan meta-annotations
        for (Annotation metaAnn : ae.getAnnotations()) {
            ann = metaAnn.annotationType().getAnnotation(annotationType);
            if (ann != null) {
                anns.add(ann);
            }
        }
        return (anns.isEmpty() ? null : anns);
    }



    public static void main(String args[]) {
        A a = new A(1, "c", 333l);
        A b = new A(2, "b", 331l);
        A c = new A(3, "a", 332l);

        A d = new A(1, "b", 331l);
        A e = new A(1, "b", 332l);
        List<A> list = new ArrayList<A>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        sort(list, new SortCriteria()
                .buildSortCriteria("a", SortEnum.DESC)
                .buildSortCriteria("b", SortEnum.ASC)
                .buildSortCriteria("c", SortEnum.DESC));
        for (A t : list) {
            System.out.println(t);
        }
    }

    public static class A extends BaseBean {
        private Integer a;
        private String b;
        private Long c;

        public A(Integer a, String b, Long c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

}
