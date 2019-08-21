package com.gangukeji.xzqn.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "xzqn_auth_sign")
public class AuthSign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 关联用户ID
     */
    @Column(nullable = false)
    private Integer authId;


    /**
     * 创建时间
     */
    @Column
    private Date createTime;


    /**
     * 最后签到时间
     */
    @Column
    private Date lastModifyTime;


    /**
     * 连续签到次数
     */
    @Column
    private Integer signCount;

    /**
     * 签到次数
     */
    @Column
    private Integer count;

}
