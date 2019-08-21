package com.gangukeji.xzqn.entity;

import com.gangukeji.xzqn.core.BaseEntity;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author hx
 * @Description 日志对应的实体项
 */
@NoArgsConstructor
@Entity
@Table
public class XzqnServiceLog extends BaseEntity implements Serializable {
    public XzqnServiceLog(Integer userType, Integer publishId, Integer sendUserId, Integer status,String receiveTitle, String receiveDes, String sendTitle, String sendDes) {
        this.userType = userType;
        this.publishId = publishId;
        this.sendUserId = sendUserId;
        this.status = status;
        this.receiveTitle = receiveTitle;
        this.receiveDes = receiveDes;
        this.sendTitle = sendTitle;
        this.sendDes = sendDes;
    }


    @Column
    //师傅用户详情id
    private Integer receiveUserId;
    @Column
    //用户类型
    private Integer userType;
    @Column
    //下单的id
    private Integer publishId;
    @Column
    //发单用户id
    private Integer sendUserId;
    @Column
    //订单状态
    private Integer status;
    //时间
    private Date time;
    @Column
    //订单id
    private Integer orderId;
    @Column
    //展示给师傅的标题
    private String receiveTitle;
    @Column
    //展示给师傅的描述
    private String receiveDes;
    @Column
    //展现给发单用户的标题
    private String sendTitle;
    @Column
    //展现给发单用户的描述
    private String sendDes;
    @Deprecated
    @Column(columnDefinition = "bit default 0")
    private Boolean receiveShow;
    @Column(columnDefinition = "bit default 0")
    @Deprecated
    private Boolean receiveView;
    @Column(columnDefinition = "bit default 0")
    @Deprecated
    private Boolean sendShow;
    @Column(columnDefinition = "bit default 0")
    @Deprecated
    private Boolean sendView;

    @Column(columnDefinition = "bit default 0")
    @Deprecated
    private Boolean sendDelete;

    @Column(columnDefinition = "bit default 0")
    @Deprecated
    private Boolean receiveDelete;

    public Integer getPublishId() {
        return publishId;
    }

    public void setPublishId(Integer publishId) {
        this.publishId = publishId;
    }

    public Integer getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Integer sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Integer getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Integer receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Boolean getSendDelete() {
        return sendDelete;
    }

    public void setSendDelete(Boolean sendDelete) {
        this.sendDelete = sendDelete;
    }

    public Boolean getReceiveDelete() {
        return receiveDelete;
    }

    public void setReceiveDelete(Boolean receiveDelete) {
        this.receiveDelete = receiveDelete;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getReceiveTitle() {
        return receiveTitle;
    }

    public void setReceiveTitle(String receiveTitle) {
        this.receiveTitle = receiveTitle;
    }

    public String getReceiveDes() {
        return receiveDes;
    }

    public void setReceiveDes(String receiveDes) {
        this.receiveDes = receiveDes;
    }


    public String getSendTitle() {
        return sendTitle;
    }

    public void setSendTitle(String sendTitle) {
        this.sendTitle = sendTitle;
    }

    public String getSendDes() {
        return sendDes;
    }

    public void setSendDes(String sendDes) {
        this.sendDes = sendDes;
    }

    public Boolean getReceiveShow() {
        return receiveShow;
    }

    public void setReceiveShow(Boolean receiveShow) {
        this.receiveShow = receiveShow;
    }

    public Boolean getReceiveView() {
        return receiveView;
    }

    public void setReceiveView(Boolean receiveView) {
        this.receiveView = receiveView;
    }

    public Boolean getSendShow() {
        return sendShow;
    }

    public void setSendShow(Boolean sendShow) {
        this.sendShow = sendShow;
    }

    public Boolean getSendView() {
        return sendView;
    }

    public void setSendView(Boolean sendView) {
        this.sendView = sendView;
    }

    private static final long serialVersionUID = 1L;
    //start
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;

    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    //end

    @Override
    public String toString() {
        return "XzqnServiceLog{" +
                "sendUserId=" + sendUserId +
                ", receiveUserId=" + receiveUserId +
                ", orderId=" + orderId +
                ", status=" + status +
                ", time=" + time +
                ", userType=" + userType +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}

