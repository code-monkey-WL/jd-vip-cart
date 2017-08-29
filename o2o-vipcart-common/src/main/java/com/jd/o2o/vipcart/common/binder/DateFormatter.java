package com.jd.o2o.vipcart.common.binder;

import com.jd.o2o.vipcart.common.utils.DateFormatUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.Formatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * spring3.0以上版本支持字符串日期转换
 * Created by liuhuiqing on 2015/5/19.
 */
public class DateFormatter implements Formatter<Date> {
    @Override
    public String print(Date object, Locale locale) {
        return null;
    }

    @Override
    public Date parse(String text, Locale locale) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            return new SimpleDateFormat(DateFormatUtils.getFormat(text)).parse(text);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("类型转换失败，格式[%s]的字符串不能转换成日期类型", text));
        }
    }
}
