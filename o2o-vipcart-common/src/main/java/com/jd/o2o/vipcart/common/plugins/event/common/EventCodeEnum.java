package com.jd.o2o.vipcart.common.plugins.event.common;


 /**
  * 事件对应业务编码
 *  Class Name: EventCodeEnum.java   
 *  @author liuhuiqing DateTime 2014-7-25 上午11:05:27    
 *  @version 1.0 
 */
public enum EventCodeEnum {
	MESSAGESEND("4000001","短信发送功能");
	private String code;
	private String desc;
	private EventCodeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	
}
