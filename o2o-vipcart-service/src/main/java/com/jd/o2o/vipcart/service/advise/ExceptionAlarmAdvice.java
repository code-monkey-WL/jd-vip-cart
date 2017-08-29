package com.jd.o2o.vipcart.service.advise;

import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.plugins.monitor.AbstractAdvice;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;

/**
 * 依赖服务异常报警
 * Created by liuhuiqing on 2017/4/25.
 */
public class ExceptionAlarmAdvice extends AbstractAdvice {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(ExceptionAlarmAdvice.class);
    @Override
    public void doAfterThrowing(JoinPoint jp, Throwable ex) {
        try{
            String msg = String.format("新优惠券系统依赖服务[%s]调用出现异常：[%s]",jp.getSignature().toShortString(),ex.getMessage());
            LOGGER.error(new StringBuilder(msg).append("入参：").append(JsonUtils.toJson(jp.getArgs())).toString(),ex);
//            Profiler.businessAlarm(UMPAlermKeyEnum.RPC_EXCEPTION.getCode(), msg);
            super.doAfterThrowing(jp, ex);
        }catch (Throwable t){
            LOGGER.error("ExceptionAlarmAdvice出现异常，请检查！",t);
        }
    }
}
