package com.jd.o2o.vipcart.service.common.project;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 工厂类
 * Created by liuhuiqing on 2016/2/22.
 */
@Component
public class ProjectFactory implements ApplicationContextAware {
    private static ApplicationContext applicationContext; // Spring应用上下文环境

//    private static Map<String, ActivityManageService> activityManageServiceMap;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ProjectFactory.applicationContext = applicationContext;
//        activityManageServiceMap = (Map<String, ActivityManageService>) applicationContext.getBean("activityManageServiceMap");
    }

    /**
     * 活动管理服务
     *
     * @param activityTypeEnum
     * @return
     */
//    public static ActivityManageService getActivityManageService(ActivityTypeEnum activityTypeEnum) {
//        ActivityManageService activityManageService = activityManageServiceMap.get(String.valueOf(activityTypeEnum.getCode()));
//        if (activityManageService == null) {
//            throw new BaseMsgException("not_find_activity_manage_service", new Object[]{activityTypeEnum});
//        }
//        return activityManageService;
//    }

    /**
     * Spring应用上下文环境
     *
     * @return
     */

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
