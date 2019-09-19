package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: hx
 * @Date: 2019/5/14 14:09
 * @Description:
 */
@Repository
@Transactional
public interface ServiceOrderDao extends JpaRepository<XzqnServiceOrder, Integer> {

    //计算订单总数
    //SELECT COUNT(order_id) FROM xzqn_service_order
    @Query(value = "SELECT COUNT(order_id) FROM xzqn_service_order ",nativeQuery=true)
    int findByOrderNums();

    //计算订单好评率
    //SELECT CONCAT(FORMAT(b.count/a.count*100,2) ,'%') as sum From (SELECT count(id) AS count FROM xzqn_service_comment )a,(SELECT count(id) AS count FROM xzqn_service_comment where grade>=4)b
    @Query(value = "SELECT CONCAT(FORMAT(b.count/a.count*100,2) ,'%') as sum From (SELECT count(id) AS count FROM xzqn_service_comment )a,(SELECT count(id) AS count FROM xzqn_service_comment where grade>=4)b ",nativeQuery=true)
    String findByOrderLikeNums();

    //计算本月内订单数
    //SELECT COUNT(order_id) FROM xzqn_service_order WHERE DATE_FORMAT( create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) AND is_cancel=0
    @Query(value = "SELECT COUNT(order_id) FROM xzqn_service_order WHERE DATE_FORMAT( create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) AND is_cancel=0",nativeQuery=true)
    int findByMonthOrderNums();

    //计算本月成交金额数
    @Query(value = "SELECT SUM(total_fee) FROM xzqn_service_order WHERE DATE_FORMAT( update_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) AND is_pay=1",nativeQuery=true)
    BigDecimal findByMonthOrderMoney();

    //计算6个月订单折线图时间
    //SELECT a.click_date,IFNULL(b.con,0) from (SELECT DATE_FORMAT(curdate(),'%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 1 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 2 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 3 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 4 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 5 MONTH), '%Y-%m') as click_date )a LEFT JOIN (SELECT  DATE_FORMAT(create_time,'%Y-%m') as mon, COUNT(*) as con FROM xzqn_service_order GROUP BY mon)b ON a.click_date = b.mon
    @Query(value = "SELECT a.click_date from (SELECT DATE_FORMAT(curdate(),'%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 1 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 2 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 3 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 4 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 5 MONTH), '%Y-%m') as click_date )a LEFT JOIN (SELECT  DATE_FORMAT(create_time,'%Y-%m') as mon, COUNT(*) as con FROM xzqn_service_order where is_cancel=0 GROUP BY mon)b ON a.click_date = b.mon ORDER BY a.click_date asc ",nativeQuery=true)
    List<String> findByMonthOrderChartDate();
    //计算6个月订单折线图数量
    //SELECT a.click_date,IFNULL(b.con,0) from (SELECT DATE_FORMAT(curdate(),'%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 1 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 2 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 3 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 4 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 5 MONTH), '%Y-%m') as click_date )a LEFT JOIN (SELECT  DATE_FORMAT(create_time,'%Y-%m') as mon, COUNT(*) as con FROM xzqn_service_order GROUP BY mon)b ON a.click_date = b.mon
    @Query(value = "SELECT IFNULL(b.con,0) from (SELECT DATE_FORMAT(curdate(),'%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 1 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 2 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 3 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 4 MONTH), '%Y-%m') as click_date union all SELECT DATE_FORMAT((CURDATE() - INTERVAL 5 MONTH), '%Y-%m') as click_date )a LEFT JOIN (SELECT  DATE_FORMAT(create_time,'%Y-%m') as mon, COUNT(*) as con FROM xzqn_service_order where is_cancel=0 GROUP BY mon)b ON a.click_date = b.mon ORDER BY a.click_date asc ",nativeQuery=true)
    List<String> findByMonthOrderChartCount();

    //计算未接过单的用户
    //select a.sum-b.sum from (SELECT COUNT(receive_user_id) AS sum FROM xzqn_user)a,(SELECT COUNT(distinct receive_user_id) AS sum FROM xzqn_service_order)b
    @Query(value = "select a.sum-b.sum from (SELECT COUNT(receive_user_id) AS sum FROM xzqn_user)a,(SELECT COUNT(distinct receive_user_id) AS sum FROM xzqn_service_order)b",nativeQuery=true)
    int findByUnreceiveUser();

    //计算7天内订单数
    //select COUNT(order_id) from xzqn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND is_pay=1
    @Query(value = "select COUNT(order_id) from xzqn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(create_time) AND is_cancel=0",nativeQuery=true)
    int findByWeekOrderNums();

    //计算7天内折线订单数日期
    //select COUNT(order_id) from xzbn m,qn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND is_pay=1
    @Query(value = "SELECT curdate() as click_date union all SELECT date_sub(curdate(), interval 1 day) as click_date union all SELECT date_sub(curdate(), interval 2 day) as click_date union all SELECT date_sub(curdate(), interval 3 day) as click_date union all SELECT date_sub(curdate(), interval 4 day) as click_date union all SELECT date_sub(curdate(), interval 5 day) as click_date union all SELECT date_sub(curdate(), interval 6 day) as click_date order by click_date asc",nativeQuery=true)
    List<String> findByorderChart();
    //计算7天内折线订单数数量
    //select COUNT(order_id) from xzbn m,qn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND is_pay=1
    @Query(value = "Select IFNULL(b.count,0) count  FROM (SELECT curdate() as click_date union all SELECT date_sub(curdate(), interval 1 day) as click_date union all SELECT date_sub(curdate(), interval 2 day) as click_date union all SELECT date_sub(curdate(), interval 3 day) as click_date union all SELECT date_sub(curdate(), interval 4 day) as click_date union all SELECT date_sub(curdate(), interval 5 day) as click_date union all SELECT date_sub(curdate(), interval 6 day) as click_date)a LEFT JOIN(select date(create_time) as datetime, count(order_id) as count from xzqn_service_order where is_cancel=0  group by date(create_time))b on a.click_date=b.datetime order by a.click_date asc",nativeQuery=true)
    List<String> findByorderChartNums();

    //Select IFNULL(b.count,0) count  FROM (SELECT curdate() as click_date union all SELECT date_sub(curdate(), interval 1 day) as click_date union all SELECT date_sub(curdate(), interval 2 day) as click_date union all SELECT date_sub(curdate(), interval 3 day) as click_date union all SELECT date_sub(curdate(), interval 4 day) as click_date union all SELECT date_sub(curdate(), interval 5 day) as click_date union all SELECT date_sub(curdate(), interval 6 day) as click_date)a LEFT JOIN(select date(update_time) as datetime, sum(total_fee) as count from xzqn_service_order where is_pay=1 group by date(update_time) )b on a.click_date=b.datetime order by a.click_date asc
    //计算7天内折线订单数金额
    //select COUNT(order_id) from xzbn m,qn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND is_pay=1
    @Query(value = "Select IFNULL(b.count,0) count  FROM (SELECT curdate() as click_date union all SELECT date_sub(curdate(), interval 1 day) as click_date union all SELECT date_sub(curdate(), interval 2 day) as click_date union all SELECT date_sub(curdate(), interval 3 day) as click_date union all SELECT date_sub(curdate(), interval 4 day) as click_date union all SELECT date_sub(curdate(), interval 5 day) as click_date union all SELECT date_sub(curdate(), interval 6 day) as click_date)a LEFT JOIN(select date(update_time) as datetime, sum(total_fee) as count from xzqn_service_order where is_pay=1 group by date(update_time) )b on a.click_date=b.datetime order by a.click_date asc",nativeQuery=true)
    List<String> findByorderChartMoney();

    //计算今天订单数
    //select COUNT(order_id) from xzqn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND is_pay=1
    @Query(value = "select COUNT(order_id) from xzqn_service_order where TO_DAYS(create_time)=TO_DAYS(NOW()) AND is_cancel=0",nativeQuery=true)
    int findByTodayOrderNums();

    //计算已完成支付订单总数
    //SELECT COUNT(order_id) FROM xzqn_service_order
    @Query(value = "SELECT COUNT(order_id) FROM xzqn_service_order where is_pay=1",nativeQuery=true)
    int findByFinishOrderNums();

    //计算未完成支付订单总数
    //SELECT COUNT(order_id) FROM xzqn_service_order
    @Query(value = "SELECT COUNT(order_id) FROM xzqn_service_order where is_pay=0 ",nativeQuery=true)
    int findByUnfinishOrderNums();

    //计算昨日付款订单数
    //select COUNT(order_id) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW())
    @Query(value = "select COUNT(order_id) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW())-1 AND is_pay=1",nativeQuery=true)
    int findByYestodayOrderPayNums();
    //计算昨日付款金额数
    //select COUNT(order_id) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW())
    @Query(value = "select SUM(total_fee) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW())-1 AND is_pay=1",nativeQuery=true)
    BigDecimal findByYestodayMoneyNums();

    //计算今日付款订单数
    //select COUNT(order_id) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW())
    @Query(value = "select COUNT(order_id) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW()) AND is_pay=1",nativeQuery=true)
    int findByTodayOrderPayNums();
    //计算今日付款金额数
    //select COUNT(order_id) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW())
    @Query(value = "select SUM(total_fee) from xzqn_service_order where TO_DAYS(update_time)=TO_DAYS(NOW()) AND is_pay=1",nativeQuery=true)
    BigDecimal findByTodayMoneyNums();

    List<XzqnServiceOrder> findBySendUserIdAndStatusIn(Integer sendUserId, int[] statusArray);

    List<XzqnServiceOrder> findByReceiveUserIdAndStatusIn(Integer receiveUserId, int[] statusArray);

    List<XzqnServiceOrder> findAllBySendUserId(Integer sendUserId);

    List<XzqnServiceOrder> findAllByReceiveUserId(Integer receiveUserId);

    Optional<XzqnServiceOrder> findByPublishId(Integer publishId);

    XzqnServiceOrder findByOrderNo(String orderNo);

    List<XzqnServiceOrder> findByCateId(Integer cateId);

    /*
     * 更新订单状态
     * */
    @Modifying
    @Query("update XzqnServiceOrder set status=:status where id=:orderId")
    void updateOrderStatus(@Param("status") int status, @Param("orderId") int orderId);

    @Query(value = "select createTime from XzqnServiceOrder where id =?1")
    Date getCreateTimeById(Integer orderId);

    List<XzqnServiceOrder> findByUserIdAndStatusIn(int userId, int[] statusArray);

    List<XzqnServiceOrder> findAllByUserIdAndStatusIn(Integer userId, int[] statusArray);

    List<XzqnServiceOrder> findAllByUserId(Integer userId);

    @Query("select new XzqnServiceOrder( orderNo,  userId,  sendUserId,  receiveUserId,  publishId,  isPay,  totalFee,  serviceFee,  status,  createTime,orderId) from XzqnServiceOrder where sendUserId=?1 and status>?2")
    List<XzqnServiceOrder> findBySendUserIdAndStatusMoreThenValue(Integer sendUserId, Integer i);

    @Query("select new XzqnServiceOrder( orderNo,  userId,  sendUserId,  receiveUserId,  publishId,  isPay,  totalFee,  serviceFee,  status,  createTime,orderId) from XzqnServiceOrder where sendUserId=?1 and status<?2")
    List<XzqnServiceOrder> findBySendUserIdAndStatusLessThanValue(Integer sendUserId, Integer i);

    @Query("select new XzqnServiceOrder( orderNo,  userId,  sendUserId,  receiveUserId,  publishId,  isPay,  totalFee,  serviceFee,  status,  createTime,orderId) from XzqnServiceOrder where receiveUserId=?1 and status>?2")
    List<XzqnServiceOrder> findByReceiveUserIdAndStatusMoreThenValue(Integer receiveUserId, int i);

    @Query(value = "select new XzqnServiceOrder( orderNo,  userId,  sendUserId,  receiveUserId,  publishId,  isPay,  totalFee,  serviceFee,  status,  createTime,orderId) from XzqnServiceOrder where receiveUserId=?1 and status<?2")
    List<XzqnServiceOrder> findByReceiveUserIdAndStatusLessThenValue(Integer receiveUserId, Integer i);

    @Modifying
    @Query("update XzqnServiceOrder set isPay=?1 where orderNo=?2")
    void updateIsPay(boolean b, String orderNo);

    @Modifying
    @Query("update XzqnServiceOrder set isPay=?1 where orderId=?2")
    void updateIsPayByOrderId(boolean b, Integer orderId);

    @Query("select status from XzqnServiceOrder where orderId=?1")
    Integer getStatus(int orderId);

    @Query("select publishId from XzqnServiceOrder where orderId=?1")
    int getPublishIdByOrderId(int orderId);

    /**
     * 支付记录对应的订单id和师傅id
     * @param orderId
     * @return
     */
    @Query("select publishId,receiveUserId from XzqnServiceOrder where orderId=?1")
    Object getPublishIdAndReceiveUserIdByOrderId(int orderId);
    /**
     * 支付记录对应的订单id和发单方id
     * @param orderId
     * @return
     */
    @Query("select publishId,sendUserId from XzqnServiceOrder where orderId=?1")
    Object getPublishIdAndSendUserIdByOrderId(int orderId);

//    @Modifying
//    @Query("update XzqnServiceOrder set receiveUserId=:receiveUserId where orderId=:orderId")
//    void updateReceiveUserId(@Param("orderId") int orderId, @Param("receiveUserId") int receiveUserId);

    /*
     * 发单用户查找未完成订单
     * */
//    @Query("select ServiceOrderFindAllResp from XzqnServiceOrder left  join XzqnServicePublish on XzqnServicePublish.id=XzqnServiceOrder.publishId where status=:status and sendUserId=:sendUserId")
//    @Query("select ServiceOrderFindAllResp from XzqnServiceOrder o  left join XzqnServicePublish p on p.id = o.publishId where o.status = :status   and o.sendUserId = :sendUserId")
//    List<ServiceOrderFindAllResp> findBySendUserIdLeftJoinPublish(@Param("status") int status, @Param("sendUserId") int sendUserId);

//    @Query("SELECT new com.jeejava.dto.DeptEmpDto(d.name, e.name, e.email, e.address) "
//            + "FROM Department d LEFT JOIN d.employees e")
}
