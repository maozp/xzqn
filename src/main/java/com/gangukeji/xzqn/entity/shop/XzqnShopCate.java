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
 * 商品分类表
 */
public class XzqnShopCate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer cateId;
  private String cateName;
  private Integer parentId;
  private String pic;



  @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
  @CreationTimestamp
  private Date createTime;
  @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
  @UpdateTimestamp
  private Date updateTime;


}
