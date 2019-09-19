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
import java.math.BigDecimal;
import java.util.List;

/**
 * {@link XzqnShopOrderMaster}
 */
@Repository
@Transactional
public interface ShopOrderMasterDao extends JpaRepository<XzqnShopOrderMaster, Integer> {

    //计算订单总数
    //select COUNT(order_id) from xzqn_shop_order_master where order_status>1
    @Query(value = "select COUNT(order_id) from xzqn_shop_order_master where order_status>1 ",nativeQuery=true)
    int findByOrderNums();

    //计算7天内订单数
    //select COUNT(order_id) from xzqn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND is_pay=1
    @Query(value = "select COUNT(order_id) from xzqn_shop_order_master where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND order_status>1",nativeQuery=true)
    int findByWeekOrderNums();

    //Select a.click_date,IFNULL(b.count,0) count  FROM (SELECT curdate() as click_date union all SELECT date_sub(curdate(), interval 1 day) as click_date union all SELECT date_sub(curdate(), interval 2 day) as click_date union all SELECT date_sub(curdate(), interval 3 day) as click_date union all SELECT date_sub(curdate(), interval 4 day) as click_date union all SELECT date_sub(curdate(), interval 5 day) as click_date union all SELECT date_sub(curdate(), interval 6 day) as click_date)a LEFT JOIN(select date(update_time) as datetime, count(order_id) as count from xzqn_shop_order_master WHERE order_status>1  group by date(update_time))b on a.click_date=b.datetime order by a.click_date asc
    //计算7天内折线订单数数量
    //select COUNT(order_id) from xzbn m,qn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND is_pay=1
    @Query(value = "Select IFNULL(b.count,0) count  FROM (SELECT curdate() as click_date union all SELECT date_sub(curdate(), interval 1 day) as click_date union all SELECT date_sub(curdate(), interval 2 day) as click_date union all SELECT date_sub(curdate(), interval 3 day) as click_date union all SELECT date_sub(curdate(), interval 4 day) as click_date union all SELECT date_sub(curdate(), interval 5 day) as click_date union all SELECT date_sub(curdate(), interval 6 day) as click_date)a LEFT JOIN(select date(update_time) as datetime, count(order_id) as count from xzqn_shop_order_master WHERE order_status>1  group by date(update_time))b on a.click_date=b.datetime order by a.click_date asc",nativeQuery=true)
    List<String> findByorderChartNums();

    //计算7天内折线订单数金额
    //select COUNT(order_id) from xzbn m,qn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND is_pay=1
    @Query(value = "Select IFNULL(b.count,0) count  FROM (SELECT curdate() as click_date union all SELECT date_sub(curdate(), interval 1 day) as click_date union all SELECT date_sub(curdate(), interval 2 day) as click_date union all SELECT date_sub(curdate(), interval 3 day) as click_date union all SELECT date_sub(curdate(), interval 4 day) as click_date union all SELECT date_sub(curdate(), interval 5 day) as click_date union all SELECT date_sub(curdate(), interval 6 day) as click_date)a LEFT JOIN(select date(update_time) as datetime, sum(order_money) as count from xzqn_shop_order_master where order_status>1 group by date(update_time) )b on a.click_date=b.datetime order by a.click_date asc",nativeQuery=true)
    List<String> findByorderChartMoney();

    //计算今天订单数
    //select COUNT(order_id) from xzqn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND is_pay=1
    @Query(value = "select COUNT(order_id) from xzqn_shop_order_master where TO_DAYS(update_time)=TO_DAYS(NOW()) AND order_status>1",nativeQuery=true)
    int findByTodayOrderNums();



    //计算已完成订单总数
    //SELECT COUNT(order_id) FROM xzqn_service_order
    @Query(value = "SELECT COUNT(order_id) FROM xzqn_shop_order_master where order_status=5",nativeQuery=true)
    int findByFinishOrderNums();

    //计算未完成订单总数
    //SELECT COUNT(order_id) FROM xzqn_service_order
    @Query(value = "SELECT COUNT(order_id) FROM xzqn_shop_order_master where order_status>1 AND order_status<5  ",nativeQuery=true)
    int findByUnfinishOrderNums();

    //计算昨日完成订单数
    //select COUNT(order_id) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW())
    @Query(value = "select COUNT(order_id) from xzqn_shop_order_master where TO_DAYS(update_time)=TO_DAYS(NOW())-1 AND order_status=5",nativeQuery=true)
    int findByYestodayOrderPayNums();
    //计算昨日付款金额数
    //select COUNT(order_id) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW())
    @Query(value = "select SUM(order_money) from xzqn_shop_order_master where TO_DAYS(update_time)=TO_DAYS(NOW())-1 AND order_status=5",nativeQuery=true)
    BigDecimal findByYestodayMoneyNums();

    //计算今日付款订单数
    //select COUNT(order_id) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW())
    @Query(value = "select COUNT(order_id) from xzqn_shop_order_master where TO_DAYS(update_time)=TO_DAYS(NOW()) AND order_status=5",nativeQuery=true)
    int findByTodayOrderPayNums();
    //计算今日付款金额数
    //select COUNT(order_id) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW())
    @Query(value = "select SUM(order_money) from xzqn_shop_order_master where TO_DAYS(update_time)=TO_DAYS(NOW())-1 AND order_status=5",nativeQuery=true)
    BigDecimal findByTodayMoneyNums();


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
