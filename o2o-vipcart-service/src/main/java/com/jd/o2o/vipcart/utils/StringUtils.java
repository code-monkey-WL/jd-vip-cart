package com.jd.o2o.vipcart.utils;

import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by lijin on 17/6/23.
 * 字符串相关工具类
 */
public class StringUtils {

    private static final Logger LOGGER = LoggerTrackFactory.getLogger(StringUtils.class);

    /**
     * 校验对象是否为空
     * @param param
     * @return
     */
    public static boolean isEmpty(Object param){
        if(param == null){
            return true;
        } else if(param instanceof String){
            return "".equals((String)param);
        } else if(param instanceof List){
            return ((List)param).size() == 0;
        } else if(param instanceof Long){
            return (Long)param <= 0l;
        }
        return false;
    }

    /**
     * 校验一组对象是否为空
     * @param param
     * @return
     */
    public static String checkEmpty(Map<String, Object> param) {
        if(null == param) {
            return "参数不能为空！";
        }
        for (Map.Entry<String, Object> map : param.entrySet()) {
            if (isEmpty(map.getValue())) {
                LOGGER.info("{}不能为空！", map.getKey());
                return String.format(String.format("[%s]不能为空！",map.getKey()));
            }
        }
        return "";
    }

    /**
     * 处理版本号为int类型
     * @param version
     * @return
     */
    public static Integer getAppVersion(String version) {
        if(isEmpty(version)) {
            return 0;
        }
        return Integer.valueOf(version.replace(".", ""));
    }
}
