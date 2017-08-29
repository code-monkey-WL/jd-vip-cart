package com.jd.o2o.vipcart.common.plugins.cache.aspect;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存（内存）
 * Created by liuhuiqing on 2015/8/13.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LocalCached {
    /**
     * 缓存生效时间
     */
    int lifetime() default 1;

    /**
     * 缓存生效时间单位
     */
    TimeUnit unit() default TimeUnit.MINUTES;

    /**
     * 缓存永不失效
     */
    boolean forever() default false;

    /**
     * 调用业务方法前先调用指定类的静态方法flushBefore()并根据返回值决定是否清空缓存数据
     * 返回值为true则清空（需重新调用业务方法）缓存数据，false不会清空（使用缓存数据）
     * For example:
     *
     * <pre> class Foo {
     *   &#64;Cacheable(before = Foo.class)
     *   int read() {
     *     // return some number
     *   }
     *   public static boolean flushBefore() {
     *   // if TRUE is returned, flushing will happen before
     *   // the call to #read()
     *   }
     * }</pre>
     *
     */
    Class<?>[] before() default { };

    /**
     * 调用业务方法后调用指定类的静态方法flushAfter()并根据返回值决定是否清空缓存数据
     * 返回值为true则清空缓存数据，false不会清空
     *
     * <p>After calling the method, call static method {@code flushAfter()}
     * in this class and, according to its result, either flush or not.
     * For example:
     *
     * <pre> class Foo {
     *   &#64;Cacheable(after = Foo.class)
     *   int read() {
     *     // return some number
     *   }
     *   public static boolean flushAfter() {
     *   // if TRUE is returned, flushing will happen after
     *   // the call to #read()
     *   }
     * }</pre>
     *
     */
    Class<?>[] after() default { };

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

    /**
     * Identifies a method that should flush all cached entities of
     * this class/object, before being executed.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface FlushBefore {
    }

    /**
     * Identifies a method that should flush all cached entities of
     * this class/object, after being executed.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface FlushAfter {
    }
}
