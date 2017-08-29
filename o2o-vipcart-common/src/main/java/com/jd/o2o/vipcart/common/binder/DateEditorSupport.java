package com.jd.o2o.vipcart.common.binder;

import com.jd.o2o.vipcart.common.utils.DateFormatUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期类型转换器
 * Created by liuhuiqing on 2015/3/11.
 */
public class DateEditorSupport extends PropertyEditorSupport {
    private final DateFormat dateFormat;

    private final boolean allowEmpty;

    private final int exactDateLength;

    public DateEditorSupport() {
        this.dateFormat = new SimpleDateFormat(DateFormatUtils.DATE_MODEL_2);
        this.allowEmpty = true;
        this.exactDateLength = -1;
    }

    public DateEditorSupport(DateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }

    public DateEditorSupport(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            setValue(null);
        } else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
            throw new IllegalArgumentException(
                    "Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
        } else {
            try {
//                setValue(this.dateFormat.parse(text));
                setValue(new SimpleDateFormat(DateFormatUtils.getFormat(text)).parse(text));
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
            }
        }
    }

    /**
     * Format the Date as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? this.dateFormat.format(value) : "");
    }
}
