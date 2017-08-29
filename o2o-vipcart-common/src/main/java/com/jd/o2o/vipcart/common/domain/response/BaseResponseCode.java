package com.jd.o2o.vipcart.common.domain.response;

/**
 * 基本服务响应结果代码
 * User: wuqingming
 * Date: 14-3-12
 * Time: 下午6:07
 */
public enum BaseResponseCode implements ResponseCode {
    /** 系统错误 */
    UNKNOWN_ERROR("-4", "未知错误"),
    /** 系统错误 */
    SYSTEM_ERROR("-3", "系统错误"),
    /** 处理警告，主要针对允许成功失败同时存在的处理（如批处理）*/
    WARNING("-2", "警告"),
    /** 处理失败 */
    FAILURE("-1", "失败"),
    /** 处理成功 */
    SUCCESS("0", "成功"),
    /** 参数错误 */
    PARAM_ERROR("1", "参数错误"),
    /** 方法不存在 */
    NO_SUCH_METHOD("2", "方法不存在"),
    /** 用户未登录 */
    USER_NOT_LOGIN("3", "用户未登录"),
    /** 记录不存在 */
    RECORD_NOT_EXISTS("4", "%s不存在"),
    /** 记录不可用 */
    RECORD_UNAVAILABLE("5", "%s不可用"),
    /** 重复记录 */
    DUPLICATE_RECORD("6", "重复记录"),
    /** 记录被引用 */
    REFERENCED_RECORD("7", "记录被引用"),
    /** 记录被引用 */
    BUSINESS_ILLEGAL_OP("9", "非法的操作"),
    /** 重复提交 */
    REPEAT_SUBMIT("10", "重复提交"),
    /** 无此操作权限 */
    NO_PERMISSION("11", "无此操作权限"),
    /** IP已变更 */
    IP_CHANGED("12", "IP已变更"),
    /** session失效 */
    SESSION_EXPIRED("13", "session失效");

    private String code;
    private String msg;

    private BaseResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
