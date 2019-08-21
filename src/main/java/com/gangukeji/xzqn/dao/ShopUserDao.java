package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.shop.XzqnShopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * {@link XzqnShopUser}
 */
@Repository
@Transactional
public interface ShopUserDao extends JpaRepository<XzqnShopUser, Integer> {
//    XzqnShopUser findByUsernameAndPassword(String username, String password);
//    XzqnShopUser findByUsername(String username);
//    XzqnShopUser findFirstByOpenidOrderByUserIdDesc(String openid);
    XzqnShopUser findByOpenid(String openId);
//    boolean existsByOpenid(String openid);
}
