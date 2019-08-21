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
 * @Date: 2019/7/18 11:45
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnAdmin {
    @Id  //说明id是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //使用自增数值作为主键
    private int id;
    private String username;
    private String password;

}
