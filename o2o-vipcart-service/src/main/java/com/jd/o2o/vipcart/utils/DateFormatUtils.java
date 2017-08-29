package com.jd.o2o.vipcart.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>文件名称: DateFormatUtils.java</p>
 * <p>文件描述: 日期格式化工具</p>
 * @author  Wuqingming
 */
public class DateFormatUtils {

    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期转字符串
     * @param date
     * @param format
     * @return
     */
    public static String date2Str (Date date, String format) {
        if(date == null){
            return null;
        }
        String resultStr = "";
        String formatTemp = format;
        if(formatTemp == null || "".equals(formatTemp)){
            formatTemp = DEFAULT_DATE_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatTemp);

        resultStr = dateFormat.format(date).trim();

        return resultStr;
    }

    /**
     * 字符串转日期
     * @param str
     * @param format
     * @return
     * @throws java.text.ParseException
     */
    public static Date str2Date (String str, String format)  {
        try {
            String formatTemp = format;
            if(formatTemp == null || "".equals(formatTemp)){
                formatTemp = DEFAULT_DATE_FORMAT;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatTemp);

            return dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转日期，自动适配字符串格式
     * @param str
     * @return
     * @throws java.text.ParseException
     */
    public static Date str2Date (String str) throws ParseException {
        String[] parsePatterns = {DEFAULT_TIME_FORMAT, DEFAULT_DATE_FORMAT, "yyyyMMdd", "yyyyMMddHHmmss",
                "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"};
        return parseDateWithLeniency(str, parsePatterns, false);
    }

    private static Date parseDateWithLeniency(
            String str, String[] parsePatterns, boolean lenient) throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }

        SimpleDateFormat parser = new SimpleDateFormat();
        parser.setLenient(lenient);
        ParsePosition pos = new ParsePosition(0);
        for (String parsePattern : parsePatterns) {

            String pattern = parsePattern;

            // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
            if (parsePattern.endsWith("ZZ")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }

            parser.applyPattern(pattern);
            pos.setIndex(0);

            String str2 = str;
            // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
            if (parsePattern.endsWith("ZZ")) {
                str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2");
            }

            Date date = parser.parse(str2, pos);
            if (date != null && pos.getIndex() == str2.length()) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }


    public static Date addDay(Date currentDate, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, day);
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        return str2Date(sdf.format(d), DEFAULT_TIME_FORMAT);
    }


/*
    public static void main(String[] args) {
        try {
            System.out.println(str2Date("2013/09/18"));
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            System.out.println(str2Date("2013/09/18 14:34:00"));
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            System.out.println(str2Date("2013-09-18"));
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            System.out.println(str2Date("2013-09-18 14:34:00"));
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            System.out.println(str2Date("20130918"));
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            System.out.println(str2Date("20130918141301"));
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }*/
}

