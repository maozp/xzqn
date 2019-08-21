package com.gangukeji.xzqn.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/1 19:16
 * @Description: 字符串转List<Integer>工具
 */
public class StringToListUtil {
    public static List<Integer> go(String str, String c) {
        String[] split = str.split(c);
        ArrayList<Integer> list = new ArrayList<>();
        Arrays.stream(split).forEach(s -> {
            if (s != null) {
                try {
                    list.add(Integer.valueOf(s));
                } catch (Exception e) {
//                    doNothing
                }
            }
        });
        return list;
    }
}
