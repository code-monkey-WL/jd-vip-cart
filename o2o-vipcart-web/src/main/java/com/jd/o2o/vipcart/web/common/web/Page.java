package com.jd.o2o.vipcart.web.common.web;

import java.io.Serializable;

public class Page implements Serializable {
    private static final long serialVersionUID = 1L;
    private int currentPage;
    private int pageSize;
    private int count;
    private int totalPageNum;
    private int recordNum;
    private Boolean hasPre;
    private Boolean hasNext;
    public static final int DEFAULT_PAGESIZE = 10;

    public Page() {
    }

    /** @deprecated */
    @Deprecated
    public Page(int currentPage, int pageSize, int count) {
        this.setCurrentPage(currentPage);
        this.setPageSize(pageSize);
        this.setCount(count);
    }

    public Page(int currentPage, int pageSize, int count, int recordNum) {
        this(currentPage, pageSize, count);
        this.setRecordNum(recordNum);
        this.getHasPre();
        this.getHasNext();
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPageNum() {
        if(this.pageSize < 1) {
            this.pageSize = 10;
        }

        if(this.totalPageNum < 1) {
            if(this.count % this.pageSize > 0) {
                this.totalPageNum = this.count / this.pageSize + 1;
            } else {
                this.totalPageNum = this.count / this.pageSize;
            }
        }

        return this.totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public Boolean getHasPre() {
        if(this.currentPage > 1) {
            this.hasPre = Boolean.valueOf(true);
        } else {
            this.hasPre = Boolean.valueOf(false);
        }

        return this.hasPre;
    }

    public void setHasPre(Boolean hasPre) {
        this.hasPre = hasPre;
    }

    public Boolean getHasNext() {
        if(this.currentPage < this.getTotalPageNum()) {
            this.hasNext = Boolean.valueOf(true);
        } else {
            this.hasNext = Boolean.valueOf(false);
        }

        return this.hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getRecordNum() {
        return this.recordNum;
    }

    public void setRecordNum(int recordNum) {
        this.recordNum = recordNum;
    }
}
