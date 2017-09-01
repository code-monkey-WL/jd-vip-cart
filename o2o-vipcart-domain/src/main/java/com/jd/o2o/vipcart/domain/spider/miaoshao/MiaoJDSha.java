package com.jd.o2o.vipcart.domain.spider.miaoshao;

import com.jd.o2o.vipcart.common.domain.BaseBean;

import java.util.List;

/**
 * Created by liuhuiqing on 2017/9/1.
 */
public class MiaoJDSha extends BaseBean {
    private List<MiaoShaJDGoodInfo> miaoShaList;
    private MiaoShaJDGroup[] groups;

    public List<MiaoShaJDGoodInfo> getMiaoShaList() {
        return miaoShaList;
    }

    public void setMiaoShaList(List<MiaoShaJDGoodInfo> miaoShaList) {
        this.miaoShaList = miaoShaList;
    }

    public MiaoShaJDGroup[] getGroups() {
        return groups;
    }

    public void setGroups(MiaoShaJDGroup[] groups) {
        this.groups = groups;
    }
}
