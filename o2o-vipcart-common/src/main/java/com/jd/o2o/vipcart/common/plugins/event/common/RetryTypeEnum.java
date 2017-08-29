package com.jd.o2o.vipcart.common.plugins.event.common;


 /**
  *  重试策略类型
  *  Class Name: RetryTypeEnum.java   
  *  @author liuhuiqing DateTime 2014-8-27 下午4:03:13    
  *  @version 1.0
  */
public enum RetryTypeEnum {
	FIXED(1,"固定时间间隔"),POWER(2,"固定值的幂"),;
	private int code;
	private String desc;
	private RetryTypeEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public int getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	
	public static RetryTypeEnum idOf(int code){
		switch (code) {
		case 1:
			return FIXED;
		case 2:
			return POWER;

		default:
			return FIXED;
		}
	}
}
