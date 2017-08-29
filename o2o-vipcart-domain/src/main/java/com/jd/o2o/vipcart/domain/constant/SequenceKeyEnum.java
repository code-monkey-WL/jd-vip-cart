package com.jd.o2o.vipcart.domain.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务序列标识
 * Created by liuhuiqing on 2017/4/25.
 */
public enum SequenceKeyEnum {
    COUPON_ID("coupon_id","优惠券编码"),
    ACTIVITY_CODE("activity_code","活动编号"),
    CONSUME_CODE("consume_code","用户领取优惠券的唯一标识"),
    COUPON_CODE("coupon_code","优惠码"),
    GROUP_ID("group_id","领券包打标");

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
