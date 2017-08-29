package com.jd.o2o.vipcart.common.plugins.log.support.spel;


import com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel.ExpressionRootObject;

import java.lang.reflect.Method;

/**
 * SPEL表达式缓存上下文对象
 * Created by liuhuiqing on 2015/10/30.
 */
public class LogExpressionRootObject extends ExpressionRootObject {

    public LogExpressionRootObject(Method method, Object[] args, Object target, Class<?> targetClass) {
        super(method, args, target, targetClass);
    }
}
