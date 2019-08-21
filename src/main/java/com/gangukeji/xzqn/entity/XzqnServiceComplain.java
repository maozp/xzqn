package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hx
 * @Description 服务投诉项
 */

@Entity
@Data
@Table(name = "xzqn_service_complain")
public class XzqnServiceComplain {
    //订单id
    private Integer orderId;
    //投诉内容
    private String content;
    //投诉图片
    private String img;
    //投诉用户id
    private Integer userId;
    //被投诉用户详情id
    private Integer receiveUserId;
    @Id
    @JsonProperty("complainId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;
}

