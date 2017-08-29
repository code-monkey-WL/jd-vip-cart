package com.jd.o2o.vipcart.common.domain.criteria;

import com.jd.o2o.vipcart.common.domain.BaseBean;
import com.jd.o2o.vipcart.common.domain.enums.SortEnum;

import java.util.List;
import java.util.Vector;

/**
 * 排序对象
 * Created by liuhuiqing on 2015/3/25.
 */
public class SortCriteria extends BaseBean {
    /**
     * 排序规则容器
     */
    private final Vector<SortBean> sortBeanList = new Vector<SortBean>();

    /**
     * 构建排序属性规则
     *
     * @param sortBean
     * @return
     */
    public SortCriteria buildSortCriteria(SortBean sortBean) {
        sortBeanList.add(sortBean);
        return this;
    }

    /**
     * 构建排序属性规则
     *
     * @param name     属性名称
     * @param sortEnum 排序规则
     * @return
     */
    public SortCriteria buildSortCriteria(String name, SortEnum sortEnum) {
        sortBeanList.add(new SortBean(name, sortEnum == null ? SortEnum.ASC : sortEnum));
        return this;
    }

    /**
     * 清除排序规则
     *
     * @return
     */
    public SortCriteria clearSortCriteria() {
        sortBeanList.clear();
        return this;
    }

    /**
     * 获得排序规则
     *
     * @return
     */
    public List<SortBean> getSortBeanList() {
        return sortBeanList;
    }

    /**
     * 属性排序
     * Created by liuhuiqing on 2015/3/25.
     */
    public static class SortBean extends BaseBean {
        /**
         * 待排序的属性名称
         */
        private String name;
        /**
         * 排序规则
         */
        private SortEnum sortEnum;

        public SortBean() {
        }

        public SortBean(String name, SortEnum sortEnum) {
            this.name = name;
            this.sortEnum = sortEnum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SortEnum getSortEnum() {
            return sortEnum;
        }

        public void setSortEnum(SortEnum sortEnum) {
            this.sortEnum = sortEnum;
        }
    }
}
