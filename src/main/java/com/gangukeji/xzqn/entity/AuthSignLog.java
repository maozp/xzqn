package com.gangukeji.xzqn.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "xzqn_auth_sign_log")
public class AuthSignLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    /**
     * 关联签到id
     */
    @Column
    private Integer authSignId;


    @Column
    private Date createTime;

    @Column
    private Date updateTime;


    /**
     * 是否可用
     */
    @Column(nullable = false, columnDefinition = "char(1)")
    @Type(type = "yes_no")
    private Boolean signStatus;
}
