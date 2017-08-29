package com.jd.o2o.coupon.test.worker;

import java.util.Scanner;

/**
 * Created by songwei3 on 2017/8/12.
 */
public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Integer result = getDay(sc.nextLine());
        if(result == null){
           System.out.println("输入非法！");
           return;
        }
        System.out.println("答案："+result);
    }

    private static Integer getDay(String param){
        if(param == null || param.equals("")){
            return null;
        }
        String[] array  = param.split(" ");
        if(array.length != 4){
            return null;
        }
        for(int i = 0;i < array.length;i++){
            for(int n = 0 ; n< array[i].length(); n++) {
                if ( ! Character.isDigit(array[i].charAt(n))) {
                    return null;
                }
            }
        }
        long l = 2 * 10^9;
        long x = Long.parseLong(array[0]);
        long f = Long.parseLong(array[1]);
        long d = Long.parseLong(array[2]);
        long p = Long.parseLong(array[3]);
        if(x >= 1 && f >= 1 && d >= 1 && p >= 1  &&  x <= l &&  p <= l && f <= l &&  d <= l ){
            return (int)Math.floor((f*p+d) / (p +x));
        }
        return null;
    }
}
