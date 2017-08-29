package com.jd.o2o.vipcart.worker.domain;

import com.jd.o2o.vipcart.common.domain.BaseBean;

import java.util.List;

/**
 * 迁移历史表用
 * Created by liuhuiqing on 2017/6/16.
 */
public class SqlQueryEntity extends BaseBean {
    private String tableName;
    private String sql;
    private String columnNames;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }
}
