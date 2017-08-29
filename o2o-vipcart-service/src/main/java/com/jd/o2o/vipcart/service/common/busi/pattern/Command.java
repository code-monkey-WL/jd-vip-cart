package com.jd.o2o.vipcart.service.common.busi.pattern;

/**
 * 系统命令行
 * Created by liuhuiqing on 2017/6/12.
 */
public interface Command<T extends Object> {
    public void handler(T  param);

    public void rollBack(T param);
}
