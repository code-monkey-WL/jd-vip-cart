package com.jd.o2o.vipcart.common.dao.router;

/**
 * Hash路由实现
 * Created by liuhuiqing on 2017/4/26.
 */
public class HashRouter implements Router {
    private final int shardsNumber;

    public HashRouter(int shardsNumber) {
        if (shardsNumber < 1) {
            throw new IllegalArgumentException("构造HashRouter入参shardsNumber不能小于1！");
        }
        this.shardsNumber = shardsNumber;
    }

    @Override
    public String router(Object param) {
        if (param == null) {
            throw new IllegalArgumentException("路由入参不能为空！！");
        }
        return String.valueOf(Math.abs(param.hashCode() % shardsNumber));
    }
}
