package com.hspedu.exception_;

/**
 * @author ZhouYu
 * @version 1.0
 */
public class NumberFormatException_ {
    public static void main(String[] args) {
        String name = "韩顺平教育";
        //将String 转成 int
        int num = Integer.parseInt(name);//抛出NumberFormatException
        System.out.println(name);//1234
    }
}
