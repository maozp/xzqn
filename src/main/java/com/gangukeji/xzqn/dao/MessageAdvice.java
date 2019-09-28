package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnMessageAdvice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface MessageAdvice extends JpaRepository<XzqnMessageAdvice,Integer> {

    //SELECT * from (select * from xzqn_message_advice order by time desc limit 0,20)a order by a.time ASC
    @Query(value = "SELECT * from (select * from xzqn_message_advice order by time desc limit 0,20)a order by a.time ASC",nativeQuery=true)
    List<XzqnMessageAdvice> findbyMessage();
}
