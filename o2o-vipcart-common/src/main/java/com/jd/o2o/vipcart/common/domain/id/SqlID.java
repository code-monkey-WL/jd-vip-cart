package com.jd.o2o.vipcart.common.domain.id;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * 业务编号
 * Created by liuhuiqing on 2016/11/9.
 */
public class SqlID extends BaseBean {
    private Long id;
    private String tableName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
