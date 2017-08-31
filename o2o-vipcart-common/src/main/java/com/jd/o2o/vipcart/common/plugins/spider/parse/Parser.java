package com.jd.o2o.vipcart.common.plugins.spider.parse;

import com.jd.o2o.vipcart.common.plugins.spider.domain.ScanRuleInput;

import java.util.List;

/**
 * 文本解析器
 * Created by liuhuiqing on 2017/8/31.
 */
public interface Parser {
    public <T> List<T> parse(ScanRuleInput<T> scanRuleInput);
}
