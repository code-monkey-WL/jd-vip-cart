package com.jd.o2o.vipcart.common.plugins.log;

import java.util.List;

/**
 * 日志记录接口抽象
 * Created by liuhuiqing on 2015/8/18.
 */
public interface LoggableService {
    /**
     * 保存日志记录
     * @param entryBean
     * @return
     */
    public int log(LogBean entryBean);

    /**
     * 查询上次操作记录
     * @param entryBean
     * @return
     */
    public List<LogBean> findListLogBean(LogBean entryBean);
}
