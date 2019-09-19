package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gangukeji.xzqn.core.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
*@author hx
*@Description 技能认证信息表
*/

@Entity //告诉springboot这是一个实体类，在SB启动的时候会加载这个类
@Table(name = "xzqn_auth_skill")  //xzqn_auth_skill对应xzqn_auth_skill表
public class XzqnAuthSkill extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String refuseReason;
	@Column(columnDefinition = "TEXT")
	private String skillCertificate;
	//用户id 如果属性名与数据库字段名相同可以省略@Column，但不建议这么使用
	private Integer userId;
	@Column
	//师傅id
	private Integer receiveUserId;
	//是否审核0未认证 1已提交 2已认证
	private Integer isCheck;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getSkillCertificate() {
		return skillCertificate;
	}

	public void setSkillCertificate(String skillCertificate) {
		this.skillCertificate = skillCertificate;
	}

	public Integer getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(Integer receiveUserId) {
		this.receiveUserId = receiveUserId;
	}


	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	//start
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

	public Integer getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}

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

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
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


}

