package com.jd.o2o.vipcart.common.plugins.tag;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.plugins.directive.utils.PaginationUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Map;

/**
 * 分页标签. Class Name: PaginationTag.java
 *
 * @param <E>
 * @author liuhuiqing DateTime 2014-8-7 下午1:26:40
 * @version 1.0
 */
public class PaginationTag<E> extends SimpleTagSupport {
    private String controller;
    private String action;
    private PageBean<E> page;
    private Map<String, String> params;

    @Override
    public void doTag() throws JspException, IOException {
        getJspContext().getOut().print(PaginationUtil.pageCompoent(controller, action, page, params));
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public PageBean<E> getPage() {
        return page;
    }

    public void setPage(PageBean<E> page) {
        this.page = page;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

}