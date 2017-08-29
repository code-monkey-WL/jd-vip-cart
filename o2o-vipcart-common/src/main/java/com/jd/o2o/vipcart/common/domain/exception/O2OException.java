package com.jd.o2o.vipcart.common.domain.exception;

import com.jd.o2o.vipcart.common.domain.response.ResponseCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * O2O 业务异常，所有O2O业务异常继承此异常
 * User: wuqingming
 * Date: 14-1-16
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
public class O2OException extends RuntimeException {
    /**
     * 业务代码
     */
    private String code;

    /**
     * 错误详细信息
     */
    private List<String> detail;

    public O2OException() {

    }

    public O2OException(String message) {
        super(message);
    }

    public O2OException(String code, String message) {
        super(message);
        this.code = code;
    }

    public O2OException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public O2OException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }
    public O2OException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
    }

    public O2OException(ResponseCode responseCode, Throwable cause) {
        super(responseCode.getMsg(), cause);
        this.code = responseCode.getCode();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetail() {
        return joinDetail();
    }

    public List<String> getDetailList() {
        return detail;
    }

    private void preAddDetail() {
        if (this.detail == null) {
            this.detail = new ArrayList<String>();
        }
    }

    public void setDetail(String... detail) {
        if (detail == null) {
            this.detail = new ArrayList<String>();
            return;
        }
        this.detail = Arrays.asList(detail);
    }

    public void setDetail(List<String> detail) {
        this.detail = detail;
    }

    public <T extends O2OException> T addDetail(List<String> detail) {
        if (detail == null) {
            return (T)this;
        }
        preAddDetail();
        this.detail.addAll(detail);
        return (T)this;
    }

    public <T extends O2OException> T addDetail(String... detail) {
        if (detail == null) {
            return (T)this;
        }
        preAddDetail();
        this.detail.addAll(Arrays.asList(detail));
        return (T)this;
    }

    private String joinDetail() {
        if (detail == null || detail.size() == 0) {
            return null;
        }
        if (detail.size() == 1) {
            return detail.get(0);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : detail) {
            stringBuilder.append("，").append(item);
        }
        return stringBuilder.substring(1);
    }

    public String toString(){
        return "code:" + code + ", msg:" + getMessage() + ",detail:[" + joinDetail() + "]";
    }

}
