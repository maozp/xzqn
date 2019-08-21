package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gangukeji.xzqn.core.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hx
 * @Description 发单用户实体类, 第一次登录时自动创建
 * 用户详情及发单用户数据在 {@link XzqnUserReceive}中
 * 只有id有用别名sendUserId
 */

@Entity
@Table(name = "xzqn_user_send")
public class XzqnUserSend extends BaseEntity implements Serializable {
    @JsonProperty("sendUserId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private static final long serialVersionUID = 1L;

    @Column
    @Deprecated
    private String nickname;

    @Column
    @Deprecated
    private String headImg;
    private int isCheck;
    private String refuseReason;

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    @Column
    @Deprecated
    private String addrIds;

    @Column
    @Deprecated
    private Integer addrId;

    @Column
    @Deprecated
    private String mark;

    @Column
    @Deprecated
    private String name;

    @Column
    @Deprecated
    private String phone;

    @Column
    @Deprecated
    private String companyName;

    @Column
    @Deprecated
    private String companyAddr;

    @Column
    @Deprecated
    private String departmentName;

    @Column
    @Deprecated
    private BigDecimal balance;

    @Column(columnDefinition = "bit default 0")
    @Deprecated
    private Boolean isVip;

    @Column
    @Deprecated
    private Integer isAuth;
    @Column
    @Deprecated
    private String companyPhone;
    @Column
    @Deprecated
    private String idCardImgb;
    @Column
    @Deprecated
    private String idCardNum;
    @Column
    @Deprecated
    private String idCardImgp;
    private String licence;

    public String getIdCardImgb() {
        return idCardImgb;
    }

    public void setIdCardImgb(String idCardImgb) {
        this.idCardImgb = idCardImgb;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getIdCardImgp() {
        return idCardImgp;
    }

    public void setIdCardImgp(String idCardImgp) {
        this.idCardImgp = idCardImgp;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getAddrIds() {
        return addrIds;
    }

    public void setAddrIds(String addrIds) {
        this.addrIds = addrIds;
    }

    public Integer getAddrId() {
        return addrId;
    }

    public void setAddrId(Integer addrId) {
        this.addrId = addrId;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddr() {
        return companyAddr;
    }

    public void setCompanyAddr(String companyAddr) {
        this.companyAddr = companyAddr;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Boolean getIsVip() {
        return isVip;
    }

    public void setIsVip(Boolean isVip) {
        this.isVip = isVip;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }
    //start

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

    public Boolean getVip() {
        return isVip;
    }

    public void setVip(Boolean vip) {
        isVip = vip;
    }
}

