package com.jd.o2o.vipcart.common.plugins.spider.domain;

import com.jd.o2o.vipcart.common.domain.BaseBean;
import com.jd.o2o.vipcart.common.plugins.spider.domain.constant.RuleEngineEnum;

/**
 * 扫描解析入参对象
 * Created by liuhuiqing on 2017/8/31.
 */
public class SpiderInput<T> extends BaseBean {
    /**
     * 抓取页面url
     */
    private String url;
    /**
     * 解析规则类型
     */
    private Integer ruleEngine = RuleEngineEnum.JSOUP.getCode();
    /**
     * 解析规则
     */
    private ScanRuleInput scanRuleInput;
    /**
     * 返回结果对象类型
     */
    private Class<T> targetClass;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRuleEngine() {
        return ruleEngine;
    }

    public void setRuleEngine(Integer ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    public ScanRuleInput getScanRuleInput() {
        return scanRuleInput;
    }

    public void setScanRuleInput(ScanRuleInput scanRuleInput) {
        this.scanRuleInput = scanRuleInput;
    }

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<T> targetClass) {
        this.targetClass = targetClass;
    }
}
