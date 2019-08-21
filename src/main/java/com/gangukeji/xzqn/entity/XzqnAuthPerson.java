package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gangukeji.xzqn.core.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author hx
 * @Description 个人认证
 */

@Entity
@Table(name = "xzqn_auth_person")
public class XzqnAuthPerson extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String refuseReason;
    @Column
    //身份证正面
    private String idCardImgP;
    //用户id
    private Integer userId;
    @Column
    //身份证背面
    private String idCardImgB;
    @Column
    //身份证姓名
    private String idCardName;
    @Column
    //身份证号码
    private String idCardNum;
    //是否审核0未认证 1已提交 2已认证
    private Integer isCheck;
    @Column
    //个人照片
    private String personalImg;
    @Column
    //用户详情(师傅)id
    private Integer receiveUserId;
    @Column
    //电话号码
    private String phone;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Integer receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCardName() {
        return idCardName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getIdCardImgP() {
        return idCardImgP;
    }

    public void setIdCardImgP(String idCardImgP) {
        this.idCardImgP = idCardImgP;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getIdCardImgB() {
        return idCardImgB;
    }

    public void setIdCardImgB(String idCardImgB) {
        this.idCardImgB = idCardImgB;
    }

    public String getPersonalImg() {
        return personalImg;
    }

    public void setPersonalImg(String personalImg) {
        this.personalImg = personalImg;
    }


    @Id
    @JsonProperty("authId")
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

    public Integer getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}



