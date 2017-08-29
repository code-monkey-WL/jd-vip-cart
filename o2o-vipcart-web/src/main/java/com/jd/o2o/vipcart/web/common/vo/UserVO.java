package com.jd.o2o.vipcart.web.common.vo;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * 用户信息
 * Created by liuhuiqing on 2017/4/25.
 */
public class UserVO extends BaseBean {

    private String userId;
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
