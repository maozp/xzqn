package com.gangukeji.xzqn.entity.shop;

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
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
/**
 * 商城支付记录表
 */
public class XzqnShopPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payId;
    private Integer orderId;
    private Integer userId;
    private BigDecimal money;
    private Integer status;//支付状态
    private Integer type;//支付类型


    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;
}
