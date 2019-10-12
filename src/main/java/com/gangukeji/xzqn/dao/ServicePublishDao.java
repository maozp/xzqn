package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnServicePublish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/5/14 11:32
 * @Description:
 */
@Repository
@Transactional
public interface ServicePublishDao extends JpaRepository<XzqnServicePublish, Integer> {

    //计算发布订单总数
    //SELECT COUNT(order_id) FROM xzqn_service_order
    @Query(value = "SELECT count(id) FROM `xzqn_service_publish` WHERE is_cancel=0 ",nativeQuery=true)
    int findByOrderNums();
    //计算7天内订单数
    //select COUNT(order_id) from xzqn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND is_pay=1
    @Query(value = "select COUNT(id) from xzqn_service_publish where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(create_time) AND is_cancel=0",nativeQuery=true)
    int findByWeekOrderNums();
    //计算今天订单数
    //select COUNT(order_id) from xzqn_service_order where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) AND is_pay=1
    @Query(value = "select COUNT(id) from xzqn_service_publish where TO_DAYS(create_time)=TO_DAYS(NOW()) AND is_cancel=0",nativeQuery=true)
    int findByTodayOrderNums();
    //计算本月内订单数
    //SELECT COUNT(order_id) FROM xzqn_service_order WHERE DATE_FORMAT( create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) AND is_cancel=0
    @Query(value = "SELECT COUNT(id) FROM xzqn_service_publish WHERE DATE_FORMAT( create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) AND is_cancel=0",nativeQuery=true)
    int findByMonthOrderNums();

//    List<XzqnServicePublish> findByStatusAndSendUserId(Integer status, Integer sendUserId);

    List<XzqnServicePublish> findByUserIdAndStatusIn(Integer sendUserId, int[] statusArray);

    List<XzqnServicePublish> findAllByStatusInAndIsCancel(int[] statusArray, Pageable pageable, boolean isCancel);

    List<XzqnServicePublish> findAllByStatusInAndIsCancelAndIsCheckIs(int[] statusArray, Pageable pageable, boolean isCancel, int isCheck);

    List<XzqnServicePublish> findAllByStatusInAndIsCancelAndJsonContaining(int[] statusArray, Pageable pageable, boolean isCancel, String json);

    List<XzqnServicePublish> findAllByStatusInAndIsCancelAndJsonContainingAndIsCheckIs(int[] statusArray, Pageable pageable, boolean isCancel, String json, int isCheck);

    List<XzqnServicePublish> findByStatus(Integer status);

    List<XzqnServicePublish> findByStatusAndCateId(Integer status, Integer cateId);

    List<XzqnServicePublish> findAllBySendUserId(Integer sendUserId);

//    @Query("select XzqnServicePublish from XzqnServicePublish where status=4 or status=5")
//    List<XzqnServicePublish> findAllByStatus4And5(Pageable pageRequest);

    //    @Query("select id from XzqnServicePublish where id=:id")
//    List<GrabListView> findAll(@Param("id") Integer id);

    /**
     * 抢单列表publish信息
     * @param cateIds
     * @param pageRequest
     * @return
     */
//    @Query("select new com.gangukeji.xzqn.entity.view.GrabListView(id,cateIds,propIds,status,serviceAddr,serviceDesc,totalFee,lat,lng,createTime,isCancel) from XzqnServicePublish where (status=4 or status=5) and cateIds in ?1")
//    List<GrabListView> findAllByStatus4And5AndFilterByCateIds(List<Integer> cateIds, PageRequest pageRequest);
    //    List<XzqnServicePublish> findByStatusAndIdBetween(Integer status,Integer s,Integer e);
    //    Integer countByStatus(Integer status);
//    XzqnServicePublish findTopBySendUserId(Integer sendUserId);
//    Timestamp findFirstBySendUserId(Integer sendUserId);

    /**
     * 查找最新记录时间,防止频繁下单
     *
     * @param sendUserId
     * @param pageRequest
     * @return
     */
    @Query(value = "select createTime from XzqnServicePublish where sendUserId =?1")
    Date findNewPublishCreateTime(Integer sendUserId, PageRequest pageRequest);

    /**
     * 更新publish status
     *
     * @param status
     * @param publishId
     */
    @Modifying
    @Query("update XzqnServicePublish set status=:status where id=:publishId")
    void updatePublishStatus(@Param("status") int status, @Param("publishId") int publishId);

    @Query(value = "select createTime from XzqnServicePublish where id =?1")
    Date getCreateTimeById(Integer publishId);

    //订单状态(0 -> 1 -> -1 )排序
    /**
     * 通过原生sql 进行查询
     * 开启nativeQuery=true，在value里可以用原生SQL语句完成查询
     */
    @Query("select u from XzqnServicePublish u ORDER BY CASE u.isCheck when 0 then 1 when 1 then 2 when -1 then 3 when 9 then 4 end ")
    Page<XzqnServicePublish> findAllByIsCheck1(Pageable pageable);

    @Query("select new XzqnServicePublish(id,sendUserId,  name,  phone,  userId,  factorId,  companyName,  companyPhone,  serviceDesc,  serviceAddr,  lat,  lng,  totalFee,  isCancel,  status,  createTime,  json,serviceFee,receiveUserIdList,isCheck,refuseReason)from XzqnServicePublish where sendUserId=?1 and status<?2")
    List<XzqnServicePublish> findBySendUserIdAndStatusLessThanValue(Integer sendUserId, int i);


    @Query("select new XzqnServicePublish(id,sendUserId,  name,  phone,  userId,  factorId,  companyName,  companyPhone,  serviceDesc,  serviceAddr,  lat,  lng,  totalFee,  isCancel,  status,  createTime,  json,serviceFee,receiveUserIdList,isCheck,refuseReason) from XzqnServicePublish where sendUserId=?1 and status=?2")
    List<XzqnServicePublish> findAllBySendUserIdAndStatus(Integer sendUserId, int i);

    @Modifying
    @Query("update XzqnServicePublish set isCancel=true ,status=:status where id=:publishId ")
    void updateIsCancelAndStatus(@Param("publishId") int publishId, @Param("status") int status);

    @Modifying
    @Query("update XzqnServicePublish set receiveUserIdList=concat(receiveUserIdList,:receiveUserId) where id=:publishId ")
    void updateReceiveUserIdListByPublishId(@Param("receiveUserId") String receiveUserId, @Param("publishId") Integer publishId);

    @Modifying
    @Query("update XzqnServicePublish set receiveUserIdList=:receiveUserIdAll where id=:publishId ")
    void updateReceiveUserIdListAllByPublishId(@Param("receiveUserIdAll") String receiveUserIdAll, @Param("publishId") Integer publishId);

    @Query("select receiveUserIdList from XzqnServicePublish where id=?1")
    String getReceiveUserIdList(int publishId);

    @Query("select serviceDesc from XzqnServicePublish where id=?1")
    String getServiceDes(int publishId);

    @Modifying
    @Query("update XzqnServicePublish set isCheck=?1 where id=?2")
    int updateIsCheckByPublishId(Integer isCheck, Integer publishId);

    @Modifying
//    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout",value = "100")})
//    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("update XzqnServicePublish set isCheck=?1 ,refuseReason=?2 where id=?3")
    int updateIsCheckAndRefuseReasonByPublishId(Integer isCheck, String refuseReason, Integer publishId);

    /**
     * 测试行锁
     * @param id
     * @return
     */
//    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
//    @Query("select XzqnServicePublish from XzqnServicePublish where id=?1")
//    XzqnServicePublish findByIdLock(int id);

//    @Query("select new XzqnServicePublish(id,sendUserId,  name,  phone,  userId,  factorId,  companyName,  companyPhone,  serviceDesc,  serviceAddr,  lat,  lng,  totalFee,  isCancel,  status,  createTime,  json,serviceFee) from XzqnServicePublish where status=:status1 or status=:status2 and isCancel=:cancel and json like CONCAT('%',:name,'%') ")
//    List<XzqnServicePublish> findAllByStatusInAndIsCancelAndSearchByName(@Param("status1") int status1,@Param("status2")int status2,  @Param("cancel")boolean cancel,@Param("name")String name,PageRequest pageRequest);

//    @Query("select new XzqnServicePublish(id,sendUserId,  name,  phone,  userId,  factorId,  companyName,  companyPhone,  serviceDesc,  serviceAddr,  lat,  lng,  totalFee,  isCancel,  status,  createTime,  json,serviceFee) from XzqnServicePublish where  status=4 or status=5 ")
//    List<XzqnServicePublish> findSearch(PageRequest pageRequest);
//    @Query(value = "select * from XzqnServicePublish where  status=4 or status=5 ",nativeQuery = true)
//    List<XzqnServicePublish> findSearchNatice(PageRequest pageRequest);

//    @Query("select new XzqnServicePublish(id,sendUserId,  name,  phone,  userId,  factorId,  companyName,  companyPhone,  serviceDesc,  serviceAddr,  lat,  lng,  totalFee,  isCancel,  status,  createTime,  json,serviceFee) from XzqnServicePublish where json=:name and status=4 or status=5 ")
//    List<XzqnServicePublish> findSearchByName(@Param("name") String name, PageRequest pageRequest);
}
