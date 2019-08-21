package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.shop.XzqnShopAddr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/5/14 16:44
 * @Description:
 */
@Repository
@Transactional
public interface ShopAddrDao extends JpaRepository<XzqnShopAddr,Integer> {

    List<XzqnShopAddr> findAllByUserId(Integer userId);
}
