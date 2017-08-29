package com.jd.o2o.vipcart.common.domain;

import com.jd.o2o.vipcart.common.domain.criteria.Criteria;
import com.jd.o2o.vipcart.common.domain.enums.YNEnum;

import java.util.Date;

/**
 * 数据库映射关系实体公共属性
 * liuhuiqing
 */
public class BaseEntityBean extends Criteria {
    private static final long serialVersionUID = -7793739903799136331L;
    private Integer sysVersion;    //数据版本号
    private Date createTime;    //创建时间
    private Date updateTime;    //修改时间
    private String createPin;    //创建人
    private String updatePin;    //修改人
    private Integer yn;    //是否有效(0：默认值，有效；1：无效，已删除)
    private Date ts;    //时间戳

    public Integer getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(Integer sysVersion) {
        this.sysVersion = sysVersion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreatePin() {
        return createPin;
    }

    public void setCreatePin(String createPin) {
        this.createPin = createPin;
    }

    public String getUpdatePin() {
        return updatePin;
    }

    public void setUpdatePin(String updatePin) {
        this.updatePin = updatePin;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    /**
     * 初始化默认值
     */
    public BaseEntityBean init() {
        this.createPin = "";
        this.updatePin = "";
        this.yn = YNEnum.Y.getCode();
        return this;
    }
}
