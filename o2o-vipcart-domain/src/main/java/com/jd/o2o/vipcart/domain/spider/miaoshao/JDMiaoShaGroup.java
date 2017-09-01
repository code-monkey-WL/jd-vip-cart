package com.jd.o2o.vipcart.domain.spider.miaoshao;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * Created by liuhuiqing on 2017/9/1.
 */
public class JDMiaoShaGroup extends BaseBean {
    private String gid; // 30,
    private String displayTime; // "20:00",
    private String name; // "20点场",
    private String functionId; // "miaoShaAreaList",
    private String timeRemain; // 6345,
    private String groupTime; // "20:00",
    private String sourceValue; // "30_0_1",
    private String startTime; // "2017-09-01 20:00:00",
    private String startTimeMills; // 1504267200000,
    private String endTime; // "2017-09-01 22:00:00",
    private String[] picList; // [ ]

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getTimeRemain() {
        return timeRemain;
    }

    public void setTimeRemain(String timeRemain) {
        this.timeRemain = timeRemain;
    }

    public String getGroupTime() {
        return groupTime;
    }

    public void setGroupTime(String groupTime) {
        this.groupTime = groupTime;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeMills() {
        return startTimeMills;
    }

    public void setStartTimeMills(String startTimeMills) {
        this.startTimeMills = startTimeMills;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String[] getPicList() {
        return picList;
    }

    public void setPicList(String[] picList) {
        this.picList = picList;
    }
}
