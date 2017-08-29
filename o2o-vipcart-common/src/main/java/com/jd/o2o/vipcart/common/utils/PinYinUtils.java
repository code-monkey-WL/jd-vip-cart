package com.jd.o2o.vipcart.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinYinUtils {

    // 简体中文的编码范围从B0A1（45217）一直到F7FE（63486）
    private static int BEGIN = 45217;
    private static int END = 63486;

    // 按照声 母表示，这个表是在GB2312中的出现的第一个汉字，也就是说“啊”是代表首字母a的第一个汉字。
    // i, u, v都不做声母, 自定规则跟随前面的字母
    private static char[] chartable = {'啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝',};

    // 二十六个字母区间对应二十七个端点
    // GB2312码汉字区间十进制表示
    private static int[] table = new int[27];

    // 对应首字母区间表
    private static char[] initialtable = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 't', 't', 'w', 'x', 'y', 'z',};

    // 初始化
    static {
        for (int i = 0; i < 26; i++) {
            table[i] = gbValue(chartable[i]);// 得到GB2312码的首字母区间端点表，十进制。
        }
        table[26] = END;// 区间表结尾
    }

    // ------------------------public方法区------------------------
    // 根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串 最重要的一个方法，思路如下：一个个字符读入、判断、输出
    @Deprecated
    public static String cn2py(String sourceStr) {
        if (sourceStr == null || sourceStr == "") {
            return "";
        }
        StringBuilder result = new StringBuilder();
        int StrLength = sourceStr.length();
        int i;
        try {
            for (i = 0; i < StrLength; i++) {
                result.append(Char2Initial(sourceStr.charAt(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return result.toString();
    }

    /**
     * 返回指定长度的拼音和数字
     *
     * @param sourceStr
     * @param length
     * @return
     */
    @Deprecated
    public static String cn2pyOnlyCharNum(String sourceStr, int length) {
        String py = cn2py(sourceStr);
        py = py.replaceAll("[^\\w]", "");
        if (py.length() > length) {
            return py.substring(0, length);
        }
        return py;
    }

    /**
     * 将汉字转换为全拼
     *
     * @param src
     * @return
     */
    public static String getPingYin(String src) {

        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder t4 = new StringBuilder();
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (Character.toString(t1[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4.append(t2[0]);
                } else
                    t4.append(Character.toString(t1[i]));
            }
            return t4.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4.toString();
    }

    public static List<String> getPingYins(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        List<String> list = new ArrayList<String>();
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (Character.toString(t1[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    list.add(t2[0]);
                } else
                    list.add(Character.toString(t1[i]));
            }
            return list;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return list;
    }

    /**
     * 返回中文的首字母
     *
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {

        StringBuilder convert = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString();
    }

    /**
     * 将字符串转移为ASCII码
     *
     * @param cnStr
     * @return
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }

    // ------------------------private方法区------------------------

    /**
     * 输入字符,得到他的声母,英文字母返回对应的大写字母,其他非简体汉字返回 '0' 　　*
     */
    private static char Char2Initial(char ch) {
        // 对英文字母的处理：小写字母转换为大写，大写的直接返回
        if (ch >= 'a' && ch <= 'z') {
            return (char) (ch - 'a' + 'A');
        }
        if (ch >= 'A' && ch <= 'Z') {
            return ch;
        }
        // 对非英文字母的处理：转化为首字母，然后判断是否在码表范围内，
        // 若不是，则直接返回。
        // 若是，则在码表内的进行判断。
        int gb = gbValue(ch);// 汉字转换首字母
        if ((gb < BEGIN) || (gb > END))// 在码表区间之前，直接返回
        {
            return ch;
        }
        int i;
        for (i = 0; i < 26; i++) {// 判断匹配码表区间，匹配到就break,判断区间形如“[,)”
            if ((gb >= table[i]) && (gb < table[i + 1])) {
                break;
            }
        }
        if (gb == END) {// 补上GB2312区间最右端
            i = 25;
        }
        return initialtable[i]; // 在码表区间中，返回首字母
    }

    /**
     * 取出汉字的编码 cn 汉字
     */
    private static int gbValue(char ch) {// 将一个汉字（GB2312）转换为十进制表示。
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2) {
                return 0;
            }
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }

    private static String getFirstNumberFromText(String text) {
        Pattern pattern = Pattern.compile("(\\d)+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static boolean isCharacter(String s) {
        for(char c:s.toCharArray()){
            int i = (int) c;
            if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            } else {
                return false;
            }
        }
        return true;
    }

    private static String floorExtraction(String address) {
        String floor = "";
        String addressPinyin = PinYinUtils.getPingYin(address);
        Pattern pattern = Pattern.compile("(\\d)+(shi|fangjian)+");
        Matcher matcher = pattern.matcher(addressPinyin);
        while (matcher.find()) {
            floor = matcher.group();
        }
        if (StringUtils.isNotEmpty(floor)) {
            floor = getFirstNumberFromText(floor);
            if (floor.length() == 3) {
                floor = floor.substring(0, 1);
            } else if (floor.length() == 4) {
                floor = floor.substring(0, 2);
            } else if (floor.length() == 5) {
                floor = floor.substring(0, 3);
            }
            return floor;
        }
        pattern = Pattern.compile("(\\d)+(ceng|f|F|floor|FLOOR)+");
        matcher = pattern.matcher(addressPinyin);
        while (matcher.find()) {
            floor = matcher.group();
        }
        if (StringUtils.isNotEmpty(floor)) {
            return getFirstNumberFromText(floor);
        }
        return floor;
    }


    public static void main(String[] args) throws Exception {
        String str = "重庆重视发展IT行业，大多数外企，如，IBM等2101shi进驻山城20层";
//        System.out.println(cn2py(str));
//        System.out.println(getPingYin(str));
//        System.out.println(getPinYinHeadChar(str));
//        System.out.println(getCnASCII(str));
//        System.out.println(JsonUtils.toJson(getPingYins(str)));

        System.err.println(floorExtraction(str));
        System.err.println(isCharacter("asfAZ!"));
        System.out.println(getFirstNumberFromText("8"));
//        Pattern pattern = Pattern.compile("(\\d)+[ceng|f|F|floor|FLOOR}]+");
//        Matcher matcher = pattern.matcher(str);
//        String floor = "";
//        while (matcher.find()) {
//            floor = matcher.group();
//            System.out.println(floor);
//        }
//
//        pattern = Pattern.compile("(\\d)+[shi|fangjian]+");
//        matcher = pattern.matcher(str);
//        while (matcher.find()) {
//            floor = matcher.group();
//            System.err.println(floor);
//        }
//
//        if (StringUtils.isNotEmpty(floor)) {
//            pattern = Pattern.compile("(\\d)+");
//            matcher = pattern.matcher(floor);
//            while (matcher.find()) {
//                floor = matcher.group();
//            }
//        }
//        System.err.println(floor);


    }
}