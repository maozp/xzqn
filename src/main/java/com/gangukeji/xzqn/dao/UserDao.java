package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * {@link XzqnUser}
 */
@Repository
@Transactional
public interface UserDao extends JpaRepository<XzqnUser, Integer> {

    XzqnUser findByUsernameAndPassword(String username, String password);

    @Query("select id from XzqnUser where username=?1 and password=?2")
    Integer findUserIdByUsernameAndPassword(String username, String password);

    XzqnUser findByUsername(String username);

    XzqnUser findByUserPhone(String userphone);

    Boolean existsByUsername(String username);

    XzqnUser findByReceiveUserId(Integer i);

    XzqnUser findBySendUserId(Integer sendUserId);

//    boolean existsByOpenid(String openid);

    //    XzqnUser findFirstByOpenidOrderByIdDesc(String openid);
    XzqnUser findByOpenid(String openId);
    XzqnUser findByUnionid(String unionId);
    /**
     * 获取sendUserId
     */
    @Query(value = "select sendUserId from XzqnUser where id=?1")
    Integer getSendUserIdByUserId(Integer userId);

    /**
     * 获取receiveUserId
     */
    @Query(value = "select receiveUserId from XzqnUser where id=?1")
    Integer getReceiveUserIdByUserId(Integer userId);

    /**
     * @param openid 获取userId
     */
    @Query(value = "select id from XzqnUser where openid=?1")
    Integer getOpenid(String openid);

    @Query("select id from XzqnUser where receiveUserId=:receiveUserId")
    Integer getUserIdByReceiveUserId(@Param("receiveUserId") Integer receiveUserId);
    @Query("select receiveUserId from XzqnUser where sendUserId=:sendUserId")
    int getReceiveUserIdBySendUserId(int sendUserId);
}
