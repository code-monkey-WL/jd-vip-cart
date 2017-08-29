package com.jd.o2o.vipcart.service.advise;

import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.plugins.monitor.AbstractAdviceInterceptorImpl;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 对外api通用处理逻辑切面
 * Created by liuhuiqing on 2016/01/12.
 */
@Aspect
public class RPCAdviceImpl extends AbstractAdviceInterceptorImpl {
    private static final org.slf4j.Logger LOGGER = LoggerTrackFactory.getLogger(RPCAdviceImpl.class);

    @Pointcut("execution (* com.jd.o2o.vipcart.rpc.*.*(..))")
    @Override
    protected void apiAuthorityCut() {
    }
}
