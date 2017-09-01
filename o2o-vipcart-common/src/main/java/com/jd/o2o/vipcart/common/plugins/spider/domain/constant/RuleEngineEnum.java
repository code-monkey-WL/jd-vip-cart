package com.jd.o2o.vipcart.common.plugins.spider.domain.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 扫描规则类型
 * Created by liuhuiqing on 2017/8/31.
 */
public enum RuleEngineEnum {
    JSOUP(1,"jsoup解析规则");


    RuleEngineEnum(Integer code, String name) {
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

    private static final Map<Integer, RuleEngineEnum> map = new HashMap<Integer, RuleEngineEnum>();

    static {
        for (RuleEngineEnum t : RuleEngineEnum.values()) {
            map.put(t.getCode(), t);
        }
    }

    public static RuleEngineEnum idOf(Integer code) {
        return map.get(code);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(code).append(",").append(name).toString();
    }
}
