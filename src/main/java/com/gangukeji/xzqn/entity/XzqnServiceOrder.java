package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gangukeji.xzqn.controller.xzqn.order.SetStatusControllerV2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hx
 * @Description publish对应的order,在发单方确认时生成,相当于订单详情
 * {@link SetStatusControllerV2 setStatus 发单方确认师傅生成}
 */
@Entity
@Data
@Table(name = "xzqn_service_order")
@AllArgsConstructor
@NoArgsConstructor
public class XzqnServiceOrder {
    public XzqnServiceOrder(String orderNo, Integer userId, Integer sendUserId, Integer receiveUserId, Integer publishId, Boolean isPay, BigDecimal totalFee, BigDecimal serviceFee, Integer status, Date createTime, Integer orderId) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.userId = userId;
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
        this.publishId = publishId;
        this.isPay = isPay;
        this.totalFee = totalFee;
        this.serviceFee = serviceFee;
        this.status = status;
        this.createTime = createTime;
    }

    private String orderNo;
    private Integer userId;//sendUser的userId
    private Integer sendUserId;
    private Integer receiveUserId;
    private Integer publishId;
    private Boolean isPay;
    private BigDecimal totalFee;
    private BigDecimal serviceFee;
    @Column(columnDefinition = "Integer default 5")
    private Integer status;
    //默认0为个人1为企业
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @JsonIgnore
    @UpdateTimestamp
    private Date updateTime;
    @Deprecated
    @JsonIgnore
    private Integer cateId;
    @Deprecated
    @JsonIgnore
    private Integer commentId;
    @JsonIgnore
    @Deprecated
    private Integer complainId;
    @Deprecated
    @JsonIgnore
    private Integer reportId;
    @JsonIgnore
    @Deprecated
    private Boolean isCancel;
    @Deprecated
    @JsonIgnore
    private String cancelReason;
    @Deprecated
//    @JsonIgnore
    private String feeItemIds;

    public String getFeeItemIds() {
        return "高空作业 ￥500 ";
    }

    @Deprecated
    @JsonIgnore
    private String cateIds;//新版本分类id
    @JsonIgnore
    @Deprecated
    private String propIds;//新版本属性|8|12|15|19|
    @JsonIgnore
    @Deprecated
    private String orderTime;
    @JsonIgnore
    @Deprecated
    private String listStatus;
    @JsonIgnore
    @Deprecated
    private String listSendName;
    @JsonIgnore
    @Deprecated
    private String listReceiveName;
    @JsonIgnore
    @Deprecated
    private String listDesc;
    @JsonIgnore
    @Deprecated
    private BigDecimal lat;
    @JsonIgnore
    @Deprecated
    private BigDecimal lng;
    @JsonIgnore
    @Deprecated
    private Integer receiveType;
}

