package com.jd.o2o.vipcart.common.dao;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.criteria.Criteria;

import java.util.List;

/**
 * Dao层公共方法
 * Class Name: BaseDao.java
 *
 * @param <M>
 * @param <PK>
 * @author liuhuiqing DateTime 2014-7-7 下午2:28:23
 * @version 1.0
 */
public interface BaseDao<M extends Criteria, PK> {
    public static final String SQL_SELECT = "select";
    public static final String SQL_UPDATE = "update";
    public static final String SQL_DELETE = "delete";
    public static final String SQL_INSERT = "insert";
    public static final String SQL_COUNT = "count";
    public static final String SQL_BATCH = "Batch";
    public static final String SQL_ONE = "One";
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String IBATIS_PROPERYTY_PREFIX = "_";

    public int save(M modelEntity);

    public int saveAll(List<M> entityList);

    public int update(M modelEntry, M modelQuery);

    public int update(M modelEntry);

    public int updateAll(List<M> entityList);

    public int saveOrUpdate(M modelEntry);

    public int saveOrUpdate(List<M> entryList);

    public int delete(PK id);

    public int delete(M modelQuery);

    public M get(PK id);

    public int count(M modelQuery);

    public List<M> findList(M modelQuery);

    public List<M> findList(M modelQuery, int pn, int pageSize);

    public PageBean<M> pageQuery(M modelQuery, PageBean<M> pageBean);

    public M findOne(M modelQuery);

    boolean exists(PK id);

}
