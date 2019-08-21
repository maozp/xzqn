package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author: hx
 * @Date: 2019/6/29 10:43
 * @Description: 用户协议
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "TEXT")
    private String text;
}
