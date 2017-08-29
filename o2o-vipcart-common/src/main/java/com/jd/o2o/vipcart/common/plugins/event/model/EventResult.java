package com.jd.o2o.vipcart.common.plugins.event.model;

import com.jd.o2o.vipcart.common.domain.BaseBean;
import com.jd.o2o.vipcart.common.plugins.event.handle.EventCallBack;
import com.jd.o2o.vipcart.common.utils.StringPrintWriter;

/**
 * 通用调用结果类
 * 
 * @author liuhuiqing
 */
public class EventResult extends BaseBean {

	private static final long serialVersionUID = -3257221965633725231L;
	private boolean isRet;// 调用是否成功
	private String retMsg;// 结果状态描述
	private EventCallBack eventCallBack;

	public EventResult() {
	}
	
	public EventResult(boolean isRet) {
		super();
		this.isRet = isRet;
	}

	public EventResult(boolean isRet, String retMsg) {
		super();
		this.isRet = isRet;
		this.retMsg = retMsg;
	}

	public EventResult(boolean isRet, Exception e) {
		super();
		this.isRet = isRet;
		this.retMsg = exceptionToString(e);
	}

	public EventResult(boolean isRet, String retMsg,
			EventCallBack eventCallBack) {
		super();
		this.isRet = isRet;
		this.retMsg = retMsg;
		this.eventCallBack = eventCallBack;
	}

	public boolean isRet() {
		return isRet;
	}

	public void setRet(boolean isRet) {
		this.isRet = isRet;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	
	public EventCallBack getEventCallBack() {
		return eventCallBack;
	}

	public void setEventCallBack(EventCallBack eventCallBack) {
		this.eventCallBack = eventCallBack;
	}

	private String exceptionToString(Exception e){
		return new StringPrintWriter().getExceptionMsg(e);
	}
}
