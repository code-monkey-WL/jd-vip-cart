package com.jd.o2o.vipcart.common.plugins.cache.guava;

import com.google.common.cache.CacheStats;
import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.utils.SpringUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存管理工具类
 * Created by liuhuiqing on 2015/7/29.
 */
public class GuavaCacheManager {
    //保存一个Map: cacheName -> cache Object，以便根据cacheName获取Guava cache对象  
    private static Map<String, ? extends AbstractGuavaCache> cacheNameToObjectMap = null;

    /**
     * 获取所有AbstractGuavaCache子类的实例，即所有的Guava Cache对象
     *
     * @return
     */

    @SuppressWarnings("unchecked")
    private static Map<String, ? extends AbstractGuavaCache> getCacheMap() {
        if (cacheNameToObjectMap == null) {
            cacheNameToObjectMap = SpringUtils.getBeanOfType(AbstractGuavaCache.class);
        }
        return cacheNameToObjectMap;
    }

    /**
     * 根据cacheName获取cache对象
     *
     * @param cacheName
     * @return
     */
    private static AbstractGuavaCache getCacheByName(String cacheName) {
        return getCacheMap().get(cacheName);
    }

    /**
     * 获取所有缓存的名字（即缓存实现类的名称）
     *
     * @return
     */
    public static Set<String> getCacheNames() {
        return getCacheMap().keySet();
    }

    /**
     * 返回所有缓存的统计数据
     *
     * @return List<Map<统计指标，统计数据>>
     */
    public static ArrayList<Map<String, Object>> getAllCacheStats() {

        Map<String, ? extends Object> cacheMap = getCacheMap();
        List<String> cacheNameList = new ArrayList<String>(cacheMap.keySet());
        Collections.sort(cacheNameList);//按照字母排序

        //遍历所有缓存，获取统计数据  
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (String cacheName : cacheNameList) {
            list.add(getCacheStatsToMap(cacheName));
        }

        return list;
    }

    /**
     * 返回一个缓存的统计数据
     *
     * @param cacheName
     * @return Map<统计指标，统计数据>
     */
    private static Map<String, Object> getCacheStatsToMap(String cacheName) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        AbstractGuavaCache cache = getCacheByName(cacheName);
        CacheStats cs = cache.getCache().stats();
        NumberFormat percent = NumberFormat.getPercentInstance(); // 建立百分比格式化用
        percent.setMaximumFractionDigits(1); // 百分比小数点后的位数  
        map.put("cacheName", cacheName);
        map.put("size", cache.getCache().size());
        map.put("maximumSize", cache.getMaximumSize());
        map.put("survivalDuration", cache.getExpireAfterWriteDuration());
        map.put("hitCount", cs.hitCount());
        map.put("hitRate", percent.format(cs.hitRate()));
        map.put("missRate", percent.format(cs.missRate()));
        map.put("loadSuccessCount", cs.loadSuccessCount());
        map.put("loadExceptionCount", cs.loadExceptionCount());
        map.put("totalLoadTime", cs.totalLoadTime() / 1000000);       //ms
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (cache.getResetTime() != null) {
            map.put("resetTime", df.format(cache.getResetTime()));
        }
        return map;
    }

    /**
     * 根据cacheName清空缓存数据
     *
     * @param cacheName
     */
    public static void resetCache(String cacheName) {
        AbstractGuavaCache cache = getCacheByName(cacheName);
        cache.getCache().invalidateAll();
        cache.setResetTime(new Date());
    }

    /**
     * 分页获得缓存中的数据
     *
     * @param pageParams
     * @return
     */
    public static PageBean<Object> queryDataByPage(String cacheName, PageBean<Object> pageParams) {
        PageBean<Object> data = new PageBean<Object>();

        AbstractGuavaCache cache = getCacheByName(cacheName);
        ConcurrentMap<Object, Object> cacheMap = cache.getCache().asMap();
        data.setTotalCount(cacheMap.size());

        //遍历  
        Iterator<Map.Entry<Object, Object>> entries = cacheMap.entrySet().iterator();
        int startPos = (int) ((pageParams.getPageNo() - 1) * pageParams.getPageSize());
        int endPos = startPos + pageParams.getPageSize();
        int i = 0;
        Map<Object, Object> resultMap = new LinkedHashMap<Object, Object>();
        while (entries.hasNext()) {
            Map.Entry<Object, Object> entry = entries.next();
            if (i >= endPos) {
                break;
            }

            if (i >= startPos) {
                resultMap.put(entry.getKey(), entry.getValue());
            }
            i++;
        }
        List<Object> resultList = new ArrayList<Object>();
        resultList.add(resultMap);
        data.setResultList(resultList);
        return data;
    }
}
