package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnAuthContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @Author: hx
 * @Date: 2019/5/15 11:02
 * @Description:
 */
@Repository
@Transactional
public interface AuthContentDao extends JpaRepository<XzqnAuthContent, Integer> {

    XzqnAuthContent findTopByUserIdOrderByIdDesc(int userId);

    @Query("select  isCheck from  XzqnAuthContent  where userId=?1")
    int findByUserId(Integer userId);

    XzqnAuthContent findXzqnAuthContentByUserId(int userId);

    boolean existsByUserId(Integer userId);

    void deleteByUserId(Integer userId);

    @Query("update XzqnAuthContent set isCheck=?1 where userId=?2")
    @Modifying
    int updateStatus(int isCheck, int userId);
    @Query("update XzqnAuthContent set isCheck=?1 ,refuseReason=?2 where userId=?3")
    @Modifying
    int updateStatusAndRefuseReason(int isCheck, String refuseReason, int userId);
    @Query("select isCheck from XzqnAuthContent where userId=?1")
    Optional<Integer> getAuthStatus(int userId);

    @Query("select userId from XzqnAuthContent ")
    List<Integer> getAuthUserId();
}
