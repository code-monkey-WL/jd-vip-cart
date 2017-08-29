package com.jd.o2o.vipcart.common.plugins.event.timingwheel;

import java.io.Serializable;

/**
 *  时间轮盘元素包装类
 *  Class Name: HashedWheelTimeout.java   
 *  @author liuhuiqing DateTime 2014-8-27 下午3:24:36    
 *  @version 1.0 
 *  @param <E>
 */
public class HashedWheelTimeout<E> implements Serializable{
	private static final long serialVersionUID = 2704822764889281665L;
	final int stopIndex;
	final long deadline;
	volatile long remainingRounds;
	private E data;

	public HashedWheelTimeout(E data, long deadline, int stopIndex,
			long remainingRounds) {
		this.data = data;
		this.deadline = deadline;
		this.stopIndex = stopIndex;
		this.remainingRounds = remainingRounds;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() > this.deadline;
	}

	public E getData() {
		return data;
	}

    @Override
    public int hashCode() {
        if(data != null){
            return data.hashCode();
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)  return true;
        if(obj == null && getData()!=null)  return false;
        if( !(obj instanceof HashedWheelTimeout)) return false;
        final HashedWheelTimeout other = (HashedWheelTimeout)obj;
        if(getData()==null && other.getData()==null) return true;
        if(getData()==null && other.getData()!=null)  return false;
        return getData().equals(other.getData());
    }

    @Override
	public String toString() {
		long currentTime = System.currentTimeMillis();
		long remaining = this.deadline - currentTime;

		StringBuilder buf = new StringBuilder(192);
		buf.append(this.getClass().getSimpleName());
		buf.append('(');

		buf.append("deadline: ");
		if (remaining > 0) {
			buf.append(remaining);
			buf.append(" ms later, ");
		} else if (remaining < 0) {
			buf.append(-remaining);
			buf.append(" ms ago, ");
		} else {
			buf.append("now, ");
		}
		return buf.append(')').toString();
	}
}
