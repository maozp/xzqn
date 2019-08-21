package com.gangukeji.xzqn.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * @Author: hx
 * @Date: 2019/5/21 9:53
 * @Description: 经纬度计算
 */
public class LocationUtils {
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离
     */
    public static double getDistance(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2,
                                     BigDecimal lng2) {
        if (lat1 == null | lat2 == null | lng2 == null | lng1 == null) {
            return 0;
        }
        double radLat1 = rad(lat1.doubleValue());
        double radLat2 = rad(lat2.doubleValue());
        double a = radLat1 - radLat2;
        double b = rad(lng1.doubleValue()) - rad(lng2.doubleValue());
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000d) / 10000d;
        distance = distance * 1000;
        return Math.ceil(distance);
    }
}

