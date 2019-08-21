package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.shop.XzqnShopOrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * {@link XzqnShopOrderMaster}
 */
@Repository
@Transactional
public interface ShopOrderMasterDao extends JpaRepository<XzqnShopOrderMaster, Integer> {
    //    List<XzqnShopOrderMaster> findAllByUserIdAndOrderStatus(Integer userId, Integer status);
    Page<XzqnShopOrderMaster> findAllByUserIdAndOrderStatusInOrderByOrderIdDesc(Integer userId, List<Integer> statusList, Pageable pageable);

    XzqnShopOrderMaster findByOrderId(Integer orderId);

    XzqnShopOrderMaster findByOrderNo(String orderNo);

    Page<XzqnShopOrderMaster> findAllByUserId(Integer userId, Pageable pageable);

    /*
     * 更新订单状态
     * */
    @Modifying
    @Query("update XzqnShopOrderMaster e set e.orderStatus=:orderStatus  where e.orderId=:orderId")
    void updateOrderStatus(@Param("orderStatus") int orderStatus, @Param("orderId") int orderId);

}
