package com.jd.o2o.vipcart.web.common.vo;

/**
 * 第三方数据视图
 * 适用:城市,商家,门店,行业,品牌下拉框
 * Created by luozhigang on 2017/7/13.
 */
public class RpcShowVO {

    private String id;
    private String name;


    public RpcShowVO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
