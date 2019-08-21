package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.shop.XzqnShopPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * {@link XzqnShopPay}
 */
@Repository
@Transactional
public interface ShopPayDao extends JpaRepository<XzqnShopPay, Integer> {
    boolean existsByTypeAndOrderId(int type, int orderId);

}
