package com.jd.o2o.vipcart.common.plugins.monitor.impl;
import com.jd.o2o.vipcart.common.plugins.monitor.AbstractMonitorAdvice;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统计访问次数
 * Created by liuhuiqing on 2016/1/15.
 */
public class AccessCountAdvice extends AbstractMonitorAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessCountAdvice.class);

    @Override
    public void doBefore(JoinPoint jp) {
        doMonitor(jp);
    }

    @Override
    protected Object buildValue(Object value) {
        Long count = 0L;
        if(value != null){
            count = (Long) value;
        }
        return ++count;
    }
}
