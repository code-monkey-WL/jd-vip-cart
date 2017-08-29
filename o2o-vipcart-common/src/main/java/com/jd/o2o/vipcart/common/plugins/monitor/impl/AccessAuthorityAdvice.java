package com.jd.o2o.vipcart.common.plugins.monitor.impl;

import com.jd.o2o.vipcart.common.domain.context.AppContext;
import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.plugins.monitor.AbstractAdvice;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;

/**
 * 访问授权验证
 * Created by liuhuiqing on 2016/1/15.
 */
public class AccessAuthorityAdvice extends AbstractAdvice {
    @Override
    public void doBefore(JoinPoint jp) {
        Object[] args = jp.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        for (Object obj : args) {
            if (obj instanceof AppContext) {
                AppContext appContext = (AppContext) obj;
                if (checkAppContext(appContext)) {
                    return;
                }
                throw new BaseMsgException("授权校验不通过");
            }
        }
        String paramTypeString = jp.toLongString();
        String appContextName = new StringBuffer(AppContext.class.getName()).append(",").toString();
        if (paramTypeString.indexOf(appContextName) > -1) {
            throw new BaseMsgException("授权校验不通过");
        }
    }
    /**
     * 入参校验
     *
     * @param appContext
     * @return
     */
    private boolean checkAppContext(AppContext appContext) {
        return appContext != null && StringUtils.isNotEmpty(appContext.getSource());
    }
}
