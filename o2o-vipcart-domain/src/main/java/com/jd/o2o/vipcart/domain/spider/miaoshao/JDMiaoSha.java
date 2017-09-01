package com.jd.o2o.vipcart.domain.spider.miaoshao;

import com.jd.o2o.vipcart.common.domain.BaseBean;

import java.util.List;

/**
 * Created by liuhuiqing on 2017/9/1.
 */
public class JDMiaoSha extends BaseBean {
    private List<JDMiaoShaGoodInfo> miaoShaList;
    private JDMiaoShaGroup[] groups;

    public List<JDMiaoShaGoodInfo> getMiaoShaList() {
        return miaoShaList;
    }

    public void setMiaoShaList(List<JDMiaoShaGoodInfo> miaoShaList) {
        this.miaoShaList = miaoShaList;
    }

    public JDMiaoShaGroup[] getGroups() {
        return groups;
    }

    public void setGroups(JDMiaoShaGroup[] groups) {
        this.groups = groups;
    }
}
