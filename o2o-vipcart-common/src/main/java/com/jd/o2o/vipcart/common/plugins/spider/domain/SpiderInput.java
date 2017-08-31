package com.jd.o2o.vipcart.common.plugins.spider.domain;

import com.jd.o2o.vipcart.common.domain.BaseBean;

import java.util.List;

/**
 * 扫描解析入参对象
 * Created by liuhuiqing on 2017/8/31.
 */
public class SpiderInput<T> extends BaseBean {
    private String url;
    private ScanRuleInput<T> scanRuleInput;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ScanRuleInput<T> getScanRuleInput() {
        return scanRuleInput;
    }

    public void setScanRuleInput(ScanRuleInput<T> scanRuleInput) {
        this.scanRuleInput = scanRuleInput;
    }
}
