package com.jd.o2o.vipcart.common.domain.criteria;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询条件基类，所有查询条件集成此类
 * User: wuqingming
 * Date: 14-2-24
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
public class Criteria implements Serializable {
    /**
     * 排序方向-升序
     */
    public static final String SORT_DIRECTION_ASC = "ASC";
    /**
     * 排序方向-降序
     */
    public static final String SORT_DIRECTION_DESC = "DESC";

    /**
     * 排序参数
     * key：属性字段，value：排序方向-asc or desc
     * @see com.jd.o2o.vipcart.common.domain.criteria.Criteria#SORT_DIRECTION_ASC
     * @see com.jd.o2o.vipcart.common.domain.criteria.Criteria#SORT_DIRECTION_DESC
     */
    private LinkedHashMap<String, String> sortItemMap;

    /**                                                                                  J
     * 扩展属性字段
     */
    private Map<String, Object> extFields;
    /**
     * 添加扩展字段
     *
     * @param fieldName  字段名称
     * @param filedValue 字段值
     */
    public <C extends Criteria> C addExtField(String fieldName, Object filedValue) {
        if (extFields == null) {
            extFields = new HashMap<String, Object>();
        }
        extFields.put(fieldName, filedValue);
        return (C)this;
    }

    public Map<String, Object> getExtFields() {
        return extFields;
    }

    public void setExtFields(Map<String, Object> extFields) {
        this.extFields = extFields;
    }


    public LinkedHashMap<String, String> getSortItemMap() {
        return sortItemMap;
    }

    public void setSortItemMap(LinkedHashMap<String, String> sortItemMap) {
        this.sortItemMap = sortItemMap;
    }
}
