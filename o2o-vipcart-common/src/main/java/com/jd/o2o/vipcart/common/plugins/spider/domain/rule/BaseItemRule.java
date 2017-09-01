package com.jd.o2o.vipcart.common.plugins.spider.domain.rule;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * 基础扫描规则定义
 * Created by liuhuiqing on 2017/8/31.
 */
public class BaseItemRule extends BaseBean {
    /**
     * 解析项别名
     */
    private String aliasName;
    /**
     * 解析项表达式
     */
    private String[] itemExpressions;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String[] getItemExpressions() {
        return itemExpressions;
    }

    public void setItemExpressions(String[] itemExpressions) {
        this.itemExpressions = itemExpressions;
    }
}
