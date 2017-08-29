package com.jd.o2o.vipcart.common.plugins.jgoups;

import com.jd.o2o.vipcart.common.domain.exception.O2OException;
import org.jgroups.Message;
import org.jgroups.blocks.*;
import org.jgroups.util.FutureListener;
import org.jgroups.util.NotifyingFuture;
import org.jgroups.util.RspList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 构建RpcDispatcher
 * Created by liuhuiqing on 2016/5/30.
 */
public abstract class JDispatcher extends JGroup{
    private static final Logger LOGGER = LoggerFactory.getLogger(JDispatcher.class);
    // 用于触发远程方法(扮演客户端角色)，同时也可以被其它成员调用(扮演服务器端角色)
    private RpcDispatcher dispatcher = null;
    private final static long MILL_TIME_OUT = 10000;

    protected abstract Map<Short, Method> buildMethodLookup();

    @Override
    public synchronized void start() {
        if(dispatcher != null){
            return;
        }
        super.start();
        dispatcher = new RpcDispatcher(this.getjChannel(), this, this, this);
        dispatcher.setMethodLookup(new MethodLookup() {
            public Method findMethod(short id) {
                return buildMethodLookup().get(id);
            }
        });
    }

    @Override
    public synchronized void stop() {
        if (dispatcher != null) {
            dispatcher.stop();
            dispatcher = null;
        }
        super.stop();
    }

    public <T> RspList<T> sendMsg(short methodId,Object msg) {
        return sendMsg(methodId,msg,null);
    }

    public <T> RspList<T> sendMsg(short methodId,Object msg,Message.Flag[] flags) {
        return sendMsg(methodId,msg,flags,ResponseMode.GET_MAJORITY, MILL_TIME_OUT);
    }

    /**
     * 同步消息发送（调用线程将会被阻塞，直到收到响应为止。）
     * @param methodId 接收处理消息的方法编号
     * @param msg 消息对象本身
     * @param flags 消息发送设置：
     *              Message.Flag.DONT_BUNDLE 无需消息合并，立即发送（默认会等待合并）
     *              Message.Flag.RSVP 需要等待接收确认消息
     *              Message.Flag.NO_FC 忽略消息发送顺序
     *              Message.Flag.OOB 直接地派发给线程池，因为OOB请求的顺序不重要，但是常规的请求应该被添加到一个队列中，按照次序处理
     *              Message.Flag.SCOPED 只能用于常规消息(非OOB消息)，在scopes之间进行并发递交，然后在给定的scope之内进行排序
     *                                  在3.3版中，SCOPE被MessageDispatcher和RpcDispatcher中的异步触发取代。在4.x版本中，SCOPE可能会被删除
     * @param responseMode 调用响应模式
     * @param millTimeOut 模块调用最长时间，0代表需要接收所有的响应(mode = GET_ALL时)
     * @param <T>
     * @return
     */
    public <T> RspList<T> sendMsg(short methodId,Object msg,Message.Flag[] flags,ResponseMode responseMode,long millTimeOut) {
        try {
            if(responseMode == null){
                responseMode = ResponseMode.GET_MAJORITY;
            }
            RequestOptions requestOptions =new RequestOptions(responseMode, millTimeOut);
            requestOptions.setFlags(flags);
            MethodCall call = new MethodCall(methodId, msg);
            return getDispatcher().callRemoteMethods(null, call, requestOptions);
        } catch (Exception e) {
            String errMsg = String.format("消息同步出现异常：{} sendMsg({}) failed",methodId,msg);
            LOGGER.error(errMsg,e);
            throw new O2OException(errMsg, e);
        }
    }

    public void sendMsgAsyn(short methodId,Object msg) {
        sendMsgAsyn(methodId,msg,null,null);
    }

    /**
     * 异步消息发送（允许调用者立即返回，并在之后抓取调用结果）
     * @param methodId
     * @param msg
     * @param flags
     * @param listener 当调用结果可用时，绑定通知处理事件
     * @param <T>
     */
    public <T> void sendMsgAsyn(short methodId,Object msg,Message.Flag[] flags,FutureListener<RspList<T>> listener) {
        try {
            RequestOptions requestOptions = RequestOptions.ASYNC();
            requestOptions.setFlags(flags);
            MethodCall call = new MethodCall(methodId, msg);
            NotifyingFuture<RspList<T>> future = getDispatcher().callRemoteMethodsWithFuture(null,call,requestOptions);
            future.setListener(listener);
        } catch (Exception e) {
            String errMsg = String.format("消息同步出现异常：{} sendMsgAsyn({}) failed",methodId,msg);
            LOGGER.error(errMsg,e);
            throw new O2OException(errMsg, e);
        }
    }

    public RpcDispatcher getDispatcher() {
        if(dispatcher != null){
            return dispatcher;
        }
        synchronized (this){
            if(dispatcher == null){
                start();
            }
        }
        return dispatcher;
    }
}
