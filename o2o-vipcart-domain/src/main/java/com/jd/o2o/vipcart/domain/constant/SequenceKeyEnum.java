package com.jd.o2o.vipcart.domain.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务序列标识
 * Created by liuhuiqing on 2017/4/25.
 */
public enum SequenceKeyEnum {
    SKU_ID("sku_id","商品编号"),
    SKU_CODE("sku_code","商品编码");

    SequenceKeyEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<String, SequenceKeyEnum> map = new HashMap<String, SequenceKeyEnum>();

    static {
        for (SequenceKeyEnum t : SequenceKeyEnum.values()) {
            map.put(t.getCode(), t);
        }
    }

    public static SequenceKeyEnum idOf(String code) {
        return map.get(code);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(code).append(",").append(name).toString();
    }
}
