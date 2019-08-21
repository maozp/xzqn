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
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
/**
 * 商城地址表
 */
public class XzqnShopAddr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addrId;
    private Integer userId;
    private String addr;
    private String area;
    private String city;
    private String name;
    private String phone;
    private Integer lat;
    private Integer lng;
    private String zip;
    private Integer isDefault;


    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;
}
