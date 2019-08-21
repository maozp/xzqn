package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnUserReceive;
import com.gangukeji.xzqn.entity.view.ReceiveDataInOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @Author: hx
 * @Date: 2019/5/15 10:43
 * @Description:
 */
@Repository
@Transactional
public interface ReceiveUserDao extends JpaRepository<XzqnUserReceive, Integer> {
    @Query(value = "select u from  XzqnUserReceive u where u.introduceIds like %:type% ")
    List<XzqnUserReceive> findAllByTypeList(@Param("type") String type);
//    java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.math.BigDecimal, java.math.BigDecimal

    /**
     * 订单详情中的师傅信息
     *
     * @param receiveUserId
     * @return
     */
    @Query(value = "select new com.gangukeji.xzqn.entity.view.ReceiveDataInOrder(authType,area,cate2_ids,promise,lat,lng) from  XzqnUserReceive  where  id=:receiveUserId ")
    Optional<ReceiveDataInOrder> findDataInOrder(@Param("receiveUserId") Integer receiveUserId);

    @Query(value = "select new XzqnUserReceive(  authSkillId,  phone,  authUserId,  authEnterpriseId,  nickname,  addrName,  addrDes,  headImg,  mark,  cate2_ids,  area,  des,  promise,  serveTime,  turnover90,  balance,  lat,  lng,  backPercentage,  serveCount,  complaintCount,  goodCount,  starts,  enterArea,  badCount,  isGuarantee,  isBan,  isAuth,  authStatus,  companyName,  name,  rate,  goodAt,  authType,  location,  createTime,  updateTime,  introduceIds,  introduce) from  XzqnUserReceive  where  name like CONCAT('%',:name,'%') or companyName like CONCAT('%',:name,'%') or introduce like CONCAT('%',:name,'%') or area like CONCAT('%',:name,'%')")
    List<XzqnUserReceive> findAllByName(@Param("name") String name, Pageable pageable);

    @Query(value = "select name,head_img from xzqn_user_receive where id=(select xzqn_user.receive_user_id from xzqn_user where xzqn_user.id=?1)", nativeQuery = true)
    Object findByNameAndHeadImgByUserId(Integer userId);

    @Modifying
    @Query("update XzqnUserReceive set authJson=?1 where id=?2")
    int updateAuthJson(String json, Integer receiveUserId);

    @Query("select lat,lng from XzqnUserReceive where id=?1")
    List<List<String>> getLocation(Integer receiveUserId);

    @Modifying
    @Query("update XzqnUserReceive set lat=?1,lng=?2 where id=?3")
    int updateLatLng(BigDecimal lat, BigDecimal lng, Integer receiveUserId);

//    @Query(value = "select name,head_img from xzqn_user_receive where id=(select xzqn_user.receive_user_id from xzqn_user where xzqn_user.id=?1)", nativeQuery = true)
//    List<Map<String, Object>> test(Integer userId);
//
//    @Query(value = "select name,head_img from xzqn_user_receive where id=(select xzqn_user.receive_user_id from xzqn_user where xzqn_user.id=?1)", nativeQuery = true)
//    Map<String, Object> test1(int i);
}
