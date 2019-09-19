package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * {@link XzqnUser}
 */
@Repository
@Transactional
public interface UserDao extends JpaRepository<XzqnUser, Integer> {

    //计算用户地区分布
    //select * from (SELECT city as 城市,COUNT(id) as 人数 FROM xzqn_user GROUP BY city ORDER BY COUNT(id) desc LIMIT 6) a UNION All  SELECT "其他",sum(b.人数 ) From (SELECT count(id) as 人数 FROM xzqn_user GROUP BY city ORDER BY COUNT(id) desc LIMIT 6,500) b

    //SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY COUNT(id) desc) AS cityNo,city ,COUNT(id) as userNums FROM xzqn_user  GROUP BY city ORDER BY COUNT(id) desc) a WHERE a.cityNo<=6 UNION ALL SELECT 7,'其他',SUM(a.userNums) from (SELECT ROW_NUMBER() OVER (ORDER BY COUNT(id) desc) AS cityNo,city ,COUNT(id) as userNums FROM xzqn_user  GROUP BY city ORDER BY COUNT(id) desc) a WHERE a.cityNo>6
    @Query(value = "SELECT c.city FROM (select * from (SELECT city ,COUNT(id) as userNums FROM xzqn_user GROUP BY city ORDER BY COUNT(id) desc LIMIT 6) a UNION All  SELECT '其他',sum(b.userNums ) From (SELECT count(id) as userNums FROM xzqn_user GROUP BY city ORDER BY COUNT(id) desc LIMIT 6,500) b)c" ,nativeQuery=true)
    List<String> findByUserCity();

    //计算用户地区分布人数
    //SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY COUNT(id) desc) AS cityNo,city ,COUNT(id) as userNums FROM xzqn_user  GROUP BY city ORDER BY COUNT(id) desc) a WHERE a.cityNo<=6 UNION ALL SELECT 7,'其他',SUM(a.userNums) from (SELECT ROW_NUMBER() OVER (ORDER BY COUNT(id) desc) AS cityNo,city ,COUNT(id) as userNums FROM xzqn_user  GROUP BY city ORDER BY COUNT(id) desc) a WHERE a.cityNo>6
    @Query(value = "SELECT c.userNums FROM (select * from (SELECT city ,COUNT(id) as userNums FROM xzqn_user GROUP BY city ORDER BY COUNT(id) desc LIMIT 6) a UNION All  SELECT '其他',sum(b.userNums ) From (SELECT count(id) as userNums FROM xzqn_user GROUP BY city ORDER BY COUNT(id) desc LIMIT 6,500) b)c " ,nativeQuery=true)
    List<Integer> findByUserNums();


    //计算用户总数
    //SELECT COUNT(order_id) FROM xzqn_service_order
    @Query(value = "SELECT COUNT(id) FROM xzqn_user ",nativeQuery=true)
    int findByPeopleNums();
    //计算7天之内注册的用户
    //SELECT COUNT(id) FROM xzqn_user where DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(create_time)
    @Query(value = "SELECT COUNT(id) FROM xzqn_user where DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(create_time)",nativeQuery=true)
    int findByWeekPeopleNums();

    //今天注册的用户
    //SELECT COUNT(id) FROM xzqn_user where DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(create_time)
    @Query(value = "SELECT COUNT(id) FROM xzqn_user where TO_DAYS(create_time)=TO_DAYS(NOW())",nativeQuery=true)
    int findByTodayPeopleNums();


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
