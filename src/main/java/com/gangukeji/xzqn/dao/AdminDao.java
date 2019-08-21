package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: hx
 * @Date: 2019/7/18 11:46
 * @Description:
 */
@Repository  //是springboot 为我们提供的简化类，默认提供了增删改查的方法
//我们只需要定义Reporistory接口就可以了，在SB启动的时候会自动帮我们生成具体的实现类，来实现CURD方法
//XzqnAdmin,Integer 使用JpaRepository需要传入实体类及主键的类型
public interface AdminDao extends JpaRepository<XzqnAdmin,Integer> {
    XzqnAdmin findByUsername(String username);
}
