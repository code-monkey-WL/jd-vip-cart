package com.jd.o2o.vipcart.common.domain.id;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * 主键编号区间范围
 * Created by liuhuiqing on 2017/4/20.
 */
public class SequenceRange extends BaseBean {
    private Long startNo; // 可用起始序号
    private Long endNo; // 可用截止序号
    private Long cursorNo; // 当前游标需要，默认等于起始序号

    public Long getStartNo() {
        return startNo;
    }

    public void setStartNo(Long startNo) {
        this.startNo = startNo;
    }

    public Long getEndNo() {
        return endNo;
    }

    public void setEndNo(Long endNo) {
        this.endNo = endNo;
    }

    public Long getCursorNo() {
        return cursorNo;
    }

    public void setCursorNo(Long cursorNo) {
        this.cursorNo = cursorNo;
    }
}
