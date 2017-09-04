package com.jd.o2o.vipcart.domain.spider;

import com.jd.o2o.vipcart.common.domain.api.PageRequestBean;

/**
 * Created by liuhuiqing on 2017/9/4.
 */
public class SpiderParam extends PageRequestBean {
    private String inputParam;

    public String getInputParam() {
        return inputParam;
    }

    public void setInputParam(String inputParam) {
        this.inputParam = inputParam;
    }
}
