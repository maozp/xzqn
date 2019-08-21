package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gangukeji.xzqn.serialize.CustomerBigDecimalSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: hx
 * @Date: 2019/6/12 18:31
 * @Description: 服务钱包记录表  负数为提现
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnServicePay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payId;
    private Integer orderId;
    @JsonIgnore
    private Integer publishId;
    private Integer userId;
    @JsonSerialize(using = CustomerBigDecimalSerialize.class)
    private BigDecimal money;
    private Integer status = 0;//支付状态
    private Integer type = 0;//支付类型
    @Transient
    private String des;//记录描述
    @Transient
    private String img;
    @Transient
    private String name;

    @Transient
    private String detail;//服务描述
    private String serializeNo;//流水号


    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;
}

