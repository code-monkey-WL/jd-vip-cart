package com.jd.o2o.vipcart.common.domain;

import java.io.Serializable;
import java.util.Collection;

/**
 * 分页信息
 * User: lihongyu wuqingming
 * Date: 13-8-30
 * Time: 下午4:03
 * 
 * Date:20150618
 * User:wenjun 增加最大分页条数的控制(100)
 */
public class PageBean<T> implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 默认每页条数
     */
    public static final int DEFAULT_PAGE_SIZE = 20;
    /**
     * 最大分页条数
     */
    public static final int PAGE_SIZE_MAX = 100;
    
    /**
     * 当前页码，默认为1
     */
    private long pageNo = 1;
    /**
     * 每页的条数
     */
    private int pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 每页最大条数
     */
    private int maxPageSize = PAGE_SIZE_MAX;
    /**
     * 此次查询的总记录数（并不一定是当前页的记录条数）
     */
    private long totalCount;
    /**
     * 存放查询的返回结果
     */
    private Collection<T> resultList;
    
    public PageBean() {

    }

    /**
     * 作为查询条件的PageBean构造方法
     * @param pageNo    当前页号
     * @param pageSize  页大小
     */
    public PageBean(long pageNo, int pageSize) {
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
    }

    /**
     * 作为查询结果的PageBean的构造方法
     * @param totalCount    总记录数
     * @param resultList    分页查询结果列表
     */
    public PageBean(long totalCount, Collection<T> resultList) {
        this.totalCount = totalCount;
        this.resultList = resultList;
    }

    /**
     * 获取总页数
     * @return
     */
    public long getTotalPage() {
        long totalPage = 0;
        if (totalCount > 0) {
            totalPage = ((totalCount % pageSize == 0) ? (totalCount / pageSize) : (totalCount / pageSize + 1));
        }
        return totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
    	if(pageSize < 1){
    		pageSize = DEFAULT_PAGE_SIZE;
    	}else if(pageSize > maxPageSize){
    		pageSize = maxPageSize;
    	}
    	this.pageSize = pageSize;
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
    	if(pageNo < 1){
    		pageNo = 1;
    	}
		this.pageNo = pageNo;
    }
    
    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
    	if(totalCount < 0 ){
    		totalCount = 0;
    	}
        this.totalCount = totalCount;
    }
    
    @SuppressWarnings("unchecked")
   	public <C extends Collection<T>> C getResultList() {
           return resultList == null ? null : (C)resultList;
    }

    public void setResultList(Collection<T> resultList) {
        this.resultList = resultList;
    }

    /**
     * 设置当前页号
     * @deprecated
     *  @see com.jd.o2o.vipcart.common.domain.PageBean#setPageNo(long)
     * @param page
     */
    @Deprecated
    public void setPage(int page) {
        this.setPageNo(page);
    }

    /**
     * 获取当前页号
     * @deprecated
     *  @see com.jd.o2o.vipcart.common.domain.PageBean#getPageNo()
     * @return
     */
    @Deprecated
    public long getPage() {
        return this.pageNo;
    }

	public int getMaxPageSize() {
		return maxPageSize;
	}

	public void setMaxPageSize(int maxPageSize) {
		this.maxPageSize = maxPageSize<0 ?  PAGE_SIZE_MAX : maxPageSize;
	}
}
