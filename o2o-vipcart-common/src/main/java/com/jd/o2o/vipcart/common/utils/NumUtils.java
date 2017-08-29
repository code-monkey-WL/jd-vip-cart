package com.jd.o2o.vipcart.common.utils;

import java.math.BigDecimal;

/**
 * 数字处理类
 *
 * @author liuhuiqing
 */
public class NumUtils {
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static double add(Object v1, Object v2) {
        BigDecimal b1 = toBigDecimal(v1);
        BigDecimal b2 = toBigDecimal(v2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static double sub(Object v1, Object v2) {
        BigDecimal b1 = toBigDecimal(v1);
        BigDecimal b2 = toBigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(Object v1, Object v2) {
        BigDecimal b1 = toBigDecimal(v1);
        BigDecimal b2 = toBigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */

    public static double div(Object v1, Object v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(Object v1, Object v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = toBigDecimal(v1);
        BigDecimal b2 = toBigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(Object v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = toBigDecimal(v);
        BigDecimal ne = new BigDecimal("1");
        return b.divide(ne, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 字符串数字转换成BigDecimal对象
     *
     * @param v
     * @return
     */
    public static BigDecimal toBigDecimal(Object v) {
        if (v == null) {
            throw new IllegalArgumentException("The Object must be not null");
        }
        String val = String.valueOf(v);
        if (!isNumber(String.valueOf(v))) {
            throw new IllegalArgumentException(
                    String.format("The Object[%s] is not a number", v));
        }
        return new BigDecimal(val);
    }

    /**
     * Double类型判断
     *
     * @param value
     * @return
     */
    private static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains(".")){
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 长整数判断
     *
     * @param value
     * @return
     */
    private static boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 实数判断
     *
     * @param value
     * @return
     */
    private static boolean isNumber(String value) {
        return isLong(value) || isDouble(value);
    }

    public static void main(String[] args) {
        Float a = 2.1F;
        Double b = 1.1D;
        System.out.println(a + b);
        System.out.println(add(a, b));

        System.out.println(a - b);
        System.out.println(sub(a, b));

        System.out.println(a * b);
        System.out.println(mul(a, b));

        System.out.println(a / b);
        System.out.println(div(a, b, 2));

    }
}
