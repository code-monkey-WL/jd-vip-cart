package com.jd.o2o.vipcart.common.plugins.cache.aspect;

import java.lang.annotation.*;

/**
 * Created by liuhuiqing on 2015/10/30.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheEvict {

    /**
     * Spring Expression Language (SpEL) attribute for computing the key dynamically.
     * <p>Default is "", meaning all method parameters are considered as a key.
     */
    String key() default "";

    /**
     * Spring Expression Language (SpEL) attribute used for conditioning the method caching.
     * <p>Default is "", meaning the method is always cached.
     */
    String condition() default "";

    /**
     * Whether or not all the entries inside the cache(s) are removed or not. By
     * default, only the value under the associated key is removed.
     * <p>Note that specifying setting this parameter to true and specifying a
     * {@link CacheKey key} is not allowed.
     */
    boolean allEntries() default false;

    /**
     * Whether the eviction should occur after the method is successfully invoked (default)
     * or before. The latter causes the eviction to occur irrespective of the method outcome (whether
     * it threw an exception or not) while the former does not.
     */
    boolean beforeInvocation() default false;

    /**
     * 缓存与业务是否同步处理
     * @return
     */
    boolean sync() default true;
    /**
     * 业务方法执行结果返回值对象里的判断业务方法调用结果成功与否的方法名称
     * 如果值为空，则将结果进行缓存（抛出异常除外）
     * 如果值不为空，则只有返回true时进行结果缓存
     * For example:
     * 服务返回结果类定义
     * <pre> class ServiceResponse {
     *   private java.lang.String code;
     *   private java.lang.String msg;
     *   private java.lang.String detail;
     *   private T result;
     *   public boolean isSuccess() {
     *      return "0".equels("code");
     *   }
     * }</pre>
     * @return
     */
    String successMethod() default "";
}