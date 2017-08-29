package com.jd.o2o.vipcart.common.plugins.monitor.tools;

import com.jd.o2o.vipcart.common.domain.response.BaseResponseCode;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.plugins.monitor.Monitor;
import com.jd.o2o.vipcart.common.utils.SpringUtils;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 监控工具类
 * Created by liuhuiqing on 2016/3/25.
 */
public class MonitorTools {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorTools.class);

    public static ServiceResponse<String> getMonitorMsg(String[] beanIds) {
        try {
            Map<String, Monitor> monitorMap = new HashMap<String, Monitor>();
            if (beanIds == null || beanIds.length == 0
                    || (beanIds.length == 1 && StringUtils.isEmpty(beanIds[0]))) {
                monitorMap = SpringUtils.getBeanOfType(Monitor.class);
                if (monitorMap == null || monitorMap.isEmpty()) {
                    return new ServiceResponse<String>("没有找到Monitor类型的对象实例");
                }
            } else {
                for (String beanId : beanIds) {
                    Monitor monitor = SpringUtils.getBean(beanId);
                    if (monitor != null) {
                        monitorMap.put(beanId, monitor);
                    }
                }
            }
            StringBuilder stringBuilder = new StringBuilder().append("///");
            for (Map.Entry<String, Monitor> entry : monitorMap.entrySet()) {
                stringBuilder.append(entry.getKey()).append(":::").append(JsonUtils.toJson(entry.getValue().monitor())).append("///");
            }
            return new ServiceResponse<String>(stringBuilder.toString());
        } catch (Exception e) {
            LOGGER.error(String.format("查询系统监控beans=[%s]出现异常！", JsonUtils.toJson(beanIds)), e);
            return new ServiceResponse<String>(BaseResponseCode.FAILURE.getCode(), e.getMessage());
        }
    }
}
