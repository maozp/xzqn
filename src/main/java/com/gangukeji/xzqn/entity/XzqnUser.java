package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gangukeji.xzqn.core.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
*@author hx
*@Description xzqn_user实体类
*/

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "xzqn_user")
@ToString
public class XzqnUser extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Column
	//发单用户id
	private Integer sendUserId;
	@JsonIgnore
	@Column
	//用户详情id
	private Integer receiveUserId;
	@JsonIgnore
	@Column
	@Deprecated
	private Integer shopUserId;
	@JsonIgnore
	@Column
	//用户名
	private String username;
	@JsonIgnore
	@Column
	//密码
	private String password;
	@Column
	private String openid;
	@Column
	private String userPhone;
	@Column
	@JsonIgnore
	private String unionid;
	@Column
	private String rongToken;
	@Column
	private String name;
	@Column
	private String userHeadImg;
	@Column
	private Integer userLevel;
	@Column
	private String mark;
	@Column
	private String city;

	public static long getSerialVersionUID() {
		return serialVersionUID;
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

	public Integer getShopUserId() {
		return shopUserId;
	}

	public void setShopUserId(Integer shopUserId) {
		this.shopUserId = shopUserId;
	}

	public String getUsername() {
		return username;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	//start
	@Id
	@JsonProperty("userId")
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
}

