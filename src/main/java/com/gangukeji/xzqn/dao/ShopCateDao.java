package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.shop.XzqnShopCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/5 14:07
 * @Description:
 */
@Repository
@Transactional
public interface ShopCateDao extends JpaRepository<XzqnShopCate,Integer> {
    List<XzqnShopCate> findAllByParentId(Integer id);
}
