package com.gangukeji.xzqn.core;


import java.util.Date;

public abstract class BaseEntity implements java.io.Serializable {

    public abstract Integer getId();

    public abstract void setId(Integer id);

    public abstract Date getCreateTime();

    public abstract void setCreateTime(Date createTime);

    public abstract Date getUpdateTime();

    public abstract void setUpdateTime(Date updateTime);
}
