package com.jd.o2o.vipcart.service.busi.common.id;

import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.JCache;
import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.utils.DateFormatUtils;
import com.jd.o2o.vipcart.domain.constant.SequenceKeyEnum;
import com.jd.o2o.vipcart.service.common.project.ProjectContext;
import com.jd.o2o.vipcart.service.common.project.ProjectFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import java.util.Random;

/**
 * ID生成器
 * Created by liuhuiqing on 2017/5/17.
 */
public class IDGenerator {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(IDGenerator.class);
    public static final String SPLIT_LINE = "-";
    private final static String[] strDigits = {"1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "H", "K", "M", "R", "W", "X", "Y"};
    private static Random random = new Random();
    private static JCache redisCache;

    /**
     * 活动编号的后三位是对应的活动类型
     *
     * @param activityType
     * @return
     */
    public static String getActivityCode(Integer activityType) {
        StringBuilder suffixBuilder = new StringBuilder()
                .append(ProjectContext.nextSequence(SequenceKeyEnum.ACTIVITY_CODE.getCode()));
        if (activityType < 10) {
            suffixBuilder.append("00");
        } else if (activityType < 100) {
            suffixBuilder.append("0");
        }
        suffixBuilder.append(activityType).append(random.nextInt(10));
        return suffixBuilder.toString();
    }

    /**
     * 根据活动编号获得活动类型
     *
     * @param activityCode
     * @return
     */
    public static Integer getActivityType(String activityCode) {
        int length = activityCode.length();
        return Integer.valueOf(activityCode.substring(length - 4, length - 1));
    }

    /**
     * 新老系统并行运行时，调用次方法（时间+领取次数+"-"+vipcartId#userPin.hashCode）
     *
     * @param userPin
     * @param vipcartId
     * @return 形如：:201708181-987654321#96354
     */
    public static String getConsumeCode(String userPin, Long vipcartId) {
        int hashCode = userPin.hashCode();
        if (Integer.MIN_VALUE == hashCode) { //处理极值
            hashCode = Integer.MAX_VALUE;
        } else {
            hashCode = Math.abs(hashCode);
        }
        return new StringBuilder()
                .append(getConsumeCodeIndex(userPin, vipcartId))
                .append(SPLIT_LINE).append(vipcartId)
                .append("#").append(hashCode).toString();
    }

    /**
     * 生成ConsumeCode唯一索引
     *
     * @param userPin
     * @param vipcartId
     * @return
     */
    private static String getConsumeCodeIndex(String userPin, Long vipcartId) {
        long index = 1;
        DateTime dateTime = new DateTime();
        String prefix = DateFormatUtils.format(dateTime.toDate(), DateFormatUtils.DATE_MODEL_11);
        try {
            String key = new StringBuilder()
                    .append("t_").append(prefix)
                    .append(SPLIT_LINE).append(vipcartId)
                    .append(SPLIT_LINE).append(userPin).toString();
            if (redisCache == null) {
                redisCache = ProjectFactory.getApplicationContext().getBean(JCache.class);
                if (redisCache == null) {
                    throw new BaseMsgException("spring上下文中没有找到redisCache实例对象！");
                }
            }
            index = redisCache.incrBy(key, 1, dateTime.plusDays(7).toDate());
        } catch (Throwable e) {
            LOGGER.error(String.format("用户userPin=[%s]领取优惠券vipcartId=[%s]", userPin, vipcartId));
        }
        return new StringBuilder().append(prefix).append(index).toString();
    }

    /**
     * 新code生成策略（新老系统并行运行时，暂不调用次方法）
     * （vipcartId-randomNum#userPin.hashCode）
     *
     * @param userPin
     * @param vipcartId
     * @return
     */
    public static String getConsumeCodeNew(String userPin, Long vipcartId) {
        int hashCode = userPin.hashCode();
        if (Integer.MIN_VALUE == hashCode) { //处理极值
            hashCode = Integer.MAX_VALUE;
        } else {
            hashCode = Math.abs(hashCode);
        }
        return new StringBuilder()
                .append(vipcartId)
                .append(SPLIT_LINE).append(ProjectContext.nextSequence(SequenceKeyEnum.CONSUME_CODE.getCode()))
                .append("#").append(hashCode).toString();
    }

    /**
     * 从用户领取优惠券的唯一标识中提取优惠券编号(暂时没法通用)
     *
     * @param consumeCode
     * @return
     */
    @Deprecated
    public static Long getvipcartIdFromConsumeCode(String consumeCode) {
        return Long.valueOf(consumeCode.split(SPLIT_LINE)[0]);
    }

    /**
     * 领券分组标识（领券包用一个值）
     *
     * @return
     */
    public static Long getGrabActivityGroupId() {
        return ProjectContext.nextSequence(SequenceKeyEnum.GROUP_ID.getCode());
    }

}
