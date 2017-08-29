package com.jd.o2o.vipcart.common.plugins.jgoups;

import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.jgroups.util.FutureListener;
import org.jgroups.util.RspList;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 使用举例
 * Created by liuhuiqing on 2016/5/27.
 */
public class Demo extends JDispatcher {
    @Override
    protected Map<Short, Method> buildMethodLookup() {
        Map<Short, Method> methods = new HashMap<Short, Method>(8);
        try {
            methods.put((short) 1, Demo.class.getDeclaredMethod("localMethod",
                    Object.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return methods;
    }

    public String localMethod(Object p1){
        System.err.println(Thread.currentThread().getName()+"  接收到信息:"+p1);
        return System.currentTimeMillis()+"";
    }

    public static void main(String[] args) throws InterruptedException {
        final Demo demo = new Demo();
        JConfig jConfig = new JConfig();
        jConfig.setClusterName("o2o-road");
//        jConfig.setXmlFileName("jgroup/udp.xml");
        jConfig.setjGroupsInitHosts("10.13.162.63[7802]");
        jConfig.setDiscardOwnMessages(true);
        demo.setjConfig(jConfig);
        demo.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
//                    demo.sendMsg((short) 1, "hello:" + UUID.randomUUID().toString());
                    demo.sendMsgAsyn((short) 1, "hello:" + UUID.randomUUID().toString(),null,new FutureListener<RspList<Object>>() {

                                    @Override
                                    public void futureDone(Future<RspList<Object>> rspListFuture) {
                                        try {
                                            System.err.println("finished:" + JsonUtils.toJson(rspListFuture.get()));
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        Thread.sleep(1*60*60*1000);
    }

}
