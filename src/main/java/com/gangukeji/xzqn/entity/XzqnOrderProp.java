package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: hx
 * @Date: 2019/6/18 18:38
 * @Description: 服务属性
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnOrderProp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer publishId;
    private Integer orderId;
    private Integer propId;
    private Integer orderCateId;
}
