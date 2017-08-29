package com.jd.o2o.vipcart.common.plugins.log;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志记录持久化类型
 * Created by liuhuiqing on 2015/8/18.
 */
public enum LogTypeEnum {
    FILE(1,"日志文件"),
    DB(1,"数据库"), ;

    LogTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, LogTypeEnum> map = new HashMap<Integer, LogTypeEnum>();

    static {
        for (LogTypeEnum t : LogTypeEnum.values()) {
            map.put(t.getCode(), t);
        }
    }

    public static LogTypeEnum idOf(int code) {
        return map.get(code);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.getCode()).append(":").append(this.getName()).toString();
    }
}
