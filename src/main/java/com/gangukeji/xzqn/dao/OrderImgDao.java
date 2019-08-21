package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnOrderImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author: hx
 * @Date: 2019/7/18 11:46
 * @Description:
 */
@Repository
public interface OrderImgDao extends JpaRepository<XzqnOrderImg, Integer> {
    XzqnOrderImg findByPublishId(Integer publishId);

    XzqnOrderImg findByOrderId(Integer orderId);

    @Modifying
    @Transactional
    @Query("update XzqnOrderImg set orderId=?1 where publishId=?2")
    int updateOrderImg(int orderId, int publishId);
}
