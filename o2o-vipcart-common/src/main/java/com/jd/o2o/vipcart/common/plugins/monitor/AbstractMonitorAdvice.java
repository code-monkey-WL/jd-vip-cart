package com.jd.o2o.vipcart.common.plugins.monitor;

import org.aspectj.lang.JoinPoint;

/**
 * 切入抽象默认实现+监控实现
 * Created by liuhuiqing on 2016/1/15.
 */
public abstract class AbstractMonitorAdvice<T> extends AbstractMonitor<T> implements Advice {

    @Override
    public void doBefore(JoinPoint jp) {

    }

    @Override
    public void doAfter(JoinPoint jp) {

    }

    @Override
    public void doAfterReturning(JoinPoint jp, Object obj) {

    }

    @Override
    public void doAfterThrowing(JoinPoint jp, Throwable ex) {

    }
}
