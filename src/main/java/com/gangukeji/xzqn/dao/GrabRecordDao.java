package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnGrabRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/5/15 11:02
 * @Description:
 */
@Repository
@Transactional
public interface GrabRecordDao extends JpaRepository<XzqnGrabRecord, Integer> {
    @Modifying
    @Query("update XzqnGrabRecord set grabStatus=:grabStatus where userId=:userId and publishId=:publishId")
    void updateGrabStatusByPublishIdWhereUserIdIs(@Param("publishId") int publishId, @Param("userId") int userId, @Param("grabStatus") int grabStatus);

    @Modifying
    @Query("update XzqnGrabRecord set grabStatus=:grabStatus where  publishId=:publishId")
    void updateByPublishIdGrabStatusId(@Param("publishId") int publishId, @Param("grabStatus") int grabStatus);

    void deleteByPublishIdAndUserId(int publishId,int userId);
    List<XzqnGrabRecord> findByUserIdAndGrabStatusIn(int userId, int[] grabStatus);

    /**
     * 抢单记录表查到publish 的 ids
     * @param userId
     * @param grabStatus (-1已被抢,0等待确认,1确认抢单,2没有枪)
     * @return
     */
    List<XzqnGrabRecord> findByUserIdAndGrabStatus(int userId, int grabStatus);

    List<XzqnGrabRecord> findByPublishIdOrderByIdDesc(int publishId);
    XzqnGrabRecord findByPublishIdAndUserId(int publishId,int userId);

    boolean existsByUserIdAndPublishId(int userId, int id);
}
