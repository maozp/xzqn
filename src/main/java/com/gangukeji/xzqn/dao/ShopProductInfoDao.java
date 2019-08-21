package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.shop.XzqnShopProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * {@link XzqnShopProductInfo}
 */
@Repository
@Transactional
public interface ShopProductInfoDao extends JpaRepository<XzqnShopProductInfo, Integer> {
    Page<XzqnShopProductInfo> findAllByUserId(Integer userId, Pageable pageable);

    Page<XzqnShopProductInfo> findAllByCateId(Integer cateId,Pageable pageable);

    Page<XzqnShopProductInfo> findAllByCateIdIn(List<Integer> cateIds ,Pageable pageable);
}
