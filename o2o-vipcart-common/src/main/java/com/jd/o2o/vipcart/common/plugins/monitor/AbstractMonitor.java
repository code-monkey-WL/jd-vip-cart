package com.jd.o2o.vipcart.common.plugins.monitor;

import com.jd.o2o.vipcart.common.domain.SoftHashMap;
import com.jd.o2o.vipcart.common.domain.context.AppContext;
import com.jd.o2o.vipcart.common.plugins.monitor.domain.MonitorVO;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通用监控处理逻辑
 * Created by liuhuiqing on 2016/1/19.
 */
public abstract class AbstractMonitor<T> implements Monitor<T> {
    private Map<String, MonitorVO<T>> monitorMap = Collections.synchronizedMap(new SoftHashMap<String, MonitorVO<T>>()); // 监控统计
    private final SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
    private Set<String> monitorSourceSet = new HashSet<String>();

    /**
     * 统一监控处理逻辑
     *
     * @param jp
     * @return
     */
    protected MonitorVO<T> doMonitor(JoinPoint jp) {
        return doMonitor(buildMonitorVO(jp));
    }

    /**
     * 统一监控处理逻辑
     *
     * @param monitorVO
     * @return
     */
    protected MonitorVO<T> doMonitor(MonitorVO monitorVO) {
        if (monitorVO == null) {
            monitorVO = new MonitorVO();
        }
        String key = buildKey(monitorVO);
        MonitorVO<T> old = monitorMap.get(key);
        if (old == null) {
            old = monitorVO;
        }
        old.setValue(buildValue(old.getValue()));
        monitorMap.put(key, old);
        return old;
    }

    protected abstract T buildValue(T value);

    /**
     * 构建key
     *
     * @param monitorVO
     * @return
     */
    private String buildKey(MonitorVO monitorVO) {
        if (monitorVO == null) {
            monitorVO = new MonitorVO();
        }
        if (StringUtils.isEmpty(monitorVO.getAppCode())) {
            monitorVO.setAppCode(checkSource(null));
        }
        if (StringUtils.isEmpty(monitorVO.getClassName())) {
            monitorVO.setClassName(this.getClass().getName());
        }
        if (StringUtils.isEmpty(monitorVO.getMethodName())) {
            monitorVO.setMethodName("noMethodName");
        }
        if (StringUtils.isEmpty(monitorVO.getDateTime())) {
            monitorVO.setDateTime(sdf.format(new Date()));
        }
        if (StringUtils.isEmpty(monitorVO.getPrefix())) {
            monitorVO.setPrefix("noPrefix");
        }
        return new StringBuilder()
                .append(monitorVO.getAppCode())
                .append(",").append(monitorVO.getClassName())
                .append(",").append(monitorVO.getMethodName())
                .append(",").append(monitorVO.getDateTime())
                .append(",").append(monitorVO.getPrefix())
                .toString();
    }

    /**
     * 构建访问上线文对象
     *
     * @param jp
     * @return
     */
    protected AppContext buildAppContext(JoinPoint jp) {
        Object[] args = jp.getArgs();
        AppContext appContext = null;
        if (args != null && args.length > 0) {
            for (Object obj : args) {
                if (obj instanceof AppContext) {
                    appContext = (AppContext) obj;
                    break;
                }
            }
        }
        return appContext;
    }

    /**
     * 构建监控对象
     *
     * @param jp
     * @return
     */
    protected MonitorVO buildMonitorVO(JoinPoint jp) {
        AppContext appContext = buildAppContext(jp);
        Method method = MethodSignature.class.cast(jp.getSignature()).getMethod();
        MonitorVO monitorVO = new MonitorVO();
        monitorVO.setAppCode(checkSource(appContext));
        monitorVO.setClassName(method.getDeclaringClass().getName());
        monitorVO.setMethodName(method.getName());
        monitorVO.setDateTime(sdf.format(new Date()));
        return monitorVO;
    }

    /**
     * 限定操作来源数量，防止内存溢出
     *
     * @param appContext
     * @return
     */
    private String checkSource(AppContext appContext) {
        if (appContext != null && !StringUtils.isEmpty(appContext.getSource())) {
            String source = appContext.getSource();
            if (monitorSourceSet.size() < 200) {
                monitorSourceSet.add(source);
                return source;
            }
        }
        return "noSource";
    }

    @Override
    public List<MonitorVO<T>> monitor() {
        return new ArrayList<MonitorVO<T>>(monitorMap.values());
    }
}
