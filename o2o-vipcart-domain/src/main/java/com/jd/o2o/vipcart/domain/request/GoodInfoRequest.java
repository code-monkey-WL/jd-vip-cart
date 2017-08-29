package com.jd.o2o.vipcart.domain.request;
import com.jd.o2o.vipcart.common.domain.api.RequestBean;

public class GoodInfoRequest extends RequestBean {
	private Long id;

	/**
	 * 商品编号
	 */
	private String skuId;

	/**
	 * 第三方商品编码
	 */
	private String outSkuCode;

	/**
	 * 商品编码，多个来源对应一个编码
	 */
	private String skuCode;

	/**
	 * 商品名称
	 */
	private String skuName;

	/**
	 * 商品说明
	 */
	private String skuDesc;

	/**
	 * 商品摘要
	 */
	private String skuSummary;

	/**
	 * 价格
	 */
	private Long skuPrice;

	/**
	 * 原价格
	 */
	private Long originPrice;

	/**
	 * 促销摘要
	 */
	private String promotionSummary;

	/**
	 * 商品原链接
	 */
	private String skuLink;

	/**
	 * 商品图片
	 */
	private String skuImg;

	/**
	 * 商品来源 1:jd 2:tmall
	 */
	private Integer fromSource;

	/**
	 * 类目编码
	 */
	private String categoryCode;

	/**
	 * 全路径类目编码逗号隔开
	 */
	private String fullCategoryCode;

	/**
	 * 品牌编号
	 */
	private String brandCode;

	/**
	 * 商家编号
	 */
	private String orgCode;

	/**
	 * 库存标记类型 0:数量 1:百分比
	 */
	private Integer stockType;

	/**
	 * 库存量
	 */
	private Integer stockNum;

	/**
	 * 评论数量
	 */
	private Integer commentNum;

	/**
	 * 抓取时间
	 */
	private java.util.Date grabTime;

	/**
	 * 商品状态 0:上架 2:下架
	 */
	private Integer skuStatus;

	/**
	 * 扩展字段
	 */
	private String ext;

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
	public String getSkuId(){
		return skuId;
	}
	public void setSkuId(String skuId){
		this.skuId = skuId;
	}
	public String getOutSkuCode(){
		return outSkuCode;
	}
	public void setOutSkuCode(String outSkuCode){
		this.outSkuCode = outSkuCode;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public void setSkuCode(String skuCode){
		this.skuCode = skuCode;
	}
	public String getSkuName(){
		return skuName;
	}
	public void setSkuName(String skuName){
		this.skuName = skuName;
	}
	public String getSkuDesc(){
		return skuDesc;
	}
	public void setSkuDesc(String skuDesc){
		this.skuDesc = skuDesc;
	}
	public String getSkuSummary(){
		return skuSummary;
	}
	public void setSkuSummary(String skuSummary){
		this.skuSummary = skuSummary;
	}
	public Long getSkuPrice(){
		return skuPrice;
	}
	public void setSkuPrice(Long skuPrice){
		this.skuPrice = skuPrice;
	}
	public Long getOriginPrice(){
		return originPrice;
	}
	public void setOriginPrice(Long originPrice){
		this.originPrice = originPrice;
	}
	public String getPromotionSummary(){
		return promotionSummary;
	}
	public void setPromotionSummary(String promotionSummary){
		this.promotionSummary = promotionSummary;
	}
	public String getSkuLink(){
		return skuLink;
	}
	public void setSkuLink(String skuLink){
		this.skuLink = skuLink;
	}
	public String getSkuImg(){
		return skuImg;
	}
	public void setSkuImg(String skuImg){
		this.skuImg = skuImg;
	}
	public Integer getFromSource(){
		return fromSource;
	}
	public void setFromSource(Integer fromSource){
		this.fromSource = fromSource;
	}
	public String getCategoryCode(){
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode){
		this.categoryCode = categoryCode;
	}
	public String getFullCategoryCode(){
		return fullCategoryCode;
	}
	public void setFullCategoryCode(String fullCategoryCode){
		this.fullCategoryCode = fullCategoryCode;
	}
	public String getBrandCode(){
		return brandCode;
	}
	public void setBrandCode(String brandCode){
		this.brandCode = brandCode;
	}
	public String getOrgCode(){
		return orgCode;
	}
	public void setOrgCode(String orgCode){
		this.orgCode = orgCode;
	}
	public Integer getStockType(){
		return stockType;
	}
	public void setStockType(Integer stockType){
		this.stockType = stockType;
	}
	public Integer getStockNum(){
		return stockNum;
	}
	public void setStockNum(Integer stockNum){
		this.stockNum = stockNum;
	}
	public Integer getCommentNum(){
		return commentNum;
	}
	public void setCommentNum(Integer commentNum){
		this.commentNum = commentNum;
	}
	public java.util.Date getGrabTime(){
		return grabTime;
	}
	public void setGrabTime(java.util.Date grabTime){
		this.grabTime = grabTime;
	}
	public Integer getSkuStatus(){
		return skuStatus;
	}
	public void setSkuStatus(Integer skuStatus){
		this.skuStatus = skuStatus;
	}
	public String getExt(){
		return ext;
	}
	public void setExt(String ext){
		this.ext = ext;
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