package com.jd.o2o.vipcart.common.binder;

import com.jd.o2o.vipcart.common.utils.DateFormatUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * spring3.0以上版本支持字符串日期转换
 * Created by liuhuiqing on 2015/5/19.
 */
public class DateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        try {
            return new SimpleDateFormat(DateFormatUtils.getFormat(source)).parse(source);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("类型转换失败，格式[%s]的字符串不能转换成日期类型", source));
        }
    }
}
