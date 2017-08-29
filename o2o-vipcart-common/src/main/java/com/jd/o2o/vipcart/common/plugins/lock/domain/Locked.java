package com.jd.o2o.vipcart.common.plugins.lock.domain;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * 同步排他锁对象描述
 * Created by liuhuiqing on 2016/3/22.
 */
public class Locked extends BaseBean {
    /**
     * 锁对象key
     */
    private Object key;
    /**
     * 锁表填充值对象
     */
    private Object value;
    /**
     * 锁定时间长度
     */
    private Long millLockTime;
    /**
     * 是否主动释放（不用等到锁定时长的被动释放锁），
     * 为空或true时表示主动释放，为false时表示被动释放（只能被动的等到超时释放，调用解锁方法并不能释放锁）
     */
    private Boolean activeRelease;

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Long getMillLockTime() {
        return millLockTime;
    }

    public void setMillLockTime(Long millLockTime) {
        this.millLockTime = millLockTime;
    }

    public Boolean getActiveRelease() {
        return activeRelease;
    }

    public void setActiveRelease(Boolean activeRelease) {
        this.activeRelease = activeRelease;
    }
}
