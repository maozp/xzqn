package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnAuthSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
public interface AuthSkillDao extends JpaRepository<XzqnAuthSkill, Integer> {
    XzqnAuthSkill findTopByReceiveUserIdOrderByIdDesc(int receiveUserId);

    /*
     * 更新审核状态
     * */
    @Modifying
    @Query("update XzqnAuthSkill e set e.isCheck=:isCheck where e.id=:authId")
    void updateCheck(@Param("isCheck") boolean isCheck, @Param("authId") int authId);

    XzqnAuthSkill findTopByUserIdOrderByIdDesc(int receiveUserId);

    @Query("select  isCheck from  XzqnAuthSkill  where userId=?1")
    int findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);

    void deleteByUserId(Integer userId);

    @Query("update XzqnAuthSkill set isCheck=?1 where userId=?2")
    @Modifying
    int updateStatus(int isCheck, int userId);
    @Query("update XzqnAuthSkill set isCheck=?1 ,refuseReason=?2 where userId=?3")
    @Modifying
    int updateStatusAndRefuseReason(int isCheck, String refuseReason, int userId);
    @Query("select isCheck from XzqnAuthSkill where userId=?1")
    Optional<Integer> getAuthStatus(int userId);

    @Query("select userId from XzqnAuthSkill ")
    List<Integer> getAuthUserId();
}
