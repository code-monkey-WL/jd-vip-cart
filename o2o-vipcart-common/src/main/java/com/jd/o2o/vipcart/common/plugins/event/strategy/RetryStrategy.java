package com.jd.o2o.vipcart.common.plugins.event.strategy;


 /**
  *  消息重试策略
  *  Class Name: RetryStrategy.java   
  *  @author liuhuiqing DateTime 2014-8-14 下午3:51:37    
  *  @version 1.0
  */
public interface RetryStrategy {
	/**
	 *  过期时间计算方法
	 *  @param tryCount 第几次重试
	 *  @param fixed 固定值
	 *  @return
	 *  @author liuhuiqing  DateTime 2014-8-27 下午3:47:11
	 */
	public long expiredTime(int tryCount, int fixed);
}
