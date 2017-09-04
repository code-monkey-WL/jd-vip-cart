package com.jd.o2o.vipcart.service.busi.spider;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.domain.spider.SpiderParam;

/**
 * 内容抓取服务声明
 */
public interface SpiderService {
    PageBean spider(SpiderParam spiderParam);
}
