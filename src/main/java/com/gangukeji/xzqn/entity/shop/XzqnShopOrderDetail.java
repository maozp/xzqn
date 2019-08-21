package com.gangukeji.xzqn.entity.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
/**
 * 商城订单详情表
 */
public class XzqnShopOrderDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer orderDetailId;
  private Integer orderId;
  private Integer productId;
  private String productName;
  private Integer productCount;
  private BigDecimal productPrice;
  private BigDecimal reduceMoney;

  @Transient
  private String pic;
  @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
  @CreationTimestamp
  private Date createTime;
  @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
  @UpdateTimestamp
  private Date updateTime;
}
