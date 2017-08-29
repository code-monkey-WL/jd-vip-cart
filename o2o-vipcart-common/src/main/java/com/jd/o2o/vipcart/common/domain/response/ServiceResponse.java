package com.jd.o2o.vipcart.common.domain.response;


import java.io.Serializable;

/**
 * 服务响应信息基类，所有的服务响应信息应继承此类
 * User: wuqingming
 * Date: 14-1-7
 * Time: 上午10:52
 * To change this template use File | Settings | File Templates.
 */
public class ServiceResponse<T> implements Serializable {

    private static final long serialVersionUID = 2488663702267110932L;
    /**
     * 业务代码,根据自身业务需求定义服务响应代码
     *
     * @see ResponseCode#getCode()
     */
    private String code = BaseResponseCode.SUCCESS.getCode();
    /**
     * 业务代码描述
     *
     * @see ResponseCode#getMsg()
     */
    private String msg = BaseResponseCode.SUCCESS.getMsg();

    /**
     * 详细信息描述
     */
    private String detail;

    /**
     * 业务结果数据
     */
    private T result;

    /**
     * 成功响应信息
     *
     * @return
     */
    public static ServiceResponse successResponse() {
        return new ServiceResponse(BaseResponseCode.SUCCESS);
    }

    /**
     * 失败响应信息
     *
     * @return
     */
    public static ServiceResponse failureResponse() {
        return new ServiceResponse(BaseResponseCode.FAILURE);
    }

    public ServiceResponse() {
    }

    public ServiceResponse(T result) {
        this.result = result;
    }

    public ServiceResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ServiceResponse(String code, String msg, String detail) {
        this.code = code;
        this.msg = msg;
        this.detail = detail;
    }

    public ServiceResponse(ResponseCode ResponseCode) {
        this.code = ResponseCode.getCode();
        this.msg = ResponseCode.getMsg();
    }

    public ServiceResponse(ResponseCode ResponseCode, String detail) {
        this.code = ResponseCode.getCode();
        this.msg = ResponseCode.getMsg();
        this.detail = detail;
    }

    public void setResponseCode(ResponseCode ResponseCode) {
        this.code = ResponseCode.getCode();
        this.msg = ResponseCode.getMsg();
    }

    /**
     * 接口是否调用成功标识
     *
     * @return
     */
    public boolean isSuccess() {
        return BaseResponseCode.SUCCESS.getCode().equals(code);
    }

    /**
     * 获取响应代码
     *
     * @return
     * @see ResponseCode#getCode()
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置响应代码
     *
     * @see ResponseCode#getCode()
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取业务代码描述
     *
     * @see ResponseCode#getMsg()
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置业务代码描述
     *
     * @see ResponseCode#getMsg()
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public <T extends ServiceResponse> T addDetail(String detail) {
        setDetail(detail);
        return (T) this;
    }

    /**
     * 获取业务数据
     *
     * @return
     */
    public T getResult() {
        return result;
    }

    /**
     * 设置业务数据
     *
     * @return
     */
    public void setResult(T result) {
        this.result = result;
    }

}
