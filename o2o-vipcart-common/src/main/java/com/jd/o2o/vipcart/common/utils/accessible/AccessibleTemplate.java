package com.jd.o2o.vipcart.common.utils.accessible;

import java.lang.reflect.AccessibleObject;

/**
 * 反射机制中操作属性或方法访问权限设置公共处理封装
 * Created by liuhuiqing on 2015/9/11.
 */

public abstract class AccessibleTemplate<T,P extends AccessibleObject> implements Accessible<T,P> {
    private boolean isAccessible;

    @Override
    public void before(P accessibleObject) {
        this.isAccessible = accessibleObject.isAccessible();
        if(!this.isAccessible){
            accessibleObject.setAccessible(true);
        }
    }

    @Override
    public T invoke(P accessibleObject) {
        before(accessibleObject);
        T obj = doInvoke(accessibleObject);
        after(accessibleObject);
        return obj;
    }

    protected abstract T doInvoke(P accessibleObject);

    @Override
    public void after(P accessibleObject) {
        if(!this.isAccessible){
            accessibleObject.setAccessible(false);
        }
    }
}
