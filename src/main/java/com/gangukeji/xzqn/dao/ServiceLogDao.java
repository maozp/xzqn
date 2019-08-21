package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnServiceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/5/16 21:34
 * @Description:
 */
@Repository
@Transactional
public interface ServiceLogDao extends JpaRepository<XzqnServiceLog, Integer> {

    List<XzqnServiceLog> findAllByPublishId(Integer publishId);

    List<XzqnServiceLog> findAllByOrderId(Integer orderId);

    //    @Modifying
//    @Query("update XzqnServiceLog set publishId=?1,status=?2")
//    void updateByPublishIdAndStatus(int publishId, int status);
//    @Modifying
//    @Query("update XzqnServiceLog set orderId=?1,status=?2")
//    void updateByOrderIdAndStatus(int orderId, int status);
    @Modifying
    @Query("update XzqnServiceLog set time=:time,userType=:userType where publishId=:publishId and status=:status")
    void updateByPublishIdAndStatus(@Param("publishId") int publishId, @Param("status") int status, @Param("time") Date time, @Param("userType") int userType);

    @Modifying
    @Query("update XzqnServiceLog set time=:time,userType=:userType where orderId=:orderId and status=:status")
    void updateByOrderIdAndStatus(@Param("orderId") int publishId, @Param("status") int status, @Param("time") Date time, @Param("userType") int userType);

    @Modifying
    @Query("update XzqnServiceLog set orderId=:orderId ,receiveUserId=:receiveUserId where publishId=:publishId")
    void updateOrderIdAndReceiveUserIdByPublishId(@Param("publishId") int publishId, @Param("orderId") int orderId, @Param("receiveUserId") int receiveUserId);

}
