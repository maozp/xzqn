package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnPreServiceImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: hx
 * @Date: 2019/7/18 17:40
 * @Description:
 */
@Repository
public interface PreServiceImageDao extends JpaRepository<XzqnPreServiceImage,Integer> {
    XzqnPreServiceImage findByOrderId(Integer orderId);
}
