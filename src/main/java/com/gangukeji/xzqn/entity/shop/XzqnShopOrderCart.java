package com.gangukeji.xzqn.entity.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * 购物车表
 */
public class XzqnShopOrderCart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer cartId;
  private Integer userId;
  private Integer productId;
  private Integer productCount;
  private String productName;
  private BigDecimal price;
  @JsonIgnore
  private String pic;
  @Transient
  private boolean isCheck=false;
  @Transient
  private String[] picList;

  @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
  @CreationTimestamp
  private Date createTime;
  @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
  @UpdateTimestamp
  private Date updateTime;

  public String[] getPicList() {
    return pic.split("@");
  }
}
