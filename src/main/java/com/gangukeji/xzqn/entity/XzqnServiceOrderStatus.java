package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hx
 * @Description 订单状态对应的描述,用于控制 1按钮 2订单详情 3订单列表里面的文本图片
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class XzqnServiceOrderStatus {
    private static final long serialVersionUID = 1L;
    //发单状态名称
    private String sendName;
    //发单状态列表名单
    private String sendListName;
    //发单列表状态描述
    private String sendListDes;
    //师傅状态列表名称
    private String receiveListName;
    //师傅列表状态描述
    private String receiveListDes;
    //发单详情顶部状态文字描述
    private String sendTop;
    //师傅详情顶部状态文字描述
    private String receiveTop;
    //师傅状态名称
    private String receiveName;
    //师傅状态描述
    private String receiveDes;
    //发单状态描述
    private String sendDes;
    //状态图片
    private String img;
    //按钮应该时 发单方可点 还是接单方可点
    private Integer sendClick;
    //订单描述
    private String des;
    //状态描述
    private Integer status;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;
}

