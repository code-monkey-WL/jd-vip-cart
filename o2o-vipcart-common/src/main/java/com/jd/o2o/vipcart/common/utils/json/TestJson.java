//package com.jd.o2o.vipcart.common.utils.json;
//
//import com.jd.o2o.vipcart.common.plugins.event.model.EventResult;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by liuhuiqing on 2015/10/13.
// */
//public class TestJson {
//    public static void main(String[] args){
//        int size = 100;
//        List<EventResult> list = new ArrayList<EventResult>();
//        List<String> pList = new ArrayList<String>();
//        System.out.println(JsonUtils.toJson(new EventResult(false,"msg")));
//        System.out.println(JSON.toJSONString(new EventResult(false,"msg")));
//        for(int i=0;i<size;i++){
//            EventResult eventResult = new EventResult(i%2==0,"msg"+i);
//            list.add(eventResult);
//            pList.add(JSON.toJSONString(eventResult));
//        }
//        int loop = 100;
//        String s = "";
//        long startTime = System.currentTimeMillis();
//        for(int i=0;i<loop;i++){
//           s = JsonUtils.toJson(list);
//        }
//        System.err.println("jackson to string cost time:"+(System.currentTimeMillis()-startTime));
//        startTime = System.currentTimeMillis();
//        for(int i=0;i<loop;i++){
//            JSON.toJSONString(list);
//        }
//        System.err.println("fastJson  to string cost time:"+(System.currentTimeMillis()-startTime));
//
//
//        startTime = System.currentTimeMillis();
//        for(int i=0;i<loop;i++){
//           JsonUtils.fromJson(pList.get(i),EventResult.class);
//        }
//        System.err.println("jackson  to object cost time:"+(System.currentTimeMillis()-startTime));
//
//        startTime = System.currentTimeMillis();
//        for(int i=0;i<loop;i++){
//            JSON.parseObject(pList.get(i),EventResult.class);
//        }
//        System.err.println("fastJson  to object cost time:"+(System.currentTimeMillis()-startTime));
//
//    }
//}
