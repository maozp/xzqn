package com.gangukeji.xzqn.entity.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/18 6:55
 * @Description: 师傅在订单中要用到的数据
 */
@Data
@NoArgsConstructor
public class ReceiveDataInOrder {
    /**
     * JPA查找字段需自定义构造函数
     *
     * @param authType
     * @param area
     * @param cateIds
     * @param promise
     * @param lat
     * @param lng
     */
    public ReceiveDataInOrder(String authType, String area, String cateIds, String promise, BigDecimal lat, BigDecimal lng) {
        this.authType = authType;
        this.area = area;
        this.cateIds = cateIds;
        this.promise = promise;
        this.lat = lat;
        this.lng = lng;
    }

    //认证类型
    private String authType;
    //服务地区
    private String area;
    @JsonIgnore
    @Deprecated
    private String cateIds;
    @Deprecated
    private List<String> cateList;//师傅服务介绍的ids
    //服务承诺
    private String promise;
    //纬度
    private BigDecimal lat;
    //经度
    private BigDecimal lng;
}
