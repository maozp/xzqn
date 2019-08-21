package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gangukeji.xzqn.entity.XzqnUserReceive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: hx
 * @Date: 2019/6/29 18:29
 * @Description: 抢单记录表 实现多人抢单
 * grabStatus (-1已被抢,0等待确认,1确认抢单,2没有枪)
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnGrabRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer publishId;
    @JsonIgnore
    private Integer userId;
    private Integer grabStatus;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @Transient
    private XzqnUserReceive userReceive;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;
    @Transient
    private boolean star;
}
