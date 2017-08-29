package com.jd.o2o.vipcart.domain.inside.rpc;

/**
 * 商家门店信息
 * Created by liuhuiqing on 2017/5/15.
 */
public class StationOutput extends OrgOutput {
    /**
     * 门店编号
     */
    private Long stationNo;
    /**
     * 门店名称
     */
    private String stationName;
    /**
     * 门店所属行业
     */
    private Integer industryTag;
    /**
     * 门店所属行业名称
     */
    private String industryTagName;
    /**
     * 门店所属行业类型
     */
    private String industryTagType;
    /**
     * 经度
     */
    private Double lon;
    /**
     * 纬度
     */
    private Double lat;

    public Long getStationNo() {
        return stationNo;
    }

    public void setStationNo(Long stationNo) {
        this.stationNo = stationNo;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Integer getIndustryTag() {
        return industryTag;
    }

    public void setIndustryTag(Integer industryTag) {
        this.industryTag = industryTag;
    }

    public String getIndustryTagName() {
        return industryTagName;
    }

    public void setIndustryTagName(String industryTagName) {
        this.industryTagName = industryTagName;
    }

    public String getIndustryTagType() {
        return industryTagType;
    }

    public void setIndustryTagType(String industryTagType) {
        this.industryTagType = industryTagType;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
