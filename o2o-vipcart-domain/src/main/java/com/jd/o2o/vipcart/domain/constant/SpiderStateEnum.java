package com.jd.o2o.vipcart.domain.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 抓取状态
 * Created by liuhuiqing on 2017/9/5.
 */
public enum SpiderStateEnum {
    START(1,"开启"),
    RUNNING(2,"运行中"),
    PAUSE(3,"暂停"),
    DROP(4,"废弃");

    SpiderStateEnum(Integer code, String name) {
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

    private static final Map<Integer, SpiderStateEnum> map = new HashMap<Integer, SpiderStateEnum>();

    static {
        for (SpiderStateEnum t : SpiderStateEnum.values()) {
            map.put(t.getCode(), t);
        }
    }

    public static SpiderStateEnum idOf(Integer code) {
        return map.get(code);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(code).append(",").append(name).toString();
    }
}
