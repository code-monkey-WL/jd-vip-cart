package com.jd.o2o.vipcart.common.plugins.directive.model;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * Created by liuhuiqing on 2015/6/1.
 */
public class UpDownloadModel extends BaseBean {
    private String resCode;
    private String action;
    private String ext;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
