package com.jd.o2o.vipcart.domain.response;

import com.jd.o2o.vipcart.common.domain.api.ResponseBean;

public class PromotionInfoResponse extends ResponseBean {
	private Long id;
	/**
	 * 促销编码
	 */
	private String promotionCode;
	/**
	 * 促销类型
	 */
	private Integer promotionType;
	/**
	 * 促销商品编号
	 */
	private String skuId;
	/**
	 * 促销说明
	 */
	private String promotionDesc;
	/**
	 * 促销图标
	 */
	private String promotionImg;
	/**
	 * 促销开始时间
	 */
	private java.util.Date startTime;
	/**
	 * 促销结束时间
	 */
	private java.util.Date endTime;
	/**
	 * 促销状态 0:有效 2:无效
	 */
	private Integer promotionStatus;
	/**
	 * 商品来源 1:jd 2:tmall
	 */
	private Integer fromSource;
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
	public String getPromotionCode(){
		return promotionCode;
	}
	public void setPromotionCode(String promotionCode){
		this.promotionCode = promotionCode;
	}
	public Integer getPromotionType(){
		return promotionType;
	}
	public void setPromotionType(Integer promotionType){
		this.promotionType = promotionType;
	}
	public String getSkuId(){
		return skuId;
	}
	public void setSkuId(String skuId){
		this.skuId = skuId;
	}
	public String getPromotionDesc(){
		return promotionDesc;
	}
	public void setPromotionDesc(String promotionDesc){
		this.promotionDesc = promotionDesc;
	}
	public String getPromotionImg(){
		return promotionImg;
	}
	public void setPromotionImg(String promotionImg){
		this.promotionImg = promotionImg;
	}
	public java.util.Date getStartTime(){
		return startTime;
	}
	public void setStartTime(java.util.Date startTime){
		this.startTime = startTime;
	}
	public java.util.Date getEndTime(){
		return endTime;
	}
	public void setEndTime(java.util.Date endTime){
		this.endTime = endTime;
	}
	public Integer getPromotionStatus(){
		return promotionStatus;
	}
	public void setPromotionStatus(Integer promotionStatus){
		this.promotionStatus = promotionStatus;
	}
	public Integer getFromSource(){
		return fromSource;
	}
	public void setFromSource(Integer fromSource){
		this.fromSource = fromSource;
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