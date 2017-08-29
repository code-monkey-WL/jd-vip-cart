package com.jd.o2o.vipcart.common.binder;

import com.jd.o2o.vipcart.common.domain.context.AppContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * 属性校验
 * Created by liuhuiqing on 2015/3/12.
 */
public class AppContextValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AppContext.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "source", null, "source is empty.");
        AppContext appContext = (AppContext) target;
        if (StringUtils.isEmpty(appContext.getSource()))
            errors.rejectValue("source", null, "Password is empty.");
    }
}
