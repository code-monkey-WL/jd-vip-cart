package com.jd.o2o.vipcart.common.domain.enums;

/**
 * 排序枚举值
 * Created by liuhuiqing on 2015/3/25.
 */
public enum SortEnum {
    DESC(1, "desc", "降序"),
    ASC(2, "asc", "升序"),;
    /**
     * CODE值
     */
    private Integer code;
    /**
     * 缩写
     */
    private String name;
    /**
     * 描述
     */
    private String desc;

    SortEnum(Integer code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
