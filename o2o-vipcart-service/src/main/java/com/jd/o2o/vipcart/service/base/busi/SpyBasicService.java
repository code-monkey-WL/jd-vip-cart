package com.jd.o2o.vipcart.service.base.busi;

import java.util.HashMap;
import java.util.List;

/**
 * sql执行器
 * Created by liuhuiqing on 2016/8/15.
 */
public interface SpyBasicService {
    int saveOrUpdate(String spySql);
    List<String> getColumnNameByTableName(String tableName);
    List<HashMap<String,Object>> findListBySpySql(String spySql);
    public int moveToHistory(String insertSql,String deleteSql);
}
