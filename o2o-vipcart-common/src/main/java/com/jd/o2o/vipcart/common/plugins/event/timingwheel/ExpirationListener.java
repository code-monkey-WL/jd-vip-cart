package com.jd.o2o.vipcart.common.plugins.event.timingwheel;
/**
 *  过期元素事件处理
 *  Class Name: ExpirationListener.java   
 *  @author liuhuiqing DateTime 2014-8-27 下午3:23:38    
 *  @version 1.0 
 *  @param <E>
 */
public interface ExpirationListener<E> {
	void expired(E expiredObject);
}
