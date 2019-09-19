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


    //select t1.*,t2.cate_name from xzqn_shop_product_info t1,xzqn_shop_cate t2
    //where t1.cate_id = t2.cate_id;
    @Query(value = "select t1.*,t2.cate_name from xzqn_shop_product_info t1,xzqn_shop_cate t2 where t1.cate_id = t2.cate_id ",nativeQuery=true)
    List<Object> findAllandCateName();

    Page<XzqnShopProductInfo> findAllByUserId(Integer userId, Pageable pageable);

    Page<XzqnShopProductInfo> findAllByCateId(Integer cateId,Pageable pageable);

    Page<XzqnShopProductInfo> findAllByCateIdIn(List<Integer> cateIds ,Pageable pageable);
}
