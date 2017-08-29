package com.jd.o2o.vipcart.common.plugins.log.track;

import com.jd.o2o.vipcart.common.domain.BaseBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 日志追踪实现类
 */
public class LoggerTrackFactory implements Logger {
    private Logger logger;
    private static final Map<String, Logger> LOGGER_MAP = new HashMap<String, Logger>();
    private static final ThreadLocal<Entity> LOCAL = new ThreadLocal<Entity>();

    /**
     * 初始化方法
     *
     * @param name
     */
    private LoggerTrackFactory(String name) {
        logger = LoggerFactory.getLogger(name);
    }

    /**
     * 实例化方法
     *
     * @param name
     * @return
     */
    public synchronized static Logger getLogger(String name) {
        Logger log = LOGGER_MAP.get(name);
        if (log == null) {
            log = new LoggerTrackFactory(name);
            LOGGER_MAP.put(name, log);
        }
        return log;
    }

    /**
     * 实例化方法
     *
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * 设置日志跟踪信息
     * 负责设置的程序必须负责还原!
     *
     * @return 设置成功返回true.
     */
    public static boolean setTrackLog(TrackBean trackBean) {
        boolean isSuccess = false;
        try {
            if (LOCAL.get() != null) {
                return isSuccess;
            }
            String key = trackBean.getKey();
            if (StringUtils.isEmpty(key)) {
                key = String.valueOf(Math.abs(UUID.randomUUID().hashCode()));
            }
            Entity entity = new Entity();
            entity.setKey(key);
            entity.setNum(0);
            LOCAL.set(entity);
            isSuccess = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 还原日志跟踪信息
     * 必须由设置的程序来还原
     *
     * @return
     */
    public static boolean resetTrackLog() {
        boolean isSuccess = false;
        try {
            LOCAL.set(null);
            isSuccess = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 修改日志描述,增加前缀
     *
     * @param format
     */
    private String addHeader(String format) {
        Entity entity = LOCAL.get();
        if (entity == null) {
            return format;
        }
        entity.setNum(entity.getNum() + 1);
        LOCAL.set(entity);
        return new StringBuilder().append("[").append(entity.getKey()).append("-").append(entity.getNum())
                .append("]").append(format).toString();
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(addHeader(msg));
    }

    @Override
    public void trace(String format, Object arg) {
        logger.trace(addHeader(format), arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(addHeader(format), arg1, arg2);
    }

    @Override
    public void trace(String format, Object[] argArray) {
        logger.trace(addHeader(format), argArray);
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(addHeader(msg), t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        logger.trace(marker, addHeader(msg));
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        logger.trace(marker, addHeader(format), arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        logger.trace(marker, addHeader(format), arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object[] argArray) {
        logger.trace(marker, addHeader(format), argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        logger.trace(marker, addHeader(msg), t);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(addHeader(msg));
    }

    @Override
    public void debug(String format, Object arg) {
        logger.debug(addHeader(format), arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(addHeader(format), arg1, arg2);
    }

    @Override
    public void debug(String format, Object[] argArray) {
        logger.debug(addHeader(format), argArray);
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(addHeader(msg), t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        logger.debug(marker, addHeader(msg));
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        logger.debug(marker, addHeader(format), arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        logger.debug(marker, addHeader(format), arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object[] argArray) {
        logger.debug(marker, addHeader(format), argArray);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        logger.debug(marker, addHeader(msg), t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        logger.info(addHeader(msg));
    }

    @Override
    public void info(String format, Object arg) {
        logger.info(addHeader(format), arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(addHeader(format), arg1, arg2);
    }

    @Override
    public void info(String format, Object[] argArray) {
        logger.info(addHeader(format), argArray);
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(addHeader(msg), t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        logger.info(marker, addHeader(msg));
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        logger.info(marker, addHeader(format), arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        logger.info(marker, addHeader(format), arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object[] argArray) {
        logger.info(marker, addHeader(format), argArray);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        logger.info(marker, addHeader(msg), t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(addHeader(msg));
    }

    @Override
    public void warn(String format, Object arg) {
        logger.warn(addHeader(format), arg);
    }

    @Override
    public void warn(String format, Object[] argArray) {
        logger.warn(addHeader(format), argArray);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(addHeader(format), arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(addHeader(msg), t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        logger.warn(marker, addHeader(msg));
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        logger.warn(marker, format, addHeader(format));
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        logger.warn(marker, addHeader(format), arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object[] argArray) {
        logger.warn(marker, addHeader(format), argArray);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        logger.warn(marker, addHeader(msg), t);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(addHeader(msg));
    }

    @Override
    public void error(String format, Object arg) {
        logger.error(addHeader(format), arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        logger.error(addHeader(format), arg1, arg2);
    }

    @Override
    public void error(String format, Object[] argArray) {
        logger.error(addHeader(format), argArray);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(addHeader(msg), t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        logger.error(marker, addHeader(msg));
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        logger.error(marker, addHeader(format), arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        logger.error(marker, addHeader(format), arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object[] argArray) {
        logger.error(marker, addHeader(format), argArray);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        logger.error(marker, addHeader(msg), t);
    }

    static class Entity extends BaseBean {
        private String key;
        private Integer num;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }
}
