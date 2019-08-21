package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gangukeji.xzqn.core.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
*@author hx
*@Description 师傅提交的施工报告
*/

@Entity
@Table(name = "xzqn_receive_report")
public class XzqnReceiveReport extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;


	@Column
	//对应订单
	private Integer orderId;
	//师傅的userId
	private Integer userId;
	@Column
	//报告描述
	private String des;

	@Column
	@JsonIgnore
	//施工报告图片@分隔
	private String img;
	@Transient
	private String [] imgs;
	@Column
	//内容
	private String content;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getImg() {
		return img;
	}


	public String[] getImgs() {
		if (img == null) {
			img = "";
		}
		return img.split("@");
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setImg(String img) {
		this.img = img;
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

