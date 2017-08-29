package com.jd.o2o.vipcart.common.plugins.monitor.impl;

import com.jd.o2o.vipcart.common.plugins.monitor.AbstractAdviceInterceptorImpl;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 默认拦截实现
 * Created by liuhuiqing on 2016/1/15.
 */
@Aspect
public class DefaultAdviceInterceptorImpl extends AbstractAdviceInterceptorImpl {
    @Pointcut("execution (* com.jd.o2o..api.*.*(..))")
    @Override
    protected void apiAuthorityCut() {
    }
}
