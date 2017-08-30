package com.jd.o2o.vipcart.domain.request;

import com.jd.o2o.vipcart.common.domain.api.RequestBean;

public class BrandInfoRequest extends RequestBean {
	private Long id;

	/**
	 * 品牌编码
	 */
	private String brandCode;

	/**
	 * 品牌名称
	 */
	private String brandName;

	/**
	 * 品牌说明
	 */
	private String brandDesc;

	/**
	 * 品牌图标
	 */
	private String brandImg;

	/**
	 * 品牌状态 0:有效 2:无效
	 */
	private Integer brandStatus;

	/**
	 * 品牌级别
	 */
	private Integer brandLevel;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 创建时间
	 */
	private java.util.Date createTime;

	/**
	 * 创建人
	 */
	private String createPin;

	/**
	 * 更新时间
	 */
	private java.util.Date updateTime;

	/**
	 * 更新人
	 */
	private String updatePin;

	/**
	 * 版本号
	 */
	private Integer sysVersion;

	/**
	 * 删除标识 0:有效 1:无效
	 */
	private Integer yn;

	/**
	 * 时间戳
	 */
	private java.util.Date ts;

    
	public Long getId(){
		return id;
	}
	public void setId(Long id){
		this.id = id;
	}
	public String getBrandCode(){
		return brandCode;
	}
	public void setBrandCode(String brandCode){
		this.brandCode = brandCode;
	}
	public String getBrandName(){
		return brandName;
	}
	public void setBrandName(String brandName){
		this.brandName = brandName;
	}
	public String getBrandDesc(){
		return brandDesc;
	}
	public void setBrandDesc(String brandDesc){
		this.brandDesc = brandDesc;
	}
	public String getBrandImg(){
		return brandImg;
	}
	public void setBrandImg(String brandImg){
		this.brandImg = brandImg;
	}
	public Integer getBrandStatus(){
		return brandStatus;
	}
	public void setBrandStatus(Integer brandStatus){
		this.brandStatus = brandStatus;
	}
	public Integer getBrandLevel(){
		return brandLevel;
	}
	public void setBrandLevel(Integer brandLevel){
		this.brandLevel = brandLevel;
	}
	public Integer getSort(){
		return sort;
	}
	public void setSort(Integer sort){
		this.sort = sort;
	}
	public java.util.Date getCreateTime(){
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	public String getCreatePin(){
		return createPin;
	}
	public void setCreatePin(String createPin){
		this.createPin = createPin;
	}
	public java.util.Date getUpdateTime(){
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	public String getUpdatePin(){
		return updatePin;
	}
	public void setUpdatePin(String updatePin){
		this.updatePin = updatePin;
	}
	public Integer getSysVersion(){
		return sysVersion;
	}
	public void setSysVersion(Integer sysVersion){
		this.sysVersion = sysVersion;
	}
	public Integer getYn(){
		return yn;
	}
	public void setYn(Integer yn){
		this.yn = yn;
	}
	public java.util.Date getTs(){
		return ts;
	}
	public void setTs(java.util.Date ts){
		this.ts = ts;
	}
}