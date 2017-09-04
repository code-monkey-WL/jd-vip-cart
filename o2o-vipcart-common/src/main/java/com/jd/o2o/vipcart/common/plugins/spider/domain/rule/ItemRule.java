package com.jd.o2o.vipcart.common.plugins.spider.domain.rule;

import com.jd.o2o.vipcart.common.domain.BaseBean;
import com.jd.o2o.vipcart.common.plugins.spider.domain.constant.ItemSourceEnum;

/**
 * 通用扫描规则定义
 * Created by liuhuiqing on 2017/8/31.
 */
public class ItemRule extends BaseBean {
    /**
     * 解析项别名
     */
    private String aliasName;
    /**
     * 解析项表达式
     */
    private String[] itemExpressions;
    /**
     * 取值来源
     * @see com.jd.o2o.vipcart.common.plugins.spider.domain.constant.ItemSourceEnum
     */
    private Integer itemSource = ItemSourceEnum.TEXT.getCode();
    /**
     * 取元素属性：当itemSource == ItemSourceEnum.ATTR时,有效
     */
    private String attrName;

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

    public Integer getItemSource() {
        return itemSource;
    }

    public void setItemSource(Integer itemSource) {
        this.itemSource = itemSource;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
        itemSource = ItemSourceEnum.ATTR.getCode();
    }
}
