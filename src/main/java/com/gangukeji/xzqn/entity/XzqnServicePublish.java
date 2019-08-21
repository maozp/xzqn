package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.*;
import com.gangukeji.xzqn.utils.StringToListUtil;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author hx
 * @Description xzqn_service_publish实体类
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"publishId", "cateList", "userId", "serviceAddr", "factorId", "name", "phone", "companyName", "companyPhone", "lat", "lng", "serviceDesc", "serviceFee", "totalFee", "factorName", "distance", "receiveUserIdList","createTime", "project","isCheck"})
public class XzqnServicePublish {
    public XzqnServicePublish(Integer id, Integer sendUserId, String name, String phone, Integer userId, Integer factorId, String companyName, String companyPhone, String serviceDesc, String serviceAddr, BigDecimal lat, BigDecimal lng, BigDecimal totalFee, Boolean isCancel, Integer status, Date createTime, String json, BigDecimal serviceFee,String receiveUserIdList,Integer isCheck,String refuseReason) {
        this.id=id;
        this.sendUserId = sendUserId;
        this.name = name;
        this.phone = phone;
        this.userId = userId;
        this.factorId = factorId;
        this.companyName = companyName;
        this.companyPhone = companyPhone;
        this.serviceDesc = serviceDesc;
        this.serviceAddr = serviceAddr;
        this.lat = lat;
        this.lng = lng;
        this.totalFee = totalFee;
        this.isCancel = isCancel;
        this.status = status;
        this.createTime = createTime;
        this.json = json;
        this.serviceFee = serviceFee;
        this.receiveUserIdList = receiveUserIdList;
        this.isCheck = isCheck;
        this.refuseReason = refuseReason;
    }
    //发单用户id
    private String refuseReason;
    private Integer sendUserId;
    private Integer isCheck;
    //发单用户名
    private String name;
    @JsonIgnore
    //抢单用户的用户详情id用,隔开
    private String receiveUserIdList;
    @Transient
    //返回前端的receiveUserIdList数组
    private List<Integer> receiveUserIdArray;

    public List<Integer> getReceiveUserIdArray() {
        if (receiveUserIdList == null) {
            receiveUserIdList = "";
        }
        return StringToListUtil.go(receiveUserIdList, ",");
    }
    //电话
    private String phone;
    //用户id
    private Integer userId;
    //计费系数id 对应表 xzqn_user_factor
    private Integer factorId;
    //公司名称
    private String companyName;
    //公司电话
    private String companyPhone;
    //服务描述
    private String serviceDesc;
    //服务地址
    private String serviceAddr;
    //服务地点纬度
    private BigDecimal lat;
    //服务地点经度
    private BigDecimal lng;
    //计算得到的服务费用
    private BigDecimal serviceFee;
    //实际费用
    private BigDecimal totalFee;
    @Column(columnDefinition = "bit default 0")
    //是否取消
    private Boolean isCancel;
    //id,cateIds,propIds,status,serviceAddr,serviceDesc,totalFee,lat,lng,createTime,isCancel
    @Column(columnDefinition = "Integer default 1")
    //订单状态
    private Integer status;
    //start
    @JsonProperty("publishId")
    @Id
    @JsonAlias("publishId")
    @SerializedName("publishId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @JsonIgnore
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;

    @Transient
    @JsonIgnore
    //前端接收分类和属性数据用
    private Object[] project;
    //客户类型对应 xzqn_user_factor表
    @Transient
    private String factorName;//回传前端用
    @Transient
    //状态名
    private String statusName;
    @Transient
    //状态描述
    private String statusDes;
    @Transient
    //距离
    private Double distance;
    public Double getDistance() {
        if (distance == null) {
            return 0d;
        } else {
            return distance;
        }
    }
    @Deprecated
    @JsonIgnore
    //公司部门
    private String companyDept;
    //分类id
    @Deprecated
    private Integer cateId;
    @Deprecated
    @JsonIgnore
    private String listSendName;
    @Deprecated
    @JsonIgnore
    private String listReceiveName;
    @Deprecated
    @JsonIgnore
    private String listStatus;
    @Deprecated
    @JsonIgnore
    private String listDesc;
    @Column(columnDefinition = "bit default 0")
    @Deprecated
    @JsonIgnore
    private Boolean isPayPlatform;
    @Deprecated
//    @JsonIgnore
    private String feeItemIds;
    public String getFeeItemIds() {
        return "高空作业 ￥500 ";
    }


    @Deprecated
    private BigDecimal platformFee;
    public BigDecimal getPlatformFee() {
        return new BigDecimal(1);
    }
    @Deprecated
    @Transient
    private String serviceCate() {
        return "消防-灭火器安装";
    }

    /**
     * 订单的分类及属性json
     */
    @Column(columnDefinition = "TEXT")
    @JsonProperty("project")
    @JsonRawValue
    private String json;//
    @Transient
    //没抢0  抢了(待确认)1   抢失败2  抢成功生成订单
    private Integer grabStatus=0;
}

