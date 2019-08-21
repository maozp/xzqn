package com.gangukeji.xzqn.entity.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
/**
 * 商城评论表
 */
public class XzqnShopProductComment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer commentId;
  private Integer productId;
  private Integer orderId;
  private Integer userId;
  private String title;
  private String content;
  private Integer isCheck;

  @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
  @CreationTimestamp
  private Date createTime;
  @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
  @UpdateTimestamp
  private Date updateTime;

}
