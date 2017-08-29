package com.jd.o2o.vipcart.common.dao;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.criteria.Criteria;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 多表关联操作dao层抽象 Class Name: AbstractDao.java
 *
 * @param <M>
 * @param <PK>
 * @author liuhuiqing DateTime 2014-7-7 下午2:27:48
 * @version 1.0
 */
public abstract class AbstractDao<M extends Criteria, PK> extends SqlSessionDaoSupport implements BaseDao<M, PK> {

    public final static String UNSUPPORT_TIPS = "The method does not support this operation";
    private final String nameSpace;

    public AbstractDao() {
        TableDes myTable = (TableDes) getClass().getAnnotation(TableDes.class);
        Assert.notNull(myTable);
        String nameSpaceTmp = myTable.nameSpace();
        nameSpace = StringUtils.isEmpty(nameSpaceTmp) ? "" : nameSpaceTmp + ".";
    }

    @Override
    public int save(M modelEntity) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public int saveAll(List<M> entityList) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public int update(M modelEntry, M modelQuery) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public int updateAll(List<M> entityList) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public int update(M modelEntry) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public int saveOrUpdate(M modelEntry) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public int saveOrUpdate(List<M> entryList) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public int delete(PK id) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public int delete(M modelQuery) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public M get(PK id) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public int count(M modelQuery) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public List<M> findList(M modelQuery) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public List<M> findList(M modelQuery, int pn, int pageSize) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public PageBean<M> pageQuery(M modelQuery, PageBean<M> pageBean) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public M findOne(M modelQuery) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    @Override
    public boolean exists(PK id) {
        throw new UnsupportedOperationException(UNSUPPORT_TIPS);
    }

    protected int save(String statement, M modelEntity) {
        return getSqlSession().insert(addNameSpace(statement), modelEntity);
    }

    protected int saveOrUpdate(String insertStatement, String updateStatement, M modelEntry) {
        if (getPrimaryFieldValue(modelEntry) == null) {
            return save(insertStatement, modelEntry);
        } else {
            return update(updateStatement, modelEntry);
        }
    }

    protected int saveOrUpdate(String insertStatement, String updateStatement, List<M> entityList) {
        int effectCount = 0;
        if (CollectionUtils.isEmpty(entityList)) {
            return effectCount;
        }
        List<M> addList = new ArrayList<M>();
        List<M> updateList = new ArrayList<M>();
        for (M modelEntry : entityList) {
            if (getPrimaryFieldValue(modelEntry) == null) {
                addList.add(modelEntry);
            } else {
                updateList.add(modelEntry);
            }
        }
        if (CollectionUtils.isNotEmpty(addList)) {
            effectCount = effectCount + saveAll(insertStatement, addList);
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            effectCount = effectCount + updateAll(updateStatement, updateList);
        }
        return effectCount;
    }

    protected int count(String statement, Criteria criteria) {
        Integer result = (Integer) getSqlSession().selectOne(addNameSpace(statement), criteria);
        return result == null ? 0 : result.intValue();
    }

    protected int update(String statement, M modelEntry, M modelQuery) {
        Map<String, Object> map = BeanHelper.modelToMap(modelEntry, "", "");
        map.putAll(BeanHelper.modelToMap(modelQuery, IBATIS_PROPERYTY_PREFIX, ""));
        return getSqlSession().update(addNameSpace(statement), map);
    }

    protected int updateAll(String statement, List<M> entityList) {
        return getSqlSession().update(addNameSpace(statement), entityList);
    }

    protected int update(String statement, M modelEntry) {
        Map<String, Object> map = BeanHelper.modelToMap(modelEntry, "", "");
        return getSqlSession().update(addNameSpace(statement), map);
    }

    protected M get(String statement, PK id) {
        return (M) getSqlSession().selectOne(addNameSpace(statement), id);
    }

    protected List<M> findList(String statement, Criteria criteria) {
        return getSqlSession().selectList(addNameSpace(statement), criteria);
    }

    protected <T> List<T> findList(String statement, Map<String, Object> modelQuery) {
        return getSqlSession().selectList(addNameSpace(statement), modelQuery);
    }

    protected List<M> findList(String statement, Criteria criteria, int pn, int pageSize) {
        return findList(statement, buildCriteria(criteria, pn, pageSize));
    }

    protected <T> List<T> findList(String statement, Object entity) {
        return getSqlSession().selectList(addNameSpace(statement), entity);
    }

    protected boolean exists(String statement, PK id) {
        return get(statement, id) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    protected int saveAll(String statement, List<M> list) {
        return getSqlSession().insert(addNameSpace(statement), list);
    }

    protected int delete(String statement, Object modelQuery) {
        return getSqlSession().delete(addNameSpace(statement), modelQuery);
    }

    /**
     * 获得一条实体记录 注意：查询条件对应结果是多条记录会出错
     *
     * @param statement
     * @param modelQuery
     * @param <T>
     * @return
     */
    protected <T> T findOne(String statement, T modelQuery) {
        return (T) getSqlSession().selectOne(addNameSpace(statement), modelQuery);
    }

    /**
     * 分页查询函数
     *
     * @param queryCountSql 查询记录总条数的sql
     * @param queryListSql  查询当页记录的sql
     * @param modelQuery    参数
     * @param pageBean      分页参数
     * @return
     */
    protected <T> PageBean<T> pageQuery(String queryCountSql, String queryListSql, Criteria modelQuery, PageBean<T> pageBean) {
        Assert.notNull(pageBean, "分页条件不能为空.");
        pageBean.setTotalCount(count(queryCountSql, modelQuery));
        pageBean.setResultList((List<T>) findList(queryListSql, buildCriteria(modelQuery, (int) pageBean.getPageNo(), pageBean.getPageSize())));
        return pageBean;
    }

    /**
     * 添加命名空间
     *
     * @param statement
     * @return
     */
    private String addNameSpace(String statement) {
        return new StringBuffer().append(nameSpace).append(statement).toString();
    }

    /**
     * 构建通用查询属性
     *
     * @param criteria
     * @param pn
     * @param pageSize
     * @return
     */
    private Criteria buildCriteria(Criteria criteria, int pn, int pageSize) {
        Assert.notNull(criteria, "查询条件不能为空.");
        int skipResults = pn > 1 ? (pn - 1) * pageSize : 0;
        int maxResults = pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
        criteria.addExtField("start", skipResults);
        criteria.addExtField("limit", maxResults);
        return criteria;
    }

    /**
     * 获得主键ID属性值
     *
     * @param modelEntry
     * @return
     */
    private Object getPrimaryFieldValue(M modelEntry) {
        Class clazz = modelEntry.getClass();
        Field field = findPrimaryField(clazz);
        if (field == null) {
            throw new RuntimeException("方法不支持该实体对象的[保存或更新]操作");
        }
        field.setAccessible(true);
        Object id;
        try {
            id = field.get(modelEntry);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * 获取主键ID属性域
     *
     * @param clazz
     * @return
     */
    private Field findPrimaryField(Class clazz) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fs = clazz.getDeclaredFields();
            for (Field field : fs) {
                if ("ID".equals(field.getName().toUpperCase())) {
                    return field;
                }
            }
        }
        return null;
    }
}
