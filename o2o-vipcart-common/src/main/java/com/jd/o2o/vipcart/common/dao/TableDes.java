/**
 *
 */
package com.jd.o2o.vipcart.common.dao;

import java.lang.annotation.*;

/**
 * ibatis 命名空间和表名注解，用于Dao层抽象类公共方法的实现
 * Class Name: TableDes.java
 *
 * @author liuhuiqing DateTime 2014-7-7 下午2:29:13
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface TableDes {
    /**
     * 命名空间
     * @return
     */
    public String nameSpace() default "";

    /**
     * 表名
     * @return
     */
    public String tableName() default "";

    /**
     * 路由字段
     * @return
     */
    public String routerKey() default "";

    /**
     * 路由分片数
     * @return
     */
    public int shards() default 0;
}