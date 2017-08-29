package com.jd.o2o.vipcart.common.plugins.monitor.domain;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * 监控视图对象
 * Created by liuhuiqing on 2016/1/19.
 */
public class MonitorVO<T> extends BaseBean{
    private String appCode;
    private String className;
    private String methodName;
    private String methodDesc;
    private String dateTime;
    private String prefix;
    private T value;
    private String ext;

    public static MonitorVO newSimpleInstance(String appCode,String methodName,String prefix){
        MonitorVO monitorVO = new MonitorVO();
        monitorVO.setAppCode(appCode);
        monitorVO.setMethodName(methodName);
        monitorVO.setPrefix(prefix);
        return monitorVO;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
