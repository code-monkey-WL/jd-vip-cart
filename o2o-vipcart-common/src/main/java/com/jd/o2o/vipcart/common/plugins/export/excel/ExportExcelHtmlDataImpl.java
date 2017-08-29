package com.jd.o2o.vipcart.common.plugins.export.excel;

import com.jd.o2o.vipcart.common.plugins.export.ExportExcelData;
import org.springframework.web.util.HtmlUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 *  用于excel大数据量导出
 * Created by liuhuiqing on 2015/2/10.
 */
public class ExportExcelHtmlDataImpl<T> extends AbstractExportExcelData<T> implements ExportExcelData<T> {

    final static StringBuffer headStr = new StringBuffer(
            "<html xmlns:x=\"urn:schemas-microsoft-com:office:excel\">")
            .append("<head>")
            .append(
                    "<meta http-equiv=\"content-type\" content=\"application/ms-excel; charset=UTF-8\"/>")
            .append("<!--[if gte mso 9]><xml>").append("<x:ExcelWorkbook>")
            .append("<x:ExcelWorksheets>").append("<x:ExcelWorksheet>").append(
                    "<x:Name></x:Name>").append("<x:WorksheetOptions>").append(
                    "<x:Print>").append("<x:ValidPrinterInfo />").append(
                    "</x:Print>").append("</x:WorksheetOptions>").append(
                    "</x:ExcelWorksheet>").append("</x:ExcelWorksheets>")
            .append("</x:ExcelWorkbook>").append("</xml><![endif]-->").append(
                    "<style type=\"text/css\">")
            .append("<!--td{mso-number-format:\"\\@\";background-color: #eee;}-->")
            .append("</style>")
            .append(
                    "</head>").append("<body>").append("<table>");

    final static StringBuffer footStr = new StringBuffer("</table></body></html>");

    private String prefix = "temp";//生成文件前缀
    private String suffix = ".xls";//生产文件后缀

    public ExportExcelHtmlDataImpl() {
    }

    public ExportExcelHtmlDataImpl(String prefix) {
        this.prefix = prefix;
    }

    public ExportExcelHtmlDataImpl(String prefix, String suffix) {
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
        writeToOutputStream(headStr.toString(), fos);
    }

    /**
     * 文件结尾的写入
     *
     * @param fos
     */
    @Override
    protected void writeFooterToOutputStream(FileOutputStream fos)
            throws IOException {
        writeToOutputStream(footStr.toString(), fos);
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
        writeToOutputStream("<tr "+(titles==content?"style='font-weight:bold'":"")+">", fos);
        Field[] fields = clazz.getDeclaredFields();
        for (Field fs:fields) {
            if (Modifier.isStatic(fs.getModifiers())) {
                continue;
            }
            fs.setAccessible(true);
            Object titleName = fs.get(titles);
            if(titleName == null){
                continue;
            }
            writeToOutputStream(new StringBuilder("<td>")
                    .append(HtmlUtils.htmlEscape(String.valueOf(fs.get(content)))).append("</td>").toString(), fos);
        }
        writeToOutputStream("</tr>", fos);
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
            out = new FileOutputStream("E:\\order.zip");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ExportExcelHtmlDataImpl<Demo> test = new ExportExcelHtmlDataImpl("tem");

        Map<String,Collection<Demo>> map  = new HashMap<String,Collection<Demo>>();
        map.put("liu",list);
//        map.put("hui",list);
//        map.put("qing",list);
//        test.exportToZip(new Demo("商品Id", "商品名称"), map, out, -1);
        test.exportToZip(new Demo("商品Id", "商品名称"), list, out, 100000);
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
