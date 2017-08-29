package com.jd.o2o.vipcart.common.utils.json;

import com.jd.o2o.vipcart.common.utils.DateFormatUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liuhuiqing on 2015/6/3.
 */
public class JsonBinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonBinder.class);
    private ObjectMapper mapper;

    public JsonBinder(JsonSerialize.Inclusion inclusion) {
        mapper = new ObjectMapper();
        //设置输出包含的属性
        mapper.setSerializationInclusion(inclusion);
       /* mapper.getSerializationConfig().withSerializationInclusion(inclusion);
        mapper.getSerializationConfig().setSerializationInclusion(inclusion);*/
        //设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        setDateFormat(DateFormatUtils.DEFAULT_TIME_FORMAT);
    }

    /**
     * 创建输出全部属性到Json字符串的Binder.
     */
    public static JsonBinder buildNormalBinder() {
        return new JsonBinder(JsonSerialize.Inclusion.ALWAYS);
    }

    /**
     * 创建只输出非空属性到Json字符串的Binder.
     */
    public static JsonBinder buildNonNullBinder() {
        return new JsonBinder(JsonSerialize.Inclusion.NON_NULL);
    }

    /**
     * 创建只输出非空或非空字符串属性到Json字符串的Binder.
     */
    public static JsonBinder buildNonEmptyBinder() {
        return new JsonBinder(JsonSerialize.Inclusion.NON_EMPTY);
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Binder.
     */
    public static JsonBinder buildNonDefaultBinder() {
        return new JsonBinder(JsonSerialize.Inclusion.NON_DEFAULT);
    }

    /**
     * 如果JSON字符串为Null或"null"字符串,返回Null.
     * 如果JSON字符串为"[]",返回空集合.
     *
     * 如需读取集合如List/Map,且不是List<String>这种简单类型时使用如下语句:
     * List<MyBean> beanList = binder.getMapper().readValue(listString, new TypeReference<List<MyBean>>() {});
     * @param jsonString   待转的json字符串
     * @param clazz   转换的记录类
     * @param <T>   转换的记录类型
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            LOGGER.warn("parse json string error:" + jsonString, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将json字符串转List
     * @param jsonArray   待转的json字符串
     * @param <T>   转换的记录类型
     * @return
     */
    public <T> List<T> fromJsonArray(String jsonArray, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonArray)) {
            return null;
        }
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        try {
            return mapper.readValue(jsonArray, typeFactory.constructCollectionType(ArrayList.class, clazz));
        } catch (IOException e) {
            LOGGER.warn("parse json string error:" + jsonArray, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将json字符串转List
     * @param jsonArray   待转的json字符串
     * @param recordClazz   转换的记录类型
     * @param <C>   转换的集合类型
     * @return
     */
    public <C extends Collection> C fromJsonArrayBy(String jsonArray, Class recordClazz, Class collectionClazz) {
        if (StringUtils.isEmpty(jsonArray)) {
            return null;
        }
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        try {
            return mapper.readValue(jsonArray, typeFactory.constructCollectionType(collectionClazz, recordClazz));
        } catch (IOException e) {
            LOGGER.warn("parse json string error:" + jsonArray, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 如果对象为Null,返回"null".
     * 如果集合为空集合,返回"[]".
     * @param  object 被转换的对象
     */
    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            LOGGER.warn("write to json string error:" + object, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置转换日期类型的format pattern,如果不设置默认打印Timestamp毫秒数.
     * @param pattern 日式格式化字符串
     */
    public void setDateFormat(String pattern) {
        if (StringUtils.isNotBlank(pattern)) {
            DateFormat df = new SimpleDateFormat(pattern);
            mapper.setDateFormat(df);/*
			mapper.getSerializationConfig().setDateFormat(df);
			mapper.getDeserializationConfig().setDateFormat(df);*/
        }
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
}
