package com.jd.o2o.vipcart.common.plugins.event.timingwheel;

import java.util.Iterator;

/**
 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
 * @author <a href="http://gleamynode.net/">Trustin Lee</a>
 * @version $Rev: 2080 $, $Date: 2010-01-26 18:04:19 +0900 (Tue, 26 Jan 2010) $
 */
public interface ReusableIterator<E> extends Iterator<E> {
    void rewind();
}
