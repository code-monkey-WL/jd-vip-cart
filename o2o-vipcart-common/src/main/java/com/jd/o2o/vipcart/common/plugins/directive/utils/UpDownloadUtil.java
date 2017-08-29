package com.jd.o2o.vipcart.common.plugins.directive.utils;

import com.jd.o2o.vipcart.common.plugins.directive.model.UpDownloadModel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *  文件上传标签工具类
 * Created by liuhuiqing on 2015/6/1.
 */
public class UpDownloadUtil {

    public static String buildUploadHtml(UpDownloadModel upDownloadModel) {
        StringBuilder  hidenStringBuilder  = new StringBuilder();
        String action = "";
        for(Field field:upDownloadModel.getClass().getDeclaredFields()){
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            String fileName = field.getName();
            try {
                Object filedValue = field.get(upDownloadModel);
                if(filedValue == null){
                    continue;
                }
                if("action".equals(fileName)){
                    action = (String) filedValue;
                    continue;
                }
                hidenStringBuilder.append(String.format("<input type='hidden' name='%s' id='%s' value='%s'/>",new Object[]{fileName,fileName,filedValue}));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return String.format("<div id='fileUploadInputDiv' v='fileUpload'><form action='%s' method='post' enctype='multipart/form-data'>%s<input type='file' name='file' id='file' size='28'/><input type='submit' name='submit' class='btn' value='上传' /></form></div>",new Object[]{action,hidenStringBuilder.toString()});
    }
}
