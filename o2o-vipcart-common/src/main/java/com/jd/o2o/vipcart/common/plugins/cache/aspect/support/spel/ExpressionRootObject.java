package com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel;

import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * SPEL表达式上下文对象
 * Created by liuhuiqing on 2015/10/30.
 */
public class ExpressionRootObject {
    protected final Method method;

    protected final Object[] args;

    protected final Object target;

    protected final Class<?> targetClass;


    public ExpressionRootObject(
            Method method, Object[] args, Object target, Class<?> targetClass) {
        Assert.notNull(method, "Method is required");
        Assert.notNull(targetClass, "targetClass is required");
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.args = args;
    }

    public Method getMethod() {
        return this.method;
    }

    public String getMethodName() {
        return this.method.getName();
    }

    public Object[] getArgs() {
        return this.args;
    }

    public Object getTarget() {
        return this.target;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }
}
