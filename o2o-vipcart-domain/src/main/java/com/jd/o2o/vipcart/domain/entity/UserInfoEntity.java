package com.jd.o2o.vipcart.domain.entity;

import com.jd.o2o.vipcart.common.domain.BaseEntityBean;

public class UserInfoEntity extends BaseEntityBean {
	/**
	 * 自增ID
	 */
	private Long id ;
	/**
	 * 用户ID
	 */
	private Long userId ;
	/**
	 * 登录名
	 */
	private String loginName ;
	/**
	 * 登录凭证
	 */
	private String loginPassword ;
	/**
	 * 登录加盐编码
	 */
	private Integer saltCode ;
	/**
	 * 中文名
	 */
	private String cn ;
	/**
	 * 联系电话
	 */
	private String phone ;
	/**
	 * 邮箱
	 */
	private String email ;
	/**
	 * 微信id
	 */
	private String weiXinId ;
	/**
	 * 状态，0-启用、1-禁用
	 */
	private Integer status ;
	/**
	 * 上次登录时间
	 */
	private java.util.Date lastLoginTime ;
	/**
	 * 连续登录失败次数
	 */
	private Integer loginFailCount ;
	/**
	 * 备注
	 */
	private String remark ;
	/**
	 * 注册来源
	 */
	private String registerSource ;
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
	 * 删除标识
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
	public Long getUserId(){
		return userId;
	}
	public void setUserId(Long userId){
		this.userId = userId;
	}
	public String getLoginName(){
		return loginName;
	}
	public void setLoginName(String loginName){
		this.loginName = loginName;
	}
	public String getLoginPassword(){
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword){
		this.loginPassword = loginPassword;
	}
	public Integer getSaltCode(){
		return saltCode;
	}
	public void setSaltCode(Integer saltCode){
		this.saltCode = saltCode;
	}
	public String getCn(){
		return cn;
	}
	public void setCn(String cn){
		this.cn = cn;
	}
	public String getPhone(){
		return phone;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public String getWeiXinId(){
		return weiXinId;
	}
	public void setWeiXinId(String weiXinId){
		this.weiXinId = weiXinId;
	}
	public Integer getStatus(){
		return status;
	}
	public void setStatus(Integer status){
		this.status = status;
	}
	public java.util.Date getLastLoginTime(){
		return lastLoginTime;
	}
	public void setLastLoginTime(java.util.Date lastLoginTime){
		this.lastLoginTime = lastLoginTime;
	}
	public Integer getLoginFailCount(){
		return loginFailCount;
	}
	public void setLoginFailCount(Integer loginFailCount){
		this.loginFailCount = loginFailCount;
	}
	public String getRemark(){
		return remark;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}
	public String getRegisterSource(){
		return registerSource;
	}
	public void setRegisterSource(String registerSource){
		this.registerSource = registerSource;
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