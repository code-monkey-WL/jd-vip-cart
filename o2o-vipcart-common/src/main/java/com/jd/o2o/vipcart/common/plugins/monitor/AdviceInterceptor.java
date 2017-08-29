package com.jd.o2o.vipcart.common.plugins.monitor;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 方法拦截器
 * Created by liuhuiqing on 2016/01/15.
 */
public interface AdviceInterceptor extends Advice {
    /**
     * 环绕通知，放在方法头上，这个方法要决定真实的方法是否执行，而且必须有返回值
     * @param pjp
     * @return
     */
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable;
}
