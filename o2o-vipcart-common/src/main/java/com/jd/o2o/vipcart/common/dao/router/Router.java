package com.jd.o2o.vipcart.common.dao.router;

/**
 * Hash路由
 * Created by liuhuiqing on 2017/4/26.
 */
public interface Router<T> {
    /**
     * 根据入参返回路由（下标）
     * @param param
     * @return
     */
    public String router(T param);
}
