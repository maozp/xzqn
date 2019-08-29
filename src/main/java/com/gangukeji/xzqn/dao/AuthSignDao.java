package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnAuthContent;
import com.gangukeji.xzqn.entity.XzqnAuthSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AuthSignDao extends JpaRepository<XzqnAuthSign,Integer> {

    XzqnAuthSign findXzqnAuthSignByAndUserId(Integer userId);

    XzqnAuthSign findTopByUserIdOrderByIdDesc(int userId);
    @Modifying
    @Query("update XzqnAuthSign set signStatus=0  where id =?1")
    int updateAuthSignStatus(Integer id);

    //添加补签卡
    //UPDATE xzqn_auth_sign set sign_card =sign_card+1  WHERE receive_user_id=5
    @Modifying
    @Query("UPDATE XzqnAuthSign n set n.signCard =n.signCard+1  WHERE n.receiveUserId=?1")
    Integer addSignCard(Integer receiveUserId);
}
