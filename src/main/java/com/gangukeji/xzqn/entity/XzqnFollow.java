package com.gangukeji.xzqn.entity;

import com.gangukeji.xzqn.core.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
*@author hx
*@Description 关注实体
*/

@Entity
@Table(name = "xzqn_follow")
@NoArgsConstructor
@AllArgsConstructor
public class XzqnFollow extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column
	//用户id
	private Integer userId;
	@Column
	//被关注的用户id
	private Integer userIdFl;
	@Column(columnDefinition = "bit default 0")
	//关注状态
	private Boolean status;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserIdFl() {
		return userIdFl;
	}

	public void setUserIdFl(Integer userIdFl) {
		this.userIdFl = userIdFl;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

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

}

