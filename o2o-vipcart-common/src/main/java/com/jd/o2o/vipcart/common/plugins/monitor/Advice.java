package com.jd.o2o.vipcart.common.plugins.monitor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 方法切入抽象
 * Created by liuhuiqing on 2016/1/15.
 */
public interface Advice {
    /**
     * 前置通知，放在方法头上
     * @param jp
     */
    public void doBefore(JoinPoint jp);

    /**
     * 后置【finally】通知，放在方法头上
     * @param jp
     */
    public void doAfter(JoinPoint jp);

    /**
     * 后置【try】通知，放在方法头上，使用returning来引用方法返回值
     * @param jp
     * @param obj
     */
    public void doAfterReturning(JoinPoint jp, Object obj);

    /**
     * 后置【catch】通知，放在方法头上，使用throwing来引用抛出的异常
     * @param jp
     * @param ex
     */
    public void doAfterThrowing(JoinPoint jp, Throwable ex);
}
