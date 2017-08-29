package com.jd.o2o.vipcart.common.plugins.event.handle;


import com.jd.o2o.vipcart.common.plugins.event.O2OEvent;
import com.jd.o2o.vipcart.common.plugins.event.model.EventResult;

/**
 *  事件处理
 *  Class Name: O2OEventHandle.java   
 *  @author liuhuiqing DateTime 2014-7-25 上午9:44:04    
 *  @version 1.0 
 */
public interface O2OEventHandle {
	
	public EventResult handleEvent(O2OEvent event);
	
}
