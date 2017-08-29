package com.jd.o2o.vipcart.common.plugins.log;

import com.jd.o2o.vipcart.common.domain.enums.OperEnum;

import java.lang.annotation.*;

/**
 * 日志记录
 * Created by liuhuiqing on 2015/8/18.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Loggable {

    /**
     * 操作类型
     * @return
     */
    OperEnum operationType();

    /**
     * 日志记录持久化类型
     * @return
     */
    LogTypeEnum storeType() default LogTypeEnum.FILE;
    /**
     * 日志与业务是否同步处理
     * @return
     */
    boolean sync() default false;

    /**
     * 日志记录出现异常是否屏蔽抛出
     * @return
     */
    boolean maskException() default true;

    /**
     * 操作表名称
     * @return
     */
    String tableName() default "";

    /**
     * 主键属性名称,使用 SpEL 编写
     * @return
     */
    String operationKey() default "";

    /**
     * 备选关键字段属性名称集合,使用 SpEL 编写
     * @return
     */
    String[] secondaryKeys() default {};
    /**
     * 记录log记录的服务对象id
     * @return
     */
    String handlerId() default "";
    /**
     * 业务方法执行结果返回值对象里的判断业务方法调用结果成功与否的方法名称
     * 如果值为空，则将结果进行操作记录（抛出异常除外）
     * 如果值不为空，则只有返回true时进行日志记录
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
     * 使用 SpEL 编写，通用属性赋值或扩展属性赋值
     * 编写格式为“属性名=SpEL表达式”
     * 属性名必须是com.jd.o2o.vipcart.common.plugins.log.LogBean里定义的属性
     * @return
     */
    String[] fields() default {};

}

