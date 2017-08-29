package com.jd.o2o.vipcart.common.plugins.cache.aspect;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存备份
 * Created by liuhuiqing on 2017/7/3.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Backup {
    /**
     * 缓存的 key，可以为空
     * 如果指定要按照 SpEL 表达式编写
     * 如果不指定，则缺省按照方法及所有参数进行组合
     */
    String key() default "";
    /**
     * 缓存生效时间
     */
    int lifetime() default 24*60;

    /**
     * 缓存生效时间单位
     */
    TimeUnit unit() default TimeUnit.MINUTES;

    /**
     * 缓存永不失效
     */
    boolean forever() default false;

    /**
     * 刷新间隔时间
     * @return
     */
    int refreshInterval() default 10;
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
