package com.jd.o2o.vipcart.common.plugins.log;

import com.jd.o2o.vipcart.common.domain.BaseEntityBean;

/**
 * 日志数据
 * Created by liuhuiqing on 2015/8/18.
 */
public class LogBean extends BaseEntityBean {
    /**
     * 记录id
     */
    private Long id;
    /**
     * 操作流水编号
     */
    private String flowNo;
    /**
     * 业务编号:用资源编号
     */
    private String busiNo;
    /**
     * 业务名称
     */
    private String busiName;
    /**
     * 操作表名
     */
    private String tableName;
    /**
     * 操作关键字
     */
    private String operationKey;
    /**
     * 备选关键字段集合：表属性名1:值1,表属性名2:值2
     */
    private String secondaryKeys;
    /**
     * 操作类型
     */
    private Integer operationType;
    /**
     * 参数类型
     */
    private String paramTypes;
    /**
     * 参数值
     */
    private String paramValues;
    /**
     * 上次操作记录id，针对修改和删除操作
     */
    private Long pid;
    /**
     * 操作耗时，单位是毫秒
     */
    private Long costTime;
    /**
     * 追溯url
     */
    private String checkUrl;
    /**
     * 操作机器ip
     */
    private String ip;
    /**
     * 应用编号
     */
    private String appCode;
    /**
     * 备注
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getBusiNo() {
        return busiNo;
    }

    public void setBusiNo(String busiNo) {
        this.busiNo = busiNo;
    }

    public String getBusiName() {
        return busiName;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOperationKey() {
        return operationKey;
    }

    public void setOperationKey(String operationKey) {
        this.operationKey = operationKey;
    }

    public String getSecondaryKeys() {
        return secondaryKeys;
    }

    public void setSecondaryKeys(String secondaryKeys) {
        this.secondaryKeys = secondaryKeys;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public String getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(String paramTypes) {
        this.paramTypes = paramTypes;
    }

    public String getParamValues() {
        return paramValues;
    }

    public void setParamValues(String paramValues) {
        this.paramValues = paramValues;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
