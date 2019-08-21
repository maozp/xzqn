package com.gangukeji.xzqn.utils;

import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/1 19:16
 * @Description: list转string工具
 */
public class ListToString {
    /**
     * 整型list拼接成字符串
     * @param integerList
     * @param c
     * @return
     */
    public static String goInt(List<Integer> integerList, String c) {
        StringBuilder builder = new StringBuilder();
        integerList.forEach(i->builder.append(i+c));
        return builder.toString();
    }

    /**
     * 字符串list拼接成字符串
     * @param stringList
     * @param c
     * @return
     */
    public static String goString(List<String> stringList, String c) {
        StringBuilder builder = new StringBuilder();
        stringList.forEach(str->builder.append(str+c));
        return builder.toString();
    }
}
