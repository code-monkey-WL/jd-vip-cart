package com.jd.o2o.vipcart.dao.busi;

import java.util.HashMap;
import java.util.List;

/**
 * sql执行器
 * Created by liuhuiqing on 2016/8/15.
 */
public interface SpyBasicDao {
    int saveOrUpdate(String spySql);
    List<String> getColumnNameByTableName(String tableName);
    List<HashMap<String,Object>> findListBySpySql(String spySql);
}
