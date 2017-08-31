package com.jd.o2o.vipcart.common.plugins.spider.parse;

import com.jd.o2o.vipcart.common.plugins.spider.domain.SpiderInput;

import java.util.List;

/**
 * 文档解析器
 * Created by liuhuiqing on 2017/8/31.
 */
public interface Spider {
    public <T> List<T> analyse(SpiderInput<T> spiderInput);
}
