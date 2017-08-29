package com.jd.o2o.vipcart.common.utils.json;
import java.util.Collection;
import java.util.List;

/**
 * Created by liuhuiqing on 2015/6/3.
 */
public class JsonUtils {
    private static JsonBinder jsonBinder;

    static {
        jsonBinder = JsonBinder.buildNonNullBinder();
    }

    /**
     * 如果JSON字符串为Null或"null"字符串,返回Null.
     * 如果JSON字符串为"[]",返回空集合.
     * @param jsonString   待转的json字符串
     * @param clazz   转换的记录类
     * @param <T>   转换的记录类型
     * @return
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return jsonBinder.fromJson(jsonString, clazz);
    }

    /**
     * 将json字符串转List
     * @param jsonArray   待转的json字符串
     * @param <T>   转换的记录类型
     * @return
     */
    public static <T> List<T> fromJsonArray(String jsonArray, Class<T> clazz) {
        return jsonBinder.fromJsonArray(jsonArray, clazz);
    }

    /**
     * 将json字符串转List
     * @param jsonArray   待转的json字符串
     * @param recordClazz   转换的记录类型
     * @param <C>   转换的集合类型
     * @return
     */
    public static <C extends Collection> C fromJsonArrayBy(String jsonArray, Class recordClazz, Class collectionClazz) {
        return jsonBinder.fromJsonArrayBy(jsonArray, recordClazz, collectionClazz);
    }

    /**
     * 如果对象为Null,返回"null".
     * 如果集合为空集合,返回"[]".
     * @param  object 被转换的对象
     */
    public static String toJson(Object object) {
        return jsonBinder.toJson(object);
    }

    /**
     * 设置转换日期类型的format pattern,如果不设置默认打印Timestamp毫秒数.
     * @param pattern 日式格式化字符串
     */
    public static void setDateFormat(String pattern) {
        jsonBinder.setDateFormat(pattern);
    }
}
