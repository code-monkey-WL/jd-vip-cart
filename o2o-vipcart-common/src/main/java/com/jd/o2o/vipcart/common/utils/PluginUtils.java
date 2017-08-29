package com.jd.o2o.vipcart.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: liuhuiqing
 * Date: 15-2-23
 * Time: 下午5:11
 * To change this template use File | Settings | File Templates.
 */
public class PluginUtils {
    /**
     * 分页查询公共参数封装
     *
     * @param start 查询起始记录索引
     * @param end   查询接受记录索引
     * @return
     */
    public static Map<String, Object> buildPagingLimit(int start, int end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("limit", end);
        return map;
    }
}
