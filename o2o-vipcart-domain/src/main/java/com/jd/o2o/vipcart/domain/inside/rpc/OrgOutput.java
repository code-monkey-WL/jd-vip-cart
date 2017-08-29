package com.jd.o2o.vipcart.domain.inside.rpc;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * Created by songwei3 on 2017/6/9.
 */
public class OrgOutput extends BaseBean{
    /***
     * 商家名称
     */
    private String orgName;
    /***
     * 商家编码
     */
    private String orgCode;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}
