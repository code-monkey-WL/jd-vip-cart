package com.jd.o2o.vipcart.service.common.busi.pattern;

/**
 * 组命令实现
 * Created by liuhuiqing on 2017/6/12.
 */
public interface GroupCommand<T> {
    /**
     * 业务执行逻辑
     * @param commandParam
     */
    public void execute(T commandParam);
    /**
     * 主业务流程
     */
    public void afterCommand(T commandParam);

}
