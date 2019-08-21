package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnClock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @Author: hx
 * @Date: 2019/5/17 13:44
 * @Description:
 */
@Repository
@Transactional
public interface ClockDao extends JpaRepository<XzqnClock, Integer> {
    XzqnClock findByPublishId(Integer publishId);
    XzqnClock findByOrderId(Integer OrderId);

}
