package com.gangukeji.xzqn.entity.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: hx
 * @Date: 2019/6/11 19:19
 * @Description: 商城用户表
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnShopUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String password;
    private String username;
    private String openid;

    private String headImg;
    private String mark;
    private String name;
    private String nickname;
    private String phone;

    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;
}
