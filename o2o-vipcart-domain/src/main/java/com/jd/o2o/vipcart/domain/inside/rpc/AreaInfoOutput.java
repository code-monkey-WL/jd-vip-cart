package com.jd.o2o.vipcart.domain.inside.rpc;

/**
 * Created by songwei3 on 2017/6/9.
 */
public class AreaInfoOutput {
    /***
     * 地域id
     */
    private String areaCode;
    /***
     * 地域名称
     */
    private String areaName;
    /***
     * 地域等级
     */
    private Integer areaLevel;

    public AreaInfoOutput(String areaCode, String areaName, Integer areaLevel) {
        this.areaCode = areaCode;
        this.areaName = areaName;
        this.areaLevel = areaLevel;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Integer areaLevel) {
        this.areaLevel = areaLevel;
    }
}
