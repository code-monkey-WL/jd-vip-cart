package com.jd.o2o.vipcart.common.plugins.spider.demo;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * Created by liuhuiqing on 2017/8/31.
 */
public class Category extends BaseBean {
    private String name;
    private String link;
    private String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
