package com.jd.o2o.vipcart.common.plugins.cache.aspect;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式缓存
 * Created by liuhuiqing on 2015/8/13.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cached {
    /**
     * 缓存的 key，可以为空
     * 如果指定要按照 SpEL 表达式编写
     * 如果不指定，则缺省按照方法及所有参数进行组合
     */
    String key() default "";

    /**
     * 缓存的条件，可以为空，使用 SpEL 编写
     * 返回 true 或者 false，只有为 true 才进行缓存
     * @return
     */
    String condition() default "";
    /**
     * 缓存生效时间
     */
    int lifetime() default 1;

    /**
     * 指定的缓存生效时间的基础上延长范围幅度阀值，用来削峰填谷
     * 例如：
     *  lifetime=5 unit=TimeUnit.MINUTES step=1
     *  则缓存的生效时间计算公式为：（lifetime+step*random）*unit 其中random为0到1的随机数
     *  即五分钟到六分钟之间的随机值
     * @return
     */
    int step() default 0;

    /**
     * 缓存生效时间单位
     */
    TimeUnit unit() default TimeUnit.MINUTES;

    /**
     * 缓存永不失效
     */
    boolean forever() default false;

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
