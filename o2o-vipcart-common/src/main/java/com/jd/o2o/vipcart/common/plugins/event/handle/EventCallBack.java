package com.jd.o2o.vipcart.common.plugins.event.handle;


import com.jd.o2o.vipcart.common.plugins.event.O2OEvent;

public interface EventCallBack {
	/**
	 *  重试失败回调函数
	 *  @param event
	 *  @return
	 *  @author liuhuiqing  DateTime 2014-9-25 下午6:06:44
	 */
	public Object callBackFail(O2OEvent event);
	
	/**
	 *  执行成功回调函数
	 *  @param event
	 *  @return
	 *  @author liuhuiqing  DateTime 2014-9-25 下午6:06:37
	 */
	public Object callBackSuccess(O2OEvent event);
}
