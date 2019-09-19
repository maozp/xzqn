package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.shop.XzqnShopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * {@link XzqnShopUser}
 */
@Repository
@Transactional
public interface ShopUserDao extends JpaRepository<XzqnShopUser, Integer> {

    //计算用户总数
    //SELECT COUNT(order_id) FROM xzqn_service_order
    @Query(value = "SELECT COUNT(user_id) FROM xzqn_shop_user ",nativeQuery=true)
    int findByPeopleNums();

    //计算7天之内注册的用户
    //SELECT COUNT(id) FROM xzqn_user where DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(create_time)
    @Query(value = "SELECT COUNT(user_id) FROM xzqn_shop_user where DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(create_time)",nativeQuery=true)
    int findByWeekPeopleNums();

    //今天注册的用户
    //SELECT COUNT(id) FROM xzqn_user where DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(create_time)
    @Query(value = "SELECT COUNT(user_id) FROM xzqn_shop_user where TO_DAYS(create_time)=TO_DAYS(NOW())",nativeQuery=true)
    int findByTodayPeopleNums();

//    XzqnShopUser findByUsernameAndPassword(String username, String password);
//    XzqnShopUser findByUsername(String username);
//    XzqnShopUser findFirstByOpenidOrderByUserIdDesc(String openid);
    XzqnShopUser findByOpenid(String openId);
//    boolean existsByOpenid(String openid);
}
