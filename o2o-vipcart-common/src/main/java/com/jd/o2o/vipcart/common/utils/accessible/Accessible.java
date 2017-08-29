package com.jd.o2o.vipcart.common.utils.accessible;

import java.lang.reflect.AccessibleObject;

/**
 * Created by liuhuiqing on 2015/9/11.
 */
public interface Accessible<T,P extends AccessibleObject> {
    public void before(P accessibleObject);
    public T invoke(P accessibleObject);
    public void after(P accessibleObject);
}
