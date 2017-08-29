package com.jd.o2o.vipcart.domain.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典类型编码（对应字典配置的字典类型编码）
 * Created by liuhuiqing on 2017/4/25.
 */
public enum DictTypeCodeEnum {
    SYSTEM_SETTING("SYSTEM_SETTING","系统设置"),
    vipcart_APPROVAL_SETTING("vipcart_APPROVAL_SETTING","优惠券审批流程配置"),
    USER_vipcart_EXPIRED_REMINDER("USER_vipcart_EXPIRED_REMINDER","优惠券过期提醒配置"),
    SPECIAL_ACTIVITY_SHOW("SPECIAL_ACTIVITY_SHOW","特殊活动WEB展示"),
//    USER_vipcart_OVER_DUE_DAYS("USER_vipcart_OVER_DUE_DAYS","用户劵过期时间（天数）"),//2
//    USER_REMINDER_INTERVAL_DAY("USER_REMINDER_INTERVAL_DAY","每个用户劵过期提醒间隔天数"),//1
//    USER_REMINDER_PLAN_TIME("USER_REMINDER_PLAN_TIME","用户过期提醒消息计划发送时间"),//11:00
//    USER_REMINDER_CONTENT_TEMPLATE("USER_REMINDER_CONTENT_TEMPLATE","优惠券过期提醒内容模版"),//您可能有%d张京东到家优惠券将在%s过期，面值%d元，请不要错过哦！
    NEW_USER_ACTIVITY("NEW_USER_ACTIVITY","新人券包ABTest活动code"),
    vipcart_SETTING("vipcart_SETTING","优惠券配置");
//    vipcart_GRAB_FINISH_REMINDER_CONTENT_TEMPLATE("vipcart_GRAB_FINISH_REMINDER_CONTENT_TEMPLATE","劵即将领尽提醒内容模板"),//活动名称:%s,领券方式:%s,类型:%s,面值%d元,发券总量:%d,用券总量超过:%d%,如继续追加,请登录运营平台操作
//    vipcart_GRAB_FINISH_LADDER("vipcart_GRAB_FINISH_LADDER","劵即将领尽比例限制阶梯 从大到小，逗号分隔"),//90,70
//    SHARE_REWARD_QUOTA_CONFIG("SHARE_REWARD_QUOTA_CONFIG","分享有礼返点记录面值配置");//[{"time":"2016-09-22 00:00:00","quota":500},{"time":"2017-04-14 17:36:31","quota":1000}];

    DictTypeCodeEnum(String code, String name) {
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

    private static final Map<String, DictTypeCodeEnum> map = new HashMap<String, DictTypeCodeEnum>();

    static {
        for (DictTypeCodeEnum t : DictTypeCodeEnum.values()) {
            map.put(t.getCode(), t);
        }
    }

    public static DictTypeCodeEnum idOf(String code) {
        return map.get(code);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(code).append(",").append(name).toString();
    }
}
