package com.gangukeji.xzqn.utils;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/3 14:27
 * @Description:
 */
public class ListToPageUtil {
    public static List go(JsonObject jsonObject, List resps) {
        //实现分页
        try {
            int page = jsonObject.get("page").getAsInt();
            int size = jsonObject.get("size").getAsInt();
            int fromIndex = page * size;
            int toIndex = (page + 1) * size;
            if (fromIndex > resps.size()) {
                fromIndex = resps.size();
            }
            if (toIndex > resps.size()) {
                toIndex = resps.size();
            }
            resps = resps.subList(fromIndex, toIndex);
        } catch (Exception e) {
            System.out.print(e + "111eeeeeeeeeeeeeeeeeeeeeeeeee 未分页查找");
        }
        return resps;
    }
    public static List go2(int page,int size, List resps) {
        //实现分页
        try {
            int fromIndex = page * size;
            int toIndex = (page + 1) * size;
            if (fromIndex > resps.size()) {
                fromIndex = resps.size();
            }
            if (toIndex > resps.size()) {
                toIndex = resps.size();
            }
            resps = resps.subList(fromIndex, toIndex);
        } catch (Exception e) {
            System.out.print(e + "111eeeeeeeeeeeeeeeeeeeeeeeeee 未分页查找");
        }
        return resps;
    }
}
