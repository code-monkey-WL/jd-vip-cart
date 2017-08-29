package com.jd.o2o.vipcart.common.plugins.export.excel;

import com.google.common.primitives.Bytes;
import com.jd.o2o.vipcart.common.plugins.export.ExportExcelData;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 *  用于excel大数据量导出CSV格式
 * Created by liuhuiqing on 2016/3/29.
 */
public class ExportExcelCSVDataImpl<T> extends AbstractExportExcelData<T> implements ExportExcelData<T> {
    // csv's default delemiter is ','
    private final static String DEFAULT_DELIMITER = ",";
    // Mark a new line
    private final static String DEFAULT_END = "\r\n";
    // If you do not want a UTF-8 ,just replace the byte array.
    private final static byte COMMON_CSV_HEAD[] = { (byte) 0xEF, (byte) 0xBB,
            (byte) 0xBF };
    private String prefix = "temp";//生成文件前缀
    private String suffix = ".csv";//生产文件后缀

    public ExportExcelCSVDataImpl() {
    }

    public ExportExcelCSVDataImpl(String prefix) {
        this.prefix = prefix;
    }

    public ExportExcelCSVDataImpl(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * 临时文件前缀
     *
     * @return
     */
    @Override
    protected String getPrefix() {
        return prefix;
    }

    /**
     * 临时文件后缀
     *
     * @return
     */
    @Override
    protected String getSuffix() {
        return suffix;
    }

    /**
     * 文件开头的写入
     *
     * @param fos
     */
    @Override
    protected void writeHeaderToOutputStream(FileOutputStream fos)
            throws IOException {
        return;
    }

    /**
     * 文件结尾的写入
     *
     * @param fos
     */
    @Override
    protected void writeFooterToOutputStream(FileOutputStream fos)
            throws IOException {
        return;
    }

    /**
     * 一行数据的写入
     *
     * @param content
     * @param fos
     * @throws Exception
     */
    @Override
    protected void writeOneRowToOutputStream(T titles,T content, FileOutputStream fos) throws Exception {
        Class clazz = titles.getClass();
        Field[] fields = clazz.getDeclaredFields();
        int length = fields.length;
        for (Field fs:fields) {
            length--;
            if (Modifier.isStatic(fs.getModifiers())) {
                continue;
            }
            fs.setAccessible(true);
            Object titleName = fs.get(titles);
            if(titleName == null){
                continue;
            }
            writeToOutputStream(new StringBuilder()
                    .append(HtmlUtils.htmlEscape(String.valueOf(fs.get(content)))).append(length>0?DEFAULT_DELIMITER:"").toString(), fos);
        }
        writeToOutputStream(DEFAULT_END, fos);
    }

    @Override
    protected void writeToOutputStream(String data, FileOutputStream fos)
            throws IOException {
        IOUtils.write(Bytes.concat(COMMON_CSV_HEAD, data.getBytes(Charsets.UTF_8.toString())), fos);
    }

    @Override
    protected void writeTitleToOutputStream(T titles, FileOutputStream fos) throws Exception {
        writeOneRowToOutputStream(titles, titles, fos);
    }

    static class Demo {
        private String skuId;
        private String skuName;

        Demo(String skuId, String skuName) {
            this.skuId = skuId;
            this.skuName = skuName;
        }
    }

    public static void main(String[] args) {
        List<Demo> list = new ArrayList<Demo>();
        for (int i = 0; i <100000; i++) {
            list.add(new Demo(i + "", "测试商品" + i));
        }
        long startTime = System.currentTimeMillis();
        OutputStream out = null;
        try {
            out = new FileOutputStream("E:\\orderCSV.zip");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ExportExcelCSVDataImpl<Demo> test = new ExportExcelCSVDataImpl("tem");

        Map<String,Collection<Demo>> map  = new HashMap<String,Collection<Demo>>();
        map.put("liu",list);
//        map.put("hui",list);
//        map.put("qing",list);
//        test.exportToZip(new Demo("商品Id", "商品名称"), map, out, -1);
        test.exportToZip(new Demo("商品Id", "商品名称"), list, out, 100000);
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
