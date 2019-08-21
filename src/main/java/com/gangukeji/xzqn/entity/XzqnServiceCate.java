package com.gangukeji.xzqn.entity;

import com.gangukeji.xzqn.core.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
*@author hx
*@Description xzqn_service_cate实体类
*/
@Deprecated
@Entity
@Table(name = "xzqn_service_cate")
public class XzqnServiceCate extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;


	@Column
	//是否是父节点
	private Integer isParent;

	@Column

	private String name;

	@Column
	private String mark;

	@Column
	private BigDecimal fee;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Integer getIsParent() {
		return isParent;
	}

	public void setIsParent(Integer isParent) {
		this.isParent = isParent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
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

