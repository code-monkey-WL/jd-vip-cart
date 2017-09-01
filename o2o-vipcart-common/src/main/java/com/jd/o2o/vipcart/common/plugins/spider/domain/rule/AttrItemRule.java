package com.jd.o2o.vipcart.common.plugins.spider.domain.rule;

/**
 * 属性扫描规则定义
 * Created by liuhuiqing on 2017/8/31.
 */
public class AttrItemRule extends BaseItemRule {
    /**
     * 取元素属性
     */
    private String attrName;

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }
}
