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
}
