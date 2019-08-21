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
 * @Date: 2019/7/16 13:43
 * @Description: 帮助数据
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnHelp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String src;
    private String des;
}
