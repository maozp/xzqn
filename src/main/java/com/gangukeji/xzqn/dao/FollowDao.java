package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnFollow;
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
 * @Date: 2019/6/1 21:14
 * @Description:
 */
@Repository
@Transactional
public interface FollowDao extends JpaRepository<XzqnFollow, Integer> {
    Optional<XzqnFollow> findTopByUserIdAndUserIdFl(Integer u, Integer f);

    List<XzqnFollow> findByUserIdAndStatus(Integer userId, Boolean status);

    //查询我关注的人数
    @Query(value = "SELECT COUNT(user_id_fl) FROM xzqn_follow where user_id=?1 and `status`=1",nativeQuery = true)
    Integer findByMyStarNums(Integer userId);

    //查询关注我的人数
    @Query(value = "SELECT COUNT(user_id) FROM xzqn_follow where user_id_fl=?1 and `status`=1",nativeQuery = true)
    Integer findByStarMeNums(Integer userId);

    /*
     * 更新关注状态
     * */
    @Modifying
    @Query("update XzqnFollow  set status=:status where  userId=:userId and userIdFl =:userIdFl")
    void updateStatus(@Param("status") boolean status, @Param("userId") int userId, @Param("userIdFl") int userIdFl);


    /*
     * 用户关注查询
     * */
    @Query("select status from XzqnFollow where userId=?1 and userIdFl=?2")
    Boolean isStarUser(int userId, int userIdFl);

    boolean existsByUserIdAndUserIdFl(Integer userId, Integer userIdFl);
}
