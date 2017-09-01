package com.jd.o2o.vipcart.common.plugins.spider.domain.rule;

/**
 * 通用扫描规则定义
 * Created by liuhuiqing on 2017/8/31.
 */
public class ItemRule extends BaseItemRule {
    /**
     * 取值来源
     */
    private Integer itemSource;

    public Integer getItemSource() {
        return itemSource;
    }

    public void setItemSource(Integer itemSource) {
        this.itemSource = itemSource;
    }
}
