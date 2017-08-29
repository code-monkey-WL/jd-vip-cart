package com.jd.o2o.vipcart.common.service;

import com.jd.o2o.vipcart.common.domain.PageBean;

import java.util.List;

/**
 * Created by liuhuiqing on 2015/2/3.
 */
public interface BaseService<M, PK> {
    public int save(M modelEntity);

    public int saveAll(List<M> entityList);

    public int update(M modelEntry, M modelQuery);

    public int update(M modelEntry);

    public int updateAll(List<M> entityList);

    public int saveOrUpdate(M modelEntry);

    public int saveOrUpdate(List<M> entryList);

    public int delete(PK id);

    public int deleteAll(M modelEntry);

    public M get(PK id);

    public int count(M modelQuery);

    public List<M> findList(M modelQuery);

    public List<M> findList(M modelQuery, int pn, int pageSize);

    public PageBean<M> pageQuery(M modelQuery, PageBean<M> pageBean);

    public M findOne(M modelQuery);

    boolean exists(PK id);
}
