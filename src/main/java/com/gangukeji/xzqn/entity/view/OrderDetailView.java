package com.gangukeji.xzqn.entity.view;

import com.fasterxml.jackson.annotation.*;
import com.gangukeji.xzqn.entity.XzqnServiceOrder;
import com.gangukeji.xzqn.entity.XzqnServicePublish;
import lombok.Data;

import java.util.Date;

/**
 * @Author: hx
 * @Date: 2019/6/18 6:04
 * @Description: 一个包含publish和order返回给前端的视图
 */
@Data
@JsonPropertyOrder({"orderId","status","statusName","statusDes","statusNameLast","statusDesLast","statusNameNext","statusDesNext","distance","createTime"})
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailView {
    //状态名
    private String statusName;
    //状态描述
    private String statusDes;
    //上一个状态和下一个状态
    private String statusNameLast;
    private String statusDesLast;
    private String statusNameNext;
    private String statusDesNext;
    //状态对应的图片
    private String img;
    //状态
    private Integer status;
    //发单是否可点击
    private Integer sendClick;
    @JsonUnwrapped
    public XzqnServicePublish publish;
    @JsonUnwrapped
    public XzqnServiceOrder order;
    //优先显示public数据
    private double distance;

    public void setDistance(double distance) {
        publish.setDistance(distance);
        this.distance = distance;
    }

    @JsonGetter("statusName")
    private String getStatusName() {
        publish.setStatusName(this.statusName);
        return this.statusName;
    }
    @JsonGetter("statusDes")
    private String getStatusDes() {
        publish.setStatusDes(this.statusDes);
        return this.statusDes;
    }


    private String project;
    public Date createTime;
    //放外面 排序

    public Date getCreateTime() {
        Date time;
        if (order == null) {
            time = publish.getCreateTime();
        } else {
            time = order.getCreateTime();
        }
        return time;
    }

    public Integer getStatus() {
        if (order == null) {
            return publish.getStatus();
        } else {
            publish.setStatus(order.getStatus());
            return order.getStatus();
        }
    }

    public Integer orderId;

    public Integer getOrderId() {

        try {
            return order.getOrderId();
        } catch (Exception e) {
            return null;
        }

    }

    @JsonRawValue
    public String getProject() {
        return publish.getJson();
    }
}
