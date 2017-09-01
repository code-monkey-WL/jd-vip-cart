package com.jd.o2o.vipcart.service.busi.common.id;

import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.domain.constant.SequenceKeyEnum;
import com.jd.o2o.vipcart.service.common.project.ProjectContext;
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

    public static String getSkuId() {
        return String.valueOf(ProjectContext.nextSequence(SequenceKeyEnum.SKU_ID.getCode()));
    }

    public static String getSkuCode() {
        return String.valueOf(ProjectContext.nextSequence(SequenceKeyEnum.SKU_CODE.getCode()));
    }


}
