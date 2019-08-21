package com.gangukeji.xzqn.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnAuthContent {
  //是否审核0未认证 1已提交 2已认证
  private Integer isCheck;
  //师傅id
  private Integer receiveUserId;
  //用户id
  private Integer userId;
  //服务地区
  private String area;
  //服务描述
  private String des;
  //服务承诺
  private String promise;
  @Column
  @JsonIgnore
  //服务介绍
  private String introduce;
  private String refuseReason;
  @Transient
  //服务介绍数组
  private List<String> introduceList;
  @Id
  @JsonProperty("authId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
  @CreationTimestamp
  private Date createTime;

  @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
  @UpdateTimestamp
  private Date updateTime;

}
