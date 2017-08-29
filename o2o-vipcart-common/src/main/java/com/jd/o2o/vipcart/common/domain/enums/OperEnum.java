package com.jd.o2o.vipcart.common.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务操作
 * Created by liuhuiqing on 2015/7/7.
 */
public enum OperEnum {
    ADD(1,"新增"),
    EDIT(2,"修改"),
    FIND(3,"查询"),
    DELETE(4,"删除"), ;

    OperEnum(int code, String name) {
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

    private static final Map<Integer, OperEnum> map = new HashMap<Integer, OperEnum>();

    static {
        for (OperEnum t : OperEnum.values()) {
            map.put(t.getCode(), t);
        }
    }

    public static OperEnum idOf(int code) {
        return map.get(code);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.getCode()).append(":").append(this.getName()).toString();
    }
}
