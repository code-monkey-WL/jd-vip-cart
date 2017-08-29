package com.jd.o2o.vipcart.common.plugins.event.strategy;

import org.springframework.stereotype.Component;

/**
 *  固定fixed时间过期
 *  Class Name: FixedRetryStrategyImpl.java   
 *  @author liuhuiqing DateTime 2014-8-27 下午4:00:55    
 *  @version 1.0
 */
@Component("fixedRetryStrategyImpl")
public class FixedRetryStrategyImpl implements RetryStrategy {

	@Override
	public long expiredTime(int tryCount, int fixed) {
		return fixed>0?fixed:0;
	}
}
