package com.jd.o2o.vipcart.domain.entity;

import com.jd.o2o.vipcart.common.domain.BaseEntityBean;

public class CategoryInfoEntity extends BaseEntityBean {
	private Long id ;
	/**
	 * 类目编码
	 */
	private String categoryCode ;
	/**
	 * 类目名称
	 */
	private String categoryName ;
	/**
	 * 类目说明
	 */
	private String categoryDesc ;
	/**
	 * 目录状态 0:有效 2:无效
	 */
	private Integer categoryStatus ;
	/**
	 * 目录全路径用逗号隔开
	 */
	private Integer fullPath ;
	/**
	 * 排序
	 */
	private Integer categoryLevel ;
	/**
	 * 排序
	 */
	private Integer sort ;
	/**
	 * 创建时间
	 */
	private java.util.Date createTime ;
	/**
	 * 创建人
	 */
	private String createPin ;
	/**
	 * 更新时间
	 */
	private java.util.Date updateTime ;
	/**
	 * 更新人
	 */
	private String updatePin ;
	/**
	 * 版本号
	 */
	private Integer sysVersion ;
	/**
	 * 删除标识 0:有效 1:无效
	 */
	private Integer yn = 0 ;
	/**
	 * 时间戳
	 */
	private java.util.Date ts ;
    
	public Long getId(){
		return id;
	}
	public void setId(Long id){
		this.id = id;
	}
	public String getCategoryCode(){
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode){
		this.categoryCode = categoryCode;
	}
	public String getCategoryName(){
		return categoryName;
	}
	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}
	public String getCategoryDesc(){
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc){
		this.categoryDesc = categoryDesc;
	}
	public Integer getCategoryStatus(){
		return categoryStatus;
	}
	public void setCategoryStatus(Integer categoryStatus){
		this.categoryStatus = categoryStatus;
	}
	public Integer getFullPath(){
		return fullPath;
	}
	public void setFullPath(Integer fullPath){
		this.fullPath = fullPath;
	}
	public Integer getCategoryLevel(){
		return categoryLevel;
	}
	public void setCategoryLevel(Integer categoryLevel){
		this.categoryLevel = categoryLevel;
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