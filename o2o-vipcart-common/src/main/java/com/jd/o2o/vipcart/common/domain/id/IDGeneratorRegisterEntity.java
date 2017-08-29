package com.jd.o2o.vipcart.common.domain.id;
import com.jd.o2o.vipcart.common.domain.BaseEntityBean;

public class IDGeneratorRegisterEntity extends BaseEntityBean {
	private Long id;
	/**
	 * 业务主键标识
	 */
	private String primaryKey;
	/**
	 * 注册主键标识
	 */
	private String registerKey;
	/**
	 * 注册机标识
	 */
	private Integer workerNo;
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
	public String getPrimaryKey(){
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey){
		this.primaryKey = primaryKey;
	}
	public String getRegisterKey(){
		return registerKey;
	}
	public void setRegisterKey(String registerKey){
		this.registerKey = registerKey;
	}
	public Integer getWorkerNo(){
		return workerNo;
	}
	public void setWorkerNo(Integer workerNo){
		this.workerNo = workerNo;
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