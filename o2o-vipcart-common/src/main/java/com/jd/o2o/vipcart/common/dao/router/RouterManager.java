package com.jd.o2o.vipcart.common.dao.router;

import java.util.Map;

/**
 * 路由管理类
 * Created by liuhuiqing on 2017/4/26.
 */
public class RouterManager {
    private final Map<String, Router> routerMap;

    public RouterManager(Map<String, Router> routerMap) {
        if (routerMap == null || routerMap.isEmpty()) {
            throw new IllegalArgumentException("RouterManager初始化入参不能为空！");
        }
        this.routerMap = routerMap;
    }

    public String router(String name, Object param) {
        Router router = routerMap.get(name);
        if (router == null) {
            throw new IllegalArgumentException("没有找到名称为" + name + "的路由实现！");
        }
        return router.router(param);
    }
}
