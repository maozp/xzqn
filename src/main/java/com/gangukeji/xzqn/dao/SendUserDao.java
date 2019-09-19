package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnUserSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SendUserDao extends JpaRepository<XzqnUserSend, Integer> {


    //SELECT COUNT(id) FROM `xzqn_user_send`
    //发单方人数
    @Query(value = "SELECT COUNT(id) FROM `xzqn_user_send` where is_check=2",nativeQuery=true)
    int findBySendNums();

    /**
     * 更新客户信息
     */
    /*@Modifying
    @Query("update XzqnUserSend set name=:name where sendUserId=:sendUserId")
    void updateUserInfo(@Param("name") String name,@Param("sendUserId") Integer sendUserId);*/
    @Modifying
    @Query("update XzqnUserSend set name=:name,phone=:phone,companyName=:companyName,companyPhone=:companyPhone where id=:sendUserId")
    int updateUserInfo(@Param("name") String name, @Param("phone") String phone, @Param("companyName") String companyName, @Param("companyPhone") String companyPhone, @Param("sendUserId") Integer sendUserId);

    @Modifying
    @Query("update XzqnUserSend set isCheck=?1,refuseReason=?2 where id=?3")
    int updateStatusAndRefuseReason(int isCheck, String refuseReason, int sendUserId);

    @Modifying
    @Query("update XzqnUserSend set isCheck=?1 where id=?2")
    int updateStatus(int isCheck, int sendUserId);

}
