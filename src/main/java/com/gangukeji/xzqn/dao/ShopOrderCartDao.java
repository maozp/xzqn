package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.shop.XzqnShopOrderCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * {@link XzqnShopOrderCart}
 */
@Repository
@Transactional
public interface ShopOrderCartDao extends JpaRepository<XzqnShopOrderCart,Integer> {
    Optional<XzqnShopOrderCart> findByUserIdAndProductId(Integer userId, Integer productId);
    List<XzqnShopOrderCart> findByUserId(Integer userId);
    void deleteByCartIdIn(List<Integer> ids);
}
