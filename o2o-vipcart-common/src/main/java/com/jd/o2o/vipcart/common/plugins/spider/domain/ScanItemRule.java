package com.jd.o2o.vipcart.common.plugins.spider.domain;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * 扫描规则定义
 * Created by liuhuiqing on 2017/8/31.
 */
public class ScanItemRule extends BaseBean {
    /**
     * 解析项名称(只有取元素属性时赋值)
     */
    private String itemName;
    /**
     * 解析项别名
     */
    private String aliasName;
    /**
     * 取值来源
     */
    private Integer itemSource = ItemSourceEnum.ATTR.getCode();
    /**
     * 解析项表达式
     */
    private String[] itemExpressions;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Integer getItemSource() {
        return itemSource;
    }

    public void setItemSource(Integer itemSource) {
        this.itemSource = itemSource;
    }

    public String[] getItemExpressions() {
        return itemExpressions;
    }

    public void setItemExpressions(String[] itemExpressions) {
        this.itemExpressions = itemExpressions;
    }
}
