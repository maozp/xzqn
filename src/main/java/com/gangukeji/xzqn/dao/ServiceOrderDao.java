package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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
