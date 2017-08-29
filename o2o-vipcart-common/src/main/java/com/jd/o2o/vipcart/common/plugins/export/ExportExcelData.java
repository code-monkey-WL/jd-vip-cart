package com.jd.o2o.vipcart.common.plugins.export;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

/**
 * 导出excel数据 泛型T的属性类型是string字符串类型
 *
 * @author liuhuiqing
 */
public interface ExportExcelData<T> {
    /**
     * 批量导出给定数据到excel文件,输出为压缩后的格式
     * 文件名前缀就是map对象的key，每一对key-value都会单独生成一个文件
     *
     * @param titles
     * @param contentsMap
     * @param os
     * @param maxRow
     */
    public void exportToZip(T titles, final Map<String, ? extends Collection<T>> contentsMap, final OutputStream os, final int maxRow);

    /**
     * 导出给定数据到excel文件,输出为压缩后的格式
     *
     * @param titles   要导出记录实体对应标题头
     * @param contents 要导出的记录集合
     * @param os       输出流
     * @param maxRow   每个excel的最大行数
     */
    public void exportToZip(final T titles, final Collection<T> contents, final OutputStream os, final int maxRow);

}