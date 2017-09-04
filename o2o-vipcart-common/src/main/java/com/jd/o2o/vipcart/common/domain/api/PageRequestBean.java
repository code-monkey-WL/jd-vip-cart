package com.jd.o2o.vipcart.common.domain.api;

/**
 * 对外api通用基类
 */
public class PageRequestBean extends RequestBean {
    private static final long serialVersionUID = -1231524027443366825L;
    /**
     * 分页查询的属性*
     */
    private int pageNo = 1;
    private int pageSize = 20;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
