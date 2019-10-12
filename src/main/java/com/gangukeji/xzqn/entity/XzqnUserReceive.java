package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author hx
 * @Description 用户详情实体类,用于存放师傅信息和发单方信息
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class XzqnUserReceive {
    /**
     * 用于JPA的查找
     */
    public XzqnUserReceive(Integer authSkillId, String phone, Integer authUserId, Integer authEnterpriseId, String nickname, String addrName, String addrDes, String headImg, String mark, String cate2_ids, String area, String des, String promise, BigDecimal serveTime, BigDecimal turnover90, BigDecimal balance, BigDecimal lat, BigDecimal lng, BigDecimal backPercentage, Integer serveCount, Integer complaintCount, Integer goodCount, Integer starts, String enterArea, Integer badCount, Boolean isGuarantee, Boolean isBan, Boolean isAuth, String authStatus, String companyName, String name, Integer rate, String goodAt, String authType, String location, Date createTime, Date updateTime, String introduceIds, String introduce) {
        this.authSkillId = authSkillId;
        this.phone = phone;
        this.authUserId = authUserId;
        this.authEnterpriseId = authEnterpriseId;
        this.nickname = nickname;
        this.addrName = addrName;
        this.addrDes = addrDes;
        this.headImg = headImg;
        this.mark = mark;
        this.cate2_ids = cate2_ids;
        this.area = area;
        this.des = des;
        this.promise = promise;
        this.serveTime = serveTime;
        this.turnover90 = turnover90;
        this.balance = balance;
        this.lat = lat;
        this.lng = lng;
        this.backPercentage = backPercentage;
        this.serveCount = serveCount;
        this.complaintCount = complaintCount;
        this.goodCount = goodCount;
        this.starts = starts;
        this.enterArea = enterArea;
        this.badCount = badCount;
        this.isGuarantee = isGuarantee;
        this.isBan = isBan;
        this.isAuth = isAuth;
        this.authStatus = authStatus;
        this.companyName = companyName;
        this.name = name;
        this.rate = rate;
        this.goodAt = goodAt;
        this.authType = authType;
        this.location = location;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.introduceIds = introduceIds;
        this.introduce = introduce;
    }
    @Deprecated
    //技能认证id
    private Integer authSkillId;
    //电话
    private String phone;
    @Deprecated
    //个人认证id
    private Integer authUserId;
    @Deprecated
    //企业认证id
    private Integer authEnterpriseId;
    //昵称
    private String nickname;
    //地址描述
    private String addrDes;
    @JsonRawValue
    private String authJson;
    //头像
    private String headImg;
    //个性签名
    private String mark;
    @Deprecated
    //用于存放师傅擅长的分类ids
    private String cate2_ids;
    //服务地区逗号分隔
    private String area;
    //服务描述
    private String des;
    //服务承诺
    private String promise;
    //服务时长
    private BigDecimal serveTime;
    //90天回头率
    private BigDecimal turnover90;
    @Deprecated
    //余额
    private BigDecimal balance;
    //纬度
    private BigDecimal lat;
    //经度
    private BigDecimal lng;
    //回头率
    private BigDecimal backPercentage;
    //服务数
    private Integer serveCount;
    //投诉数
    private Integer complaintCount;
    //好评数
    private Integer goodCount;
    @Deprecated
    //被关注数
    private Integer starts;

    //我关注的数
    private String myStarNums;

    @Transient
    //用户id
    private Integer userId;
    //入驻地区
    private String enterArea;
    //差评数
    private Integer badCount;
    @Column(columnDefinition = "bit default 0")
    @Deprecated
    //是否交保证金
    private Boolean isGuarantee;
    @Column(columnDefinition = "bit default 0")
    @Deprecated
    //是否禁用师傅
    private Boolean isBan;
    @Column(columnDefinition = "bit default 0")
    @Deprecated
    //是否认证
    private Boolean isAuth;
    @Column(columnDefinition = "varchar(3) default '000'")
    //认证状态
    private String authStatus;//000都是未认证
    //公司名称
    private String companyName;
    //师傅名字
    private String name;
    //好评率
    private Integer rate;
    //擅长
    private String goodAt;
    //是否关注该师傅
    @Transient
    private boolean star;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    //认证类型
    private String authType;
    //所属地区
    private String location;
    //地址名称
    private String addrName;

    public String getAddrName() {
        return addrName;
    }

    //用户详情id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("receiveUserId")
    private Integer id;

    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;

    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;
    //擅长的服务分类ids
    private String introduceIds;
    @JsonIgnore
    //服务介绍,隔开
    private String introduce;
    @Transient
    //服务介绍数组
    private List<String> introduceList;

    //拿数据库查到的introduce放到introduceList返回给前端
    public List<String> getIntroduceList() {
        try {
            return Arrays.asList(introduce.split("@"));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

