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
@Table(name = "xzqn_auth_sign")
public class XzqnAuthSign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "receive_user_id")
    //(师傅)id
    private Integer receiveUserId;
    @Column
    private Integer userId;
    @Column
    private Integer signPoint;
    @Column
    private Date signTime;
    @Column
    private Date signLastTime;
    @Column
    private Integer signContinueDays;
    @Column
    private Integer signTotalDays;
    @Column
    private Integer isSign;

}
