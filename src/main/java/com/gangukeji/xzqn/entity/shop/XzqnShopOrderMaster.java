package com.gangukeji.xzqn.entity.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
/**
 * 商城订单主表
 */
public class XzqnShopOrderMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    private Integer userId;
    private String phone;
    private String orderNo;
    private String userName;
    private String province;
    private String city;
    private String area;
    private String address;
    private Integer paymentMethod;
    private BigDecimal orderMoney;
    private BigDecimal reduceMoney;
    private BigDecimal paymentMoney;
    private BigDecimal expressMoney;
    //下单时的经纬度
    private BigDecimal lat;
    private BigDecimal lng;
    private String expressName;
    private String expressNo;
    private Integer orderStatus;
    private String invoiceTitle;
    @Transient
    private Integer productCount;//订单里的商品类型数
    @Transient
    private String statusDes;
    @Transient
    private List<XzqnShopOrderDetail> details;
    //6种status时间 1未支付 2已支付 3出货中 4寄货中 5已完成 6已评价
    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date commentTime;
    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date finishTime;
    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date expressTime;
    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date confirmTime;
    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date payTime;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;

}
