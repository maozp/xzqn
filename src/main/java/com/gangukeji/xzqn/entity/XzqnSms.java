package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: hx
 * @Date: 2019/7/23 16:57
 * @Description:
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class XzqnSms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String phone;
    private String code;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;

    public XzqnSms(String phone, String code) {
        this.phone = phone;
        this.code = code;
    }
}
