package com.jd.o2o.vipcart.common.plugins.monitor;

import com.jd.o2o.vipcart.common.plugins.monitor.domain.MonitorVO;

import java.util.List;

/**
 * 方法切入监控
 * Created by liuhuiqing on 2016/1/15.
 */
public interface Monitor<T> {
    /**
     * 切面运行监控状态
     * @return
     */
    public List<MonitorVO<T>> monitor();
}
