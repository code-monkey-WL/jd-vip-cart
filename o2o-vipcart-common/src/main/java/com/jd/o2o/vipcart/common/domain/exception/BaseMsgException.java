package com.jd.o2o.vipcart.common.domain.exception;

import com.jd.o2o.vipcart.common.utils.MessageUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author liuhuiqing
 */
public class BaseMsgException extends RuntimeException {
    private static final long serialVersionUID = 7398568719311520034L;
    /**
     * 错误码
     */
    private String code;

    /**
     * 错误码对应的参数
     */
    private Object[] args;

    /**
     * 错误消息
     */
    private String defaultMessage;


    public BaseMsgException(String code, Object[] args, String defaultMessage) {
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public BaseMsgException(String code, Object[] args) {
        this(code, args, null);
    }

    public BaseMsgException(String code, String defaultMessage) {
        this(code, null, defaultMessage);
    }

    public BaseMsgException(String defaultMessage) {
        this(null, null, defaultMessage);
    }

    @Override
    public String getMessage() {
        String message = null;
        if (StringUtils.isNotEmpty(code)) {
            message = MessageUtils.message(code, args);
        }
        if (message == null || message.equals(code)) {
            message = defaultMessage;
        }
        return message;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().toString()).append("{code='").append(code)
                .append("',message='").append(getMessage()).append("'}").toString();
    }
}
