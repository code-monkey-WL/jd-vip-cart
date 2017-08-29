package com.jd.o2o.vipcart.common.plugins.jgoups;

import org.apache.commons.collections.CollectionUtils;
import org.jgroups.Address;
import org.jgroups.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 集群成员变更时的默认响应处理（日志打印）
 * Created by liuhuiqing on 2016/5/31.
 */
public class DefaultNotification implements Notification {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNotification.class);
    @Override
    public void viewChange(View view, List<Address> mbrs_joined, List<Address> mbrs_left) {
        StringBuilder stringBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(mbrs_joined)) {
            stringBuilder.append("有新成员加入，").append(view.toString()).append(":");
            for(Address address:mbrs_joined){
                stringBuilder.append(",").append(address.toString());
            }
            LOGGER.info(stringBuilder.toString());
        }
        if (CollectionUtils.isNotEmpty(mbrs_left)) {
            stringBuilder.append("有成员退出，").append(view.toString()).append(":");
            for(Address address:mbrs_joined){
                stringBuilder.append(",").append(address.toString());
            }
            LOGGER.info(stringBuilder.toString());
        }
    }
}
