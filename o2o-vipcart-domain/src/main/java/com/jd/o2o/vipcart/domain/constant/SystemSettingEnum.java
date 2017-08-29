package com.jd.o2o.vipcart.domain.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典类型编码（对应字典配置的字典项编码）
 * Created by liuhuiqing on 2017/4/25.
 */
public enum SystemSettingEnum {
    AUDIT_CONFIG("audit_config","审核配置信息",""),
    RECEIVE_MAX_THRESHOLD("receive_max_threshold","默认抢单最大阀值","5");

    SystemSettingEnum(String code, String name,String defaultValue) {
        this.code = code;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    private String code;
    private String name;
    private String defaultValue;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    private static final Map<String, SystemSettingEnum> map = new HashMap<String, SystemSettingEnum>();

    static {
        for (SystemSettingEnum t : SystemSettingEnum.values()) {
            map.put(t.getCode(), t);
        }
    }

    public static SystemSettingEnum idOf(String code) {
        return map.get(code);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(code).append(",").append(name).toString();
    }
}
