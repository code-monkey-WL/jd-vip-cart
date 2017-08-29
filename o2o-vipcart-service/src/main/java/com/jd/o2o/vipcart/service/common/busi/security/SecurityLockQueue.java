package com.jd.o2o.vipcart.service.common.busi.security;

import com.jd.o2o.vipcart.common.domain.BaseBean;
import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import org.apache.commons.lang.StringUtils;

/**
 * 分布式锁key队列
 * Created by liuhuiqing on 2017/8/10.
 */
public class SecurityLockQueue extends BaseBean{
    /**
     * 用途名称
     */
    private String name;
    /**
     * 队列大小，取值必须是2的幂
     */
    private Integer size = 128;
    /**
     * 可用key队列
     */
    private String[] queues;

    public SecurityLockQueue() {
    }

    public SecurityLockQueue(String name, Integer size) {
        this.name = name;
        this.size = size;
        initQueue();
    }

    public String getLockKey(Object object) {
        if (queues == null) {
            initQueue();
        }
        int index = object.hashCode() & (size - 1);
        return queues[index];
    }

    /**
     * 初始化可用key队列
     */
    private synchronized void initQueue() {
        if (queues != null) {
            return;
        }
        if (StringUtils.isEmpty(name)) {
            throw new BaseMsgException("SecurityLockQueue.initQueue初始化入参[name]不能为空！");
        }
        if (size == null || size < 1) {
            throw new BaseMsgException("SecurityLockQueue.initQueue初始化入参[size]不能小于1！");
        }
        queues = new String[size];
        for (int i = 0; i < size; i++) {
            queues[i] = name + i;
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
