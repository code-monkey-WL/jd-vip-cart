package com.jd.o2o.vipcart.common.plugins.monitor;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * 方法拦截器
 * Created by liuhuiqing on 2016/01/15.
 */
public abstract class AbstractAdviceInterceptorImpl implements AdviceInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAdviceInterceptorImpl.class);

    private List<Advice> adviceList;

//    @Pointcut("execution (* com.jd.o2o..api.*.*(..))")
    protected abstract void apiAuthorityCut();

    @Override
    @Before("apiAuthorityCut()")
    public void doBefore(JoinPoint jp) {
        if(CollectionUtils.isEmpty(adviceList)){
            return;
        }
        for(Advice advice:adviceList){
            advice.doBefore(jp);
        }
    }

    @Override
    @Around("apiAuthorityCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch sw = new StopWatch(getClass().getSimpleName());
        try {
            sw.start(pjp.getSignature().toShortString());
            return pjp.proceed();
        }finally {
            sw.stop();
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug(sw.prettyPrint());
            }
        }
    }

    @Override
    @After("apiAuthorityCut()")
    public void doAfter(JoinPoint jp) {
        if(CollectionUtils.isEmpty(adviceList)){
            return;
        }
        for(Advice advice:adviceList){
            advice.doAfter(jp);
        }
    }

    @Override
    @AfterReturning(value = "apiAuthorityCut()",returning = "obj")
    public void doAfterReturning(JoinPoint jp, Object obj) {
        if(CollectionUtils.isEmpty(adviceList)){
            return;
        }
        for(Advice advice:adviceList){
            advice.doAfterReturning(jp,obj);
        }
    }

    @Override
    @AfterThrowing(value="apiAuthorityCut()",throwing = "ex")
    public void doAfterThrowing(JoinPoint jp, Throwable ex) {
        if(CollectionUtils.isEmpty(adviceList)){
            return;
        }
        for(Advice advice:adviceList){
            advice.doAfterThrowing(jp,ex);
        }
    }

    public List<Advice> getAdviceList() {
        return adviceList;
    }

    public void setAdviceList(List<Advice> adviceList) {
        this.adviceList = adviceList;
    }
}
