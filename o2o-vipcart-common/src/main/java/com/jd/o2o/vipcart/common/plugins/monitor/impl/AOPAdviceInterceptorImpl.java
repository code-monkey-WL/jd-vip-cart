package com.jd.o2o.vipcart.common.plugins.monitor.impl;

import com.jd.o2o.vipcart.common.plugins.monitor.AbstractAdviceInterceptorImpl;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * AOP配置拦截实现
 * Created by liuhuiqing on 2017/7/28.
 */
@Aspect
public class AOPAdviceInterceptorImpl extends AbstractAdviceInterceptorImpl {
    @Override
    protected void apiAuthorityCut() {
    }
}
