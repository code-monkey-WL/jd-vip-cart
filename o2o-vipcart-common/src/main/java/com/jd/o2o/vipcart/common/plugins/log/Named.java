package com.jd.o2o.vipcart.common.plugins.log;

import java.lang.annotation.*;

/**
 * 入参标记
 * Created by liuhuiqing on 2015/8/18.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Named {

    /**
     * 参数名称
     * @return
     */
    String value();

}

