package com.jd.o2o.vipcart.domain.response;

import com.jd.o2o.vipcart.common.domain.api.ResponseBean;

public class SpiderConfigResponse extends ResponseBean {
	/**
	 * 自增ID
	 */
	private Long id;
	/**
	 * 爬取名称
	 */
	private String spiderName;
	/**
	 * 爬取类型 1:URL 2:content
	 */
	private Integer spiderType;
	/**
	 * 爬取url
	 */
	private String url;
	/**
	 * 请求入参
	 */
	private String requestParam;
	/**
	 * 解析内容
	 */
	private String content;
	/**
	 *  解析规则类型
	 */
	private Integer ruleEngine;
	/**
	 * 解析返回值输出:json字符串对象格式
	 */
	private String targetOut;
	/**
	 * 输出数据库表名称
	 */
	private String outTableName;
	/**
	 * 基础url
	 */
	private String baseUrl;
	/**
	 * 全局扫描表达式
	 */
	private String scanExpressions;
	/**
	 * 爬取项目表达式:json字符串对象格式
	 */
	private String itemRules;
	/**
	 * 爬取时间规则表达式
	 */
	private Integer scheduledCron;
	/**
	 * 爬取深度（分页）
	 */
	private Integer deepNum;
	/**
	 * 爬取状态 1:开启 2:运行中 3:暂停 4:废弃
	 */
	private Integer state;
	/**
	 * 爬取次数
	 */
	private Integer spiderNum;
	/**
	 * 上次爬取时间
	 */
	private java.util.Date lastSpiderTime;
	/**
	 * 备注
	 */
	private String remark;
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
	public String getSpiderName(){
		return spiderName;
	}
	public void setSpiderName(String spiderName){
		this.spiderName = spiderName;
	}
	public Integer getSpiderType(){
		return spiderType;
	}
	public void setSpiderType(Integer spiderType){
		this.spiderType = spiderType;
	}
	public String getUrl(){
		return url;
	}
	public void setUrl(String url){
		this.url = url;
	}
	public String getRequestParam(){
		return requestParam;
	}
	public void setRequestParam(String requestParam){
		this.requestParam = requestParam;
	}
	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
	public Integer getRuleEngine(){
		return ruleEngine;
	}
	public void setRuleEngine(Integer ruleEngine){
		this.ruleEngine = ruleEngine;
	}
	public String getTargetOut(){
		return targetOut;
	}
	public void setTargetOut(String targetOut){
		this.targetOut = targetOut;
	}
	public String getOutTableName(){
		return outTableName;
	}
	public void setOutTableName(String outTableName){
		this.outTableName = outTableName;
	}
	public String getBaseUrl(){
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl){
		this.baseUrl = baseUrl;
	}
	public String getScanExpressions(){
		return scanExpressions;
	}
	public void setScanExpressions(String scanExpressions){
		this.scanExpressions = scanExpressions;
	}
	public String getItemRules(){
		return itemRules;
	}
	public void setItemRules(String itemRules){
		this.itemRules = itemRules;
	}
	public Integer getScheduledCron(){
		return scheduledCron;
	}
	public void setScheduledCron(Integer scheduledCron){
		this.scheduledCron = scheduledCron;
	}
	public Integer getDeepNum(){
		return deepNum;
	}
	public void setDeepNum(Integer deepNum){
		this.deepNum = deepNum;
	}
	public Integer getState(){
		return state;
	}
	public void setState(Integer state){
		this.state = state;
	}
	public Integer getSpiderNum(){
		return spiderNum;
	}
	public void setSpiderNum(Integer spiderNum){
		this.spiderNum = spiderNum;
	}
	public java.util.Date getLastSpiderTime(){
		return lastSpiderTime;
	}
	public void setLastSpiderTime(java.util.Date lastSpiderTime){
		this.lastSpiderTime = lastSpiderTime;
	}
	public String getRemark(){
		return remark;
	}
	public void setRemark(String remark){
		this.remark = remark;
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