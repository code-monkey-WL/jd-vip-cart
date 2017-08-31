package com.jd.o2o.vipcart.common.plugins.spider.domain;

import java.util.HashMap;
import java.util.Map;

/**
 *  取值来源
 * Created by liuhuiqing on 2017/8/31.
 */
public enum ItemSourceEnum {
    ATTR(1,"元素属性"),
    TEXT(2,"文本"),
    IN_HTML(3,"标签内HTML片段"),
    OUT_HTML(3,"包含标签本身加内部HTML片段");


    ItemSourceEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, ItemSourceEnum> map = new HashMap<Integer, ItemSourceEnum>();

    static {
        for (ItemSourceEnum t : ItemSourceEnum.values()) {
            map.put(t.getCode(), t);
        }
    }

    public static ItemSourceEnum idOf(Integer code) {
        return map.get(code);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(code).append(",").append(name).toString();
    }
}
