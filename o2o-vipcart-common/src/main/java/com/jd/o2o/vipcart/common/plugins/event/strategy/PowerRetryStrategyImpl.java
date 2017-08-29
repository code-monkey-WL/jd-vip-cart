package com.jd.o2o.vipcart.common.plugins.event.strategy;

import org.springframework.stereotype.Component;

/**
 * 指定fixed的 tryCount次幂
 * Class Name: PowerRetryStrategyImpl.java
 * 
 * @author liuhuiqing DateTime 2014-8-27 下午3:53:51
 * @version 1.0
 */
@Component("powerRetryStrategyImpl")
public class PowerRetryStrategyImpl implements RetryStrategy {
    private int maxTry = 4;
	@Override
	public long expiredTime(int tryCount, int fixed) {

		int base = fixed > 0 ? fixed : 1;
        tryCount = tryCount>maxTry?maxTry:tryCount;
		return Double.valueOf(Math.pow(base, maxTry - tryCount)).longValue();
	}
}
