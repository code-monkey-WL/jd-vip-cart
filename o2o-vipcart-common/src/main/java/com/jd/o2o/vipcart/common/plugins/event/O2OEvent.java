package com.jd.o2o.vipcart.common.plugins.event;

/**
 *  消息事件实体类
 *  Class Name: Event.java   
 *  @author liuhuiqing DateTime 2014-7-25 上午9:33:19    
 *  @version 1.0
 */
public class O2OEvent {
	
	private Object data;//消息主体
	
	private String eventCode;//业务code
	
	private int tryCount=3;//消息重试次数
	
	private int tryFixed=1;//重试策略基础值

	private int tryStrategy=1;//对应RetryTypeEnum类型取值
	
	public O2OEvent() {
		super();
	}

	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}


	public String getEventCode() {
		return eventCode;
	}


	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}


	public int getTryCount() {
		return tryCount;
	}


	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}
	
	public int getTryFixed() {
		return tryFixed;
	}

	public void setTryFixed(int tryFixed) {
		this.tryFixed = tryFixed;
	}

	public int getTryStrategy() {
		return tryStrategy;
	}

	public void setTryStrategy(int tryStrategy) {
		this.tryStrategy = tryStrategy;
	}

	@Override
	public String toString(){
	    StringBuffer eventStr=new StringBuffer("o2o event =[");
	    eventStr.append("eventCode="+eventCode+";");
	    eventStr.append("tryCount="+tryCount+";");
	    eventStr.append("tryFixed="+tryFixed+";");
	    eventStr.append("tryStrategy="+tryStrategy+";");
	    eventStr.append("data="+data.toString()+";");
	    eventStr.append("]");
		return eventStr.toString();
	}

}
