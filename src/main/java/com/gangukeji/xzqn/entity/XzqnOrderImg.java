package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author: hx
 * @Date: 2019/7/18 11:45
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnOrderImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int orderId;
    private int publishId;
    @Column(columnDefinition = "TEXT")
    private String imgs;

}
