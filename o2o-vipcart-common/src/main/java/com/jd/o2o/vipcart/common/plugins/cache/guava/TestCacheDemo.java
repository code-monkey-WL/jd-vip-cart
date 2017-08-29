package com.jd.o2o.vipcart.common.plugins.cache.guava;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuhuiqing on 2015/7/29.
 */
public class TestCacheDemo {
    private static int a = 0;
    private static int expireTime = 2;

    public static void main(String[] args) throws InterruptedException {
        String cacheNameKey = "test";
        GuavaLoaderCache<Object,Object> dc = new GuavaLoaderCache<Object,Object>(){
            {
                this.timeUnit = TimeUnit.SECONDS;
                //最大缓存条数
                setMaximumSize(10000);
                //设置超时时间
                setExpireAfterWriteDuration(expireTime);
            }

            @Override
            protected Object fetchData(Object key) {
                return a++;
            }
        }.named(cacheNameKey);
        System.out.println(dc.fromCache("a"));
        System.out.println(dc.fromCache("b"));
        System.out.println(dc.fromCache("c"));
        System.out.println(dc.fromCache("d"));
        Thread.sleep(expireTime*1000+1);

        System.out.println("*************计数归零**************");

        GuavaLoaderCache cache = GuavaLoaderCache.getCacheMap().get(cacheNameKey);
        if(cache == null){
            cache = new GuavaLoaderCache<Object,Object>(){
                {
                    this.timeUnit = TimeUnit.SECONDS;
                    //最大缓存条数
                    setMaximumSize(10000);
                    //设置超时时间
                    setExpireAfterWriteDuration(expireTime);
                }

                @Override
                protected Object fetchData(Object key) {
                    return a++;
                }
            }.named(cacheNameKey);
        }
        System.out.println(cache.fromCache("a"));
        System.out.println(cache.fromCache("b"));
        System.out.println(cache.fromCache("c"));
        System.out.println(cache.fromCache("d"));
        Thread.sleep(expireTime*1000+1);
/**############################################################################################**/
        GuavaCallableCache gc= GuavaCallableCache.getCacheMap().get(cacheNameKey);
        if(gc == null){
            gc = new GuavaCallableCache<Object,Object,Object>(){
                {
                    //最大缓存条数
                    setMaximumSize(10000);
                    //设置超时时间
                    setExpireAfterWriteDuration(expireTime);
                }

                @Override
                protected Object fetchData(Object key) {
                    return a++;
                }
            }.named(cacheNameKey);
        }
        System.out.println(gc.fromCache("key1", null));
        System.out.println(gc.fromCache("key2", null));
        System.out.println(gc.fromCache("key3", null));
        System.out.println(gc.fromCache("key4", null));
//        Thread.sleep(expireTime*1000+1);

        System.out.println("*************计数归零**************");
        System.out.println(gc.fromCache("key1",null));
        System.out.println(gc.fromCache("key2", null));
        System.out.println(gc.fromCache("key3", null));
        System.out.println(gc.fromCache("key4", null));
    }
}
