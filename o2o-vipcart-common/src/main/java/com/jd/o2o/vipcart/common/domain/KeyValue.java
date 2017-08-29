package com.jd.o2o.vipcart.common.domain;

import java.io.Serializable;

/**
 * 键值对象
 * User: wuqingming
 * Date: 14-4-25
 * Time: 上午10:01
 * To change this template use File | Settings | File Templates.
 */
public class KeyValue<K, V> implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 键 */
    private K key;
    /** 值 */
    private V value;

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue() {
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

}
