package com.jd.o2o.vipcart.domain.response;
import com.jd.o2o.vipcart.common.domain.api.ResponseBean;

public class DictEntryResponse extends ResponseBean {
	/**
	 * 自增ID
	 */
	private Long id;
	/**
	 * 字典类型编号
	 */
	private Long dictTypeId;
	/**
	 * 字典编号
	 */
	private String dictCode;
	private String dictName;
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
	public Long getDictTypeId(){
		return dictTypeId;
	}
	public void setDictTypeId(Long dictTypeId){
		this.dictTypeId = dictTypeId;
	}
	public String getDictCode(){
		return dictCode;
	}
	public void setDictCode(String dictCode){
		this.dictCode = dictCode;
	}
	public String getDictName(){
		return dictName;
	}
	public void setDictName(String dictName){
		this.dictName = dictName;
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