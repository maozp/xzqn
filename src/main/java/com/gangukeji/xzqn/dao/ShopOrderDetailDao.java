package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.shop.XzqnShopOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * {@link XzqnShopOrderDetail}
 */
@Repository
@Transactional
public interface ShopOrderDetailDao extends JpaRepository<XzqnShopOrderDetail,Integer> {
    List<XzqnShopOrderDetail> findAllByOrderId(Integer orderId);
}
