package com.jd.o2o.vipcart.common.dao;

import com.jd.o2o.vipcart.common.dao.router.HashRouter;
import com.jd.o2o.vipcart.common.dao.router.Router;
import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.criteria.Criteria;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共dao层抽象 Class Name: AbstractBaseDao.java
 *
 * @param <M>
 * @param <PK>
 * @author liuhuiqing DateTime 2014-7-7 下午2:27:48
 * @version 1.0
 */
public abstract class AbstractBaseDao<M extends Criteria, PK> extends SqlSessionDaoSupport implements BaseDao<M, PK> {
    private final static String ID = "id";
    private final static String TABLE_INDEX = "tableIndex";
    private final String sql_select;
    private final String sql_select_one;
    private final String sql_count;
    private final String sql_insert;
    private final String sql_insert_batch;
    private final String sql_update;
    private final String sql_update_batch;
    private final String sql_delete;
    private final String sql_delete_one;
    private final String nameSpace;
    private final String tableName;
    private final String routerKey;
    private final Router router;
    private Map<String, Field> entityFieldMap = new HashMap<String, Field>();

    public AbstractBaseDao() {
        TableDes myTable = (TableDes) getClass().getAnnotation(TableDes.class);
        Assert.notNull(myTable);
        tableName = myTable.tableName();
        Assert.notNull(tableName);
        String nameSpaceTmp = myTable.nameSpace();
        routerKey = myTable.routerKey();
        if (StringUtils.isEmpty(routerKey)) {
            router = null;
        } else {
            router = new HashRouter(myTable.shards());
        }
        nameSpace = StringUtils.isEmpty(nameSpaceTmp) ? "" : nameSpaceTmp + ".";
        sql_select = new StringBuffer().append(SQL_SELECT).append(tableName).toString();
        sql_select_one = new StringBuffer().append(SQL_SELECT).append(SQL_ONE).append(tableName).toString();
        sql_insert = new StringBuffer().append(SQL_INSERT).append(tableName).toString();
        sql_insert_batch = new StringBuffer().append(SQL_INSERT).append(tableName).append(SQL_BATCH).toString();
        sql_update = new StringBuffer().append(SQL_UPDATE).append(tableName).toString();
        sql_update_batch = new StringBuffer().append(SQL_UPDATE).append(tableName).append(SQL_BATCH).toString();
        sql_delete = new StringBuffer().append(SQL_DELETE).append(tableName).toString();
        sql_delete_one = new StringBuffer().append(SQL_DELETE).append(SQL_ONE).append(tableName).toString();
        sql_count = new StringBuffer().append(SQL_COUNT).append(tableName).toString();
    }

    @Override
    public int save(M modelEntity) {
        return save(sql_insert, modelEntity);
    }

    @Override
    public int saveAll(List<M> entityList) {
        return saveAll(sql_insert_batch, entityList);
    }

    @Override
    public int update(M modelEntry, M modelQuery) {
        return update(sql_update, modelEntry, modelQuery);
    }

    @Override
    public int updateAll(List<M> entityList) {
        return updateAll(sql_update_batch, entityList);
    }

    @Override
    public int update(M modelEntry) {
        return update(sql_update, modelEntry);
    }

    @Override
    public int saveOrUpdate(M modelEntry) {
        return saveOrUpdate(sql_insert, sql_update, modelEntry);
    }

    @Override
    public int saveOrUpdate(List<M> entryList) {
        return saveOrUpdate(sql_insert, sql_update, entryList);
    }

    @Override
    public int delete(PK id) {
        return delete(sql_delete_one, id);
    }

    @Override
    public int delete(M modelQuery) {
        return delete(sql_delete, modelQuery);
    }

    @Override
    public M get(PK id) {
        return get(sql_select_one, id);
    }

    @Override
    public int count(M modelQuery) {
        return count(sql_count, modelQuery);
    }

    @Override
    public List<M> findList(M modelQuery) {
        return findList(sql_select, modelQuery);
    }

    @Override
    public List<M> findList(M modelQuery, int pn, int pageSize) {
        return findList(sql_select, modelQuery, pn, pageSize);
    }

    @Override
    public PageBean<M> pageQuery(M modelQuery, PageBean<M> pageBean) {
        Assert.notNull(modelQuery, "查询条件不能为空.");
        Assert.notNull(pageBean, "分页条件不能为空.");
        int count = count(sql_count, modelQuery);
        pageBean.setTotalCount(count);
        if (count == 0) {
            return pageBean;
        }
        pageBean.setResultList(findList(sql_select, modelQuery, (int) pageBean.getPageNo(), pageBean.getPageSize()));
        return pageBean;
    }

    @Override
    public M findOne(M modelQuery) {
        return findOne(sql_select, modelQuery);
    }

    @Override
    public boolean exists(PK id) {
        return exists(sql_select, id);
    }

    protected int save(String statement, M modelEntity) {
        buildRouterSupport(modelEntity);
        return getSqlSession().insert(addNameSpace(statement), modelEntity);
    }

    protected int saveOrUpdate(String insertStatement, String updateStatement, M modelEntity) {
        if (getFieldValueByCondition(modelEntity, ID) == null) {
            return save(insertStatement, modelEntity);
        } else {
            return update(updateStatement, modelEntity);
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
            if (getFieldValueByCondition(modelEntry, ID) == null) {
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
        buildRouterSupport(criteria);
        Integer result = (Integer) getSqlSession().selectOne(addNameSpace(statement), criteria);
        return result == null ? 0 : result.intValue();
    }

    protected int count(String statement, Map<String, Object> map) {
        buildRouterSupport(map);
        Integer result = (Integer) getSqlSession().selectOne(addNameSpace(statement), map);
        return result == null ? 0 : result.intValue();
    }

    protected int update(String statement, M modelEntry, M modelQuery) {
        Map<String, Object> map = BeanHelper.modelToMap(modelEntry, "", "");
        map.putAll(BeanHelper.modelToMap(modelQuery, IBATIS_PROPERYTY_PREFIX, ""));
        buildRouterSupport(map);
        return getSqlSession().update(addNameSpace(statement), map);
    }

    protected int updateAll(String statement, List<M> entityList) {
        buildRouterSupport(entityList);
        return getSqlSession().update(addNameSpace(statement), entityList);
    }

    protected int update(String statement, M modelEntry) {
        Map<String, Object> map = BeanHelper.modelToMap(modelEntry, "", "");
        buildRouterSupport(map);
        return updateByCondition(statement, map);
    }

    protected int updateByCondition(String statement, Map map) {
        buildRouterSupport(map);
        return getSqlSession().update(addNameSpace(statement), map);
    }

    protected M get(String statement, PK id) {
        buildRouterSupport(id);
        return (M) getSqlSession().selectOne(addNameSpace(statement), id);
    }

    protected List<M> findList(String statement, Criteria criteria) {
        buildRouterSupport(criteria);
        return getSqlSession().selectList(addNameSpace(statement), criteria);
    }

    protected <T> List<T> findList(String statement, Map<String, Object> modelQuery) {
        buildRouterSupport(modelQuery);
        return getSqlSession().selectList(addNameSpace(statement), modelQuery);
    }

    protected List<M> findList(String statement, Criteria criteria, int pn, int pageSize) {
        return findList(statement, buildCriteria(criteria, pn, pageSize));
    }

    protected <T> List<T> findList(String statement, Object entity) {
        buildRouterSupport(entity);
        return getSqlSession().selectList(addNameSpace(statement), entity);
    }

    protected boolean exists(String statement, PK id) {
        buildRouterSupport(id);
        return get(statement, id) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    protected int saveAll(String statement, List<M> list) {
        int r = 0;
        if (CollectionUtils.isEmpty(list)) {
            return r;
        }
        if(router == null){
            return getSqlSession().insert(addNameSpace(statement), list);
        }
        // 需要分表路由的降级为单挑插入
        for (M entity : list) {
            r = r + save(entity);
        }
        return r;
    }

    protected int delete(String statement, Object modelQuery) {
        buildRouterSupport(modelQuery);
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
        buildRouterSupport(modelQuery);
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
     * 获得对象的指定属性值
     *
     * @param criteria
     * @param fieldName
     * @return
     */
    private Object getFieldValueByCondition(Criteria criteria, String fieldName) {
        Class clazz = criteria.getClass();
        if (entityFieldMap.isEmpty()) {
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                Field[] fs = clazz.getDeclaredFields();
                for (Field field : fs) {
                    String fName = field.getName();
                    if (ID.equals(fName.toLowerCase())) {
                        fName = ID;
                    }
                    entityFieldMap.put(fName, field);
                }
            }
        }
        Field field = entityFieldMap.get(fieldName);
        if (field == null) {
            throw new RuntimeException(String.format("没有找到对象类型[%s]对应的属性[%s]", clazz.getName(), fieldName));
        }
        boolean isAccessible = field.isAccessible();
        if (!isAccessible) {
            field.setAccessible(true);
        }
        Object value;
        try {
            value = field.get(criteria);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (!isAccessible) {
                field.setAccessible(isAccessible);
            }
        }
        return value;
    }

    /**
     * 校验/设置路由信息
     *
     * @param objects
     */
    private void buildRouterSupport(Object... objects) {
        if (StringUtils.isEmpty(routerKey)) {
            return;
        }
        if (objects == null) {
            throw new RuntimeException("该方法不支持路由分表操作！");
        }
        for (Object object : objects) {
            if (object instanceof Criteria) {
                Criteria criteria = (Criteria) object;
                Object routerObj = getFieldValueByCondition(criteria, routerKey);
                if (routerObj == null) {
                    throw new RuntimeException(String.format("类型[%s]对应的路由对象属性[%s]不能为空", criteria.getClass().getName(), routerKey));
                }
                String tableIndex = router.router(routerObj);
                criteria.addExtField(TABLE_INDEX, tableIndex);
                continue;
            }
            if (object instanceof Map) {
                Map map = (Map) object;
                Object routerObj = map.get(routerKey);
                if (routerObj == null) {
                    throw new RuntimeException(String.format("类型[%s]对应的路由对象属性[%s]不能为空", this.getClass().getName(), routerKey));
                }
                String tableIndex = router.router(routerObj);
                map.put(TABLE_INDEX, tableIndex);
                // 兼容Criteria对象入参的写法
                String extFieldsString = "extFields";
                Object extFieldsObject = map.get(extFieldsString);
                if (extFieldsObject == null) {
                    Map<String, Object> extFields = new HashMap<String, Object>();
                    extFields.put(TABLE_INDEX, tableIndex);
                    map.put(extFieldsString, extFields);
                }
                continue;
            }
            if (object instanceof List) {
                List list = (List) object;
                for (Object obj : list) {
                    buildRouterSupport(obj);
                }
                continue;
            }
            throw new RuntimeException("该方法不支持路由分表操作！");
        }
    }
}
