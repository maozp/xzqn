package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnServicePay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/12 19:00
 * @Description:
 */
@Repository
@Transactional
public interface ServicePayDao extends JpaRepository<XzqnServicePay, Integer> {
    List<XzqnServicePay> findByUserIdOrderByPayIdDesc(int userId);

    XzqnServicePay findByOrderId(int orderId);

    XzqnServicePay findByPayId(int payId);
}
