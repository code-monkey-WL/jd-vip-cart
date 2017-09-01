package com.jd.o2o.vipcart.common.plugins.spider.domain;

import com.jd.o2o.vipcart.common.domain.BaseBean;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.BaseItemRule;

import java.util.List;

/**
 * 扫描解析入参对象
 * Created by liuhuiqing on 2017/8/31.
 */
public class ScanRuleInput extends BaseBean {
    /**
     * 绝对基础路径
     */
    private String baseUrl;
    /**
     * 解析数据源
     */
    private String content;
    /**
     * 全局定位解析表达式
     */
    private String[] scanExpressions;
    /**
     * 解析项
     */
    private List<BaseItemRule> itemRuleList;

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

    public String[] getScanExpressions() {
        return scanExpressions;
    }

    public void setScanExpressions(String[] scanExpressions) {
        this.scanExpressions = scanExpressions;
    }

    public List<BaseItemRule> getItemRuleList() {
        return itemRuleList;
    }

    public void setItemRuleList(List<BaseItemRule> itemRuleList) {
        this.itemRuleList = itemRuleList;
    }
}
