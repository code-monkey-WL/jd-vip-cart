package com.jd.o2o.vipcart.common.plugins.export.excel;

import com.jd.o2o.vipcart.common.plugins.export.ExportExcelData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 大数据量导出 抽象类
 * Created by liuhuiqing on 2015/2/10.
 */
public abstract class AbstractExportExcelData<T> implements ExportExcelData<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExportExcelData.class);
    /**
     * 每个文件的最大行数 超过请求按默认算
     */
    public static final int MAXROWS = 1000000;
    public static final String DEFAULT_DIR = "/export/Logs/file.o2o.jd.com/";

    /**
     * 临时文件前缀
     *
     * @return
     */
    protected abstract String getPrefix();

    /**
     * 临时文件后缀
     *
     * @return
     */
    protected abstract String getSuffix();

    /**
     * 删除临时文件
     *
     * @param fileList
     */
    protected void cleanTempFile(List<File> fileList) {
        for (File file : fileList) {
            file.delete();
        }
    }

    /**
     * 数据输出
     *
     * @param data
     * @param fos
     * @throws java.io.IOException
     */
    protected void writeToOutputStream(String data, FileOutputStream fos)
            throws IOException {
        IOUtils.write(data, fos, "UTF-8");
    }

    /**
     * 文件开头的写入
     *
     * @param fos
     * @throws java.io.IOException
     */
    protected abstract void writeHeaderToOutputStream(FileOutputStream fos)
            throws IOException;

    /**
     * 文件结尾的写入
     *
     * @param fos
     */
    protected abstract void writeFooterToOutputStream(FileOutputStream fos)
            throws IOException;

    /**
     *  一行数据的写入
     * @param titles
     * @param content
     * @param fos
     * @throws Exception
     */
    protected abstract void writeOneRowToOutputStream(T titles, T content, FileOutputStream fos) throws Exception;

    /**
     * 写数据标题
     *
     * @param titles
     * @param fos
     * @throws Exception
     */
    protected abstract void writeTitleToOutputStream(T titles, FileOutputStream fos) throws Exception;

    /**
     * 生成文件前缀规则
     * @param key
     * @return
     */
    protected String getFileNamePrefix(String key){
        String prefix = getPrefix();
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isNotEmpty(prefix)){
            sb.append(prefix).append("_");
        }
        return sb.append(key).append("_").toString();
    }

    /**
     * 打包 压缩成zip
     *
     * @param os       压缩输出流
     * @param fileList 被压缩的文件列表
     * @throws java.io.IOException
     */
    protected void doZip(OutputStream os, List<File> fileList)
            throws IOException {
        if (fileList != null && fileList.size() > 0) {
            byte[] buf = new byte[1024];
            ZipOutputStream out = new ZipOutputStream(os);
            for (File file : fileList) {
                FileInputStream in = new FileInputStream(file);
                out.putNextEntry(new ZipEntry(file.getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        }
    }

    /**
     * 获取单个文件最大行数
     *
     * @param maxRow
     * @return
     */
    protected int getMaxRows(int maxRow) {
        return maxRow > 0 && maxRow < MAXROWS ? maxRow : MAXROWS;
    }

    @Override
    public void exportToZip( T titles, final Map<String,? extends Collection<T>> contentsMap, final OutputStream os, final int maxRow) {
        // 每个文件最大行数
        final int max = getMaxRows(maxRow);
        // 文件收集器
        List<File> fileList = new ArrayList<File>();
        FileOutputStream fos = null;
        try {
            for(Map.Entry<String,? extends Collection<T>> entry:contentsMap.entrySet()){
                // 行数记录器
                int i = 0;
                // 临时文件
                File file = null;
                String key = entry.getKey();
                Collection<T> contents = entry.getValue();
                //内容为空也生成只有表头的文件
                if(CollectionUtils.isEmpty(contents)){
                    contents = new ArrayList<T>();
                    contents.add(titles);
                }
                for (Iterator<T> iterator = contents.iterator(); iterator.hasNext(); ) {
                    T content = iterator.next();
                    if (content == null) {
                        continue;
                    }
                    // 达到最大行数 或者 新建的 创建新文件
                    if (i == max || i == 0) {
                        // 如果不是新文件 为这个文件写入文件尾
                        if (file != null) {
                            // 写文件尾
                            writeFooterToOutputStream(fos);
                            // 关闭流
                            IOUtils.closeQuietly(fos);
                        }
                        // 创建临时文件
                        file = File.createTempFile(getFileNamePrefix(key), getSuffix(), new File(DEFAULT_DIR));
                        // 打开流
                        fos = FileUtils.openOutputStream(file);
                        // 放进收集器里
                        fileList.add(file);
                        // 写文件头
                        writeHeaderToOutputStream(fos);
                        // 数据区标题栏
                        writeTitleToOutputStream(titles, fos);
                        i = 0;
                    }
                    i++;
                    ////排除空文件生成两行标题的情况
                    if(titles!=content){
                        // 写实际一行数据
                        writeOneRowToOutputStream(titles,content, fos);
                    }
                }
                if (file != null) {
                    // 写文件尾
                    writeFooterToOutputStream(fos);
                    // 关闭流
                    IOUtils.closeQuietly(fos);
                }
            }
            // 打包
            doZip(os, fileList);
        } catch (Exception e) {
            // io异常
            LOGGER.error("写文件出现异常",e);
        } finally {
            IOUtils.closeQuietly(fos);
            // 清空临时文件
            cleanTempFile(fileList);
            fileList.clear();
            fileList = null;
        }
    }

    @Override
    public void exportToZip( T titles, final Collection<T> contents, final OutputStream os, final int maxRow) {
        Map<String ,Collection<T>> map = new HashMap<String, Collection<T>>();
        map.put(getPrefix(),contents);
        exportToZip(titles,map,os,maxRow);
    }
}