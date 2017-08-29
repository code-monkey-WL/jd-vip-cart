/**
 *
 */
package com.jd.o2o.vipcart.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 继承重写PrintWriter类的toString方法
 * Class Name: StringPrintWriter.java
 *
 * @author liuhuiqing DateTime 2014-6-24 下午6:20:51
 * @version 1.0
 */
public class StringPrintWriter extends PrintWriter {

    public StringPrintWriter() {
        super(new StringWriter());
    }

    public StringPrintWriter(int initialSize) {
        super(new StringWriter(initialSize));
    }

    public String getString() {
        flush();
        return ((StringWriter) this.out).toString();
    }

    public String getExceptionMsg(Throwable e) {
        e.printStackTrace(this);
        return getString();
    }

    @Override
    public String toString() {
        return getString();
    }
}
