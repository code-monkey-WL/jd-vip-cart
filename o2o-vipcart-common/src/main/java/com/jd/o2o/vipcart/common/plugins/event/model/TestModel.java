package com.jd.o2o.vipcart.common.plugins.event.model;

import com.jd.o2o.vipcart.common.domain.BaseBean;

public class TestModel extends BaseBean {
	private static final long serialVersionUID = -3041731326205180449L;
	private String test;

	public TestModel(String test) {
		super();
		this.test = test;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	
}
