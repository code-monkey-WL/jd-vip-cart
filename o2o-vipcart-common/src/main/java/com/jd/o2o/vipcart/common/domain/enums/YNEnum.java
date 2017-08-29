package com.jd.o2o.vipcart.common.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成业务主键标记枚举类
 * Created by liuhuiqing on 2015/3/4.
 */
public enum YNEnum {
    Y(0,"启用"),
    N(1,"禁用");
    /**
     * CODE值
     */
    private Integer code;
    /**
     * 名称
     */
    private String name;

    YNEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer,YNEnum> map = new HashMap<Integer,YNEnum>();
    static {
        for(YNEnum t: YNEnum.values()){
            map.put(t.getCode(),t);
        }
    }
    /**
     * 根据code获得对应的枚举项
     * @param code
     * @return
     */
    public static YNEnum get(Integer code){
        return map.get(code);
    }

    @Override
    public String toString(){
        return new StringBuilder().append(this.getCode()).append(":").append(this.getName()).toString();
    }
}
