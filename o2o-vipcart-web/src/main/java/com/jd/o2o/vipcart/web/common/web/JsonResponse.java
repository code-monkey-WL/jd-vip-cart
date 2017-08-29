package com.jd.o2o.vipcart.web.common.web;

import com.jd.o2o.vipcart.common.domain.response.BaseResponseCode;

import java.io.Serializable;

public class JsonResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String msg;
    private String code;
    private String header;
    private Object result;
    private Page page;
    private Foot foot;

    public JsonResponse() {
        this.code = BaseResponseCode.SUCCESS.getCode();
        this.msg = BaseResponseCode.SUCCESS.getMsg();
    }

    public JsonResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getResult() {
        return this.result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getHeader() {
        return this.header==null?"":this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Foot getFoot() {
        if(null == this.foot) {
            this.foot = new Foot();
        }

        return this.foot;
    }

    public void setFoot(Foot foot) {
        this.foot = foot;
    }

}
