package com.jd.o2o.vipcart.common.service;

import com.jd.o2o.vipcart.common.dao.BaseDao;
import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.criteria.Criteria;

import java.util.List;

/**
 * 基础服务类抽象
 * Created by liuhuiqing on 2015/2/3.
 */
public abstract class AbstractBaseService<M extends Criteria, PK> implements BaseService<M, PK> {

    protected abstract BaseDao<M, PK> getBaseDao();

    @Override
    public int save(M modelEntity) {
        return getBaseDao().save(modelEntity);
    }

    @Override
    public int saveAll(List<M> entityList) {
        return getBaseDao().saveAll(entityList);
    }

    @Override
    public int update(M modelEntry, M modelQuery) {
        return getBaseDao().update(modelEntry, modelQuery);
    }

    @Override
    public int update(M modelEntry) {
        return getBaseDao().update(modelEntry);
    }

    @Override
    public int updateAll(List<M> entityList) {
        return getBaseDao().updateAll(entityList);
    }

    @Override
    public int saveOrUpdate(M modelEntry) {
        return getBaseDao().saveOrUpdate(modelEntry);
    }

    @Override
    public int saveOrUpdate(List<M> entryList) {
        return getBaseDao().saveOrUpdate(entryList);
    }

    @Override
    public int delete(PK id) {
        return getBaseDao().delete(id);
    }

    @Override
    public int deleteAll(M modelEntry) {
        return getBaseDao().delete(modelEntry);
    }

    @Override
    public M get(PK id) {
        return getBaseDao().get(id);
    }

    @Override
    public int count(M modelQuery) {
        return getBaseDao().count(modelQuery);
    }

    @Override
    public List<M> findList(M modelQuery) {
        return getBaseDao().findList(modelQuery);
    }

    @Override
    public List<M> findList(M modelQuery, int pn, int pageSize) {
        return getBaseDao().findList(modelQuery, pn, pageSize);
    }

    @Override
    public PageBean<M> pageQuery(M modelQuery, PageBean<M> pageBean) {
        return getBaseDao().pageQuery(modelQuery, pageBean);
    }

    @Override
    public M findOne(M modelQuery) {
        return getBaseDao().findOne(modelQuery);
    }

    @Override
    public boolean exists(PK id) {
        return getBaseDao().exists(id);
    }
}
