package com.jd.o2o.vipcart.service.base.busi.impl;

import com.jd.o2o.vipcart.dao.busi.SpyBasicDao;
import com.jd.o2o.vipcart.service.base.busi.SpyBasicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * sql执行器
 * Created by liuhuiqing on 2017/6/15.
 */
@Service
public class SpyBasicServiceImpl implements SpyBasicService {
    @Resource
    private SpyBasicDao spyBasicDaoImpl;

    @Override
    public int saveOrUpdate(String spySql) {
        return spyBasicDaoImpl.saveOrUpdate(spySql);
    }

    @Override
    public List<String> getColumnNameByTableName(String tableName) {
        return spyBasicDaoImpl.getColumnNameByTableName(tableName);
    }

    @Override
    public List<HashMap<String, Object>> findListBySpySql(String spySql) {
        return spyBasicDaoImpl.findListBySpySql(spySql);
    }

    @Override
    @Transactional
    public int moveToHistory(String insertSql, String deleteSql) {
        int r = spyBasicDaoImpl.saveOrUpdate(insertSql);
        if(r > 0){
            spyBasicDaoImpl.saveOrUpdate(deleteSql);
        }
        return r;
    }
}
