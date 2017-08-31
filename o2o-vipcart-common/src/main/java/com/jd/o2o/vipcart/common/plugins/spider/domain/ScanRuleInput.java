package com.jd.o2o.vipcart.common.plugins.spider.domain;

import com.jd.o2o.vipcart.common.domain.BaseBean;

import java.util.List;

/**
 * 扫描解析入参对象
 * Created by liuhuiqing on 2017/8/31.
 */
public class ScanRuleInput<T> extends BaseBean {
    /**
     * 绝对基础路径
     */
    private String baseUrl;
    /**
     * 解析数据源
     */
    private String content;
    /**
     * 解析规则类型
     */
    private Integer scanRuleType;
    /**
     * 全局定位解析表达式
     */
    private String[] scanExpressions;
    /**
     * 解析项
     */
    private List<ScanItemRule> scanItemRuleList;
    /**
     * @see com.jd.o2o.vipcart.common.plugins.spider.domain.ScanItemRule
     * 解析成目标对象类型（对象属性名称即解析项别名（aliasName）一一对应）
     */
    private Class<T> targetClass;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getScanRuleType() {
        return scanRuleType;
    }

    public void setScanRuleType(Integer scanRuleType) {
        this.scanRuleType = scanRuleType;
    }

    public String[] getScanExpressions() {
        return scanExpressions;
    }

    public void setScanExpressions(String[] scanExpressions) {
        this.scanExpressions = scanExpressions;
    }

    public List<ScanItemRule> getScanItemRuleList() {
        return scanItemRuleList;
    }

    public void setScanItemRuleList(List<ScanItemRule> scanItemRuleList) {
        this.scanItemRuleList = scanItemRuleList;
    }

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<T> targetClass) {
        this.targetClass = targetClass;
    }
}
