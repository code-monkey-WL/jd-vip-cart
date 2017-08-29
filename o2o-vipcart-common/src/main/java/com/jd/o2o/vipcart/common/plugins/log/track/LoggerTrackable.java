package com.jd.o2o.vipcart.common.plugins.log.track;

import java.lang.annotation.*;

/**
 * 日志追加前后缀功能
 * Created by liuhuiqing on 2017/7/5.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface LoggerTrackable {
    /**
     * 主键属性名称,使用 SpEL 编写
     *
     * @return
     */
    String key() default "";
}
