//package com.jd.o2o.vipcart.service.common.project.advice;
//
//import com.jd.o2o.vipcart.common.domain.BaseBean;
//import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
//import com.jd.o2o.vipcart.common.plugins.monitor.AbstractAdvice;
//import com.jd.ump.profiler.CallerInfo;
//import com.jd.ump.profiler.proxy.Profiler;
//import org.aspectj.lang.JoinPoint;
//import org.slf4j.Logger;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * 跟踪方法调用栈信息
// * Created by liuhuiqing on 2017/5/23.
// */
//public class TrackingAdvice extends AbstractAdvice {
//    private static final Logger LOGGER = LoggerTrackFactory.getLogger(TrackingAdvice.class);
//    private String appName;
//
//    public static final ThreadLocal<TrackingVO> session = new ThreadLocal<TrackingVO>();
//
//    @Override
//    public void doBefore(JoinPoint jp) {
//        try {
//            String taskName = getTaskName(jp);
//            TrackingVO trackingVO = session.get();
//            if (trackingVO == null) {
//                trackingVO = new TrackingVO();
//                trackingVO.setStartTaskName(taskName);
//                trackingVO.setTrackingMap(new LinkedHashMap<String, Tracking>());
//            }
//            trackingVO.getTrackingMap().put(taskName, buildTracking(jp));
//            session.set(trackingVO);
//        } catch (Exception e) {
//            LOGGER.warn("方法调用跟踪(TrackingAdvice.doBefore)出现异常！", e);
//        }
//    }
//
//    @Override
//    public void doAfter(JoinPoint jp) {
//        try {
//            String taskName = getTaskName(jp);
//            TrackingVO trackingVO = session.get();
//            Tracking tracking = trackingVO.getTrackingMap().get(taskName);
//            tracking.setCostMillTime(System.currentTimeMillis() - tracking.getStartMillTime());
//            Profiler.registerInfoEnd(tracking.getCallerInfo());
//            if (!taskName.equals(trackingVO.getStartTaskName())) {
//                return;
//            }
//            // 汇总打印整个调用链信息
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug(buildTrackingVOString(trackingVO));
//            }
//        } catch (Exception e) {
//            LOGGER.warn("方法调用跟踪(TrackingAdvice.doAfter)出现异常！", e);
//        }
//    }
//
//    private String getTaskName(JoinPoint jp) {
//        String className = jp.getSignature().getDeclaringType().getName();
//        String methodName = jp.getSignature().getName();
//        return new StringBuilder(className).append(".").append(methodName).toString();
//    }
//
//    /**
//     * 构建方法调用耗时信息文案
//     *
//     * @param trackingVO
//     * @return
//     */
//    private String buildTrackingVOString(TrackingVO trackingVO) {
//        StringBuilder stringBuilder = new StringBuilder("方法调用跟踪耗时：");
//        for (Map.Entry<String, Tracking> entry : trackingVO.getTrackingMap().entrySet()) {
//            stringBuilder.append(entry.getKey()).append(" ==> ")
//                    .append(entry.getValue().getCostMillTime()).append(" ms").append(",    ");
//        }
//        return stringBuilder.toString();
//    }
//
//
//    /**
//     * 构建跟踪实体信息
//     *
//     * @param jp
//     * @return
//     */
//    private Tracking buildTracking(JoinPoint jp) {
//        Tracking tracking = new Tracking();
//        tracking.setClassName(jp.getSignature().getDeclaringType().getName());
//        tracking.setMethodName(jp.getSignature().getName());
//        tracking.setStartMillTime(System.currentTimeMillis());
//        tracking.setCallerInfo(Profiler.registerInfo(getTaskName(jp), appName, false, true));
//        return tracking;
//    }
//
//    public String getAppName() {
//        return appName;
//    }
//
//    public void setAppName(String appName) {
//        this.appName = appName;
//    }
//
//    class Tracking extends BaseBean {
//        private String className;
//        private String methodName;
//        private Long startMillTime;
//        private Long costMillTime;
//        private CallerInfo callerInfo;
//
//        public String getClassName() {
//            return className;
//        }
//
//        public void setClassName(String className) {
//            this.className = className;
//        }
//
//        public String getMethodName() {
//            return methodName;
//        }
//
//        public void setMethodName(String methodName) {
//            this.methodName = methodName;
//        }
//
//        public Long getStartMillTime() {
//            return startMillTime;
//        }
//
//        public void setStartMillTime(Long startMillTime) {
//            this.startMillTime = startMillTime;
//        }
//
//        public Long getCostMillTime() {
//            return costMillTime;
//        }
//
//        public void setCostMillTime(Long costMillTime) {
//            this.costMillTime = costMillTime;
//        }
//
//        public CallerInfo getCallerInfo() {
//            return callerInfo;
//        }
//
//        public void setCallerInfo(CallerInfo callerInfo) {
//            this.callerInfo = callerInfo;
//        }
//    }
//
//    class TrackingVO extends BaseBean {
//        private String startTaskName;
//        private Map<String, Tracking> trackingMap;
//
//        public String getStartTaskName() {
//            return startTaskName;
//        }
//
//        public void setStartTaskName(String startTaskName) {
//            this.startTaskName = startTaskName;
//        }
//
//        public Map<String, Tracking> getTrackingMap() {
//            return trackingMap;
//        }
//
//        public void setTrackingMap(Map<String, Tracking> trackingMap) {
//            this.trackingMap = trackingMap;
//        }
//    }
//}
