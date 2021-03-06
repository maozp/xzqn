package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnMessageAdvice {

    @Id  //说明id是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //使用自增数值作为主键
    private Integer id;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Date time;

}
