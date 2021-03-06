package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnAuthSign;
import com.gangukeji.xzqn.entity.XzqnAuthSignLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sun.awt.SunHints;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface AuthSignLogDao extends JpaRepository<XzqnAuthSignLog,Integer> {


    //查询补签那天是否签到过
    //SELECT * from xzqn_auth_sign_log WHERE user_id=11120 AND date_format(sign_time,'%Y-%m-%d') ='2019-10-07'
    @Query(value = "SELECT id from xzqn_auth_sign_log WHERE user_id=?1 AND date_format(sign_time,'%Y-%m-%d') =?2",nativeQuery = true)
    Integer isSign(Integer userId,String date);

    //查找连续签到天数 原生sql
    @Query(value = "select (max(id1)-min(id1)+1) from (SELECT *,datediff(sign_time, '2019-01-01') id1,(select count(1) from xzqn_auth_sign_log where sign_time <= a.sign_time and user_id=?1) id2 FROM xzqn_auth_sign_log a) a where user_id=?1 group by a.id1-a.id2 ORDER BY sign_time desc limit 1",nativeQuery = true)
    Integer selectContinueDays(Integer userId);

    @Query("SELECT n FROM XzqnAuthSignLog n WHERE n.userId=?1 ORDER BY n.signTime DESC")
    List<XzqnAuthSignLog> selectPoint(Integer userId);

    //查询所有签到天数
    @Query(value = "SELECT n FROM XzqnAuthSignLog n WHERE n.userId=?1 ORDER BY n.signTime DESC")
    List<XzqnAuthSignLog> findAllByDays(Integer userId);

    //select * from xzqn_auth_sign_log t where date_format(sign_time,'%Y-%m')='2019-08' AND user_id=11122 ORDER BY sign_time ASC
    @Query(value = "select * from xzqn_auth_sign_log t where date_format(sign_time,'%Y-%m')=?2 AND user_id=?1 ORDER BY sign_time ASC",nativeQuery = true)
    List<XzqnAuthSignLog> findBySignTime(Integer userId,String date);
}
