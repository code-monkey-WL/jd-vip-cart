package com.jd.o2o.vipcart.dao.busi.impl;

import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.dao.busi.SpyBasicDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * sql执行器
 * Created by liuhuiqing on 2016/8/15.
 */
@TableDes(nameSpace = "SpyBasicMapper", tableName = "")
@Repository
public class SpyBasicDaoImpl extends AbstractBaseDao implements SpyBasicDao {
    @Override
    public int saveOrUpdate(String spySql) {
        return super.getSqlSession().update("saveOrUpdateSpyBasic", spySql);
    }

    @Override
    public List<String> getColumnNameByTableName(String tableName) {
        return super.getSqlSession().selectList("getColumnNameByTableName",tableName);
    }

    @Override
    public List<HashMap<String, Object>> findListBySpySql(String spySql) {
        return super.getSqlSession().selectList("findListBySpySql",spySql);
    }
}
