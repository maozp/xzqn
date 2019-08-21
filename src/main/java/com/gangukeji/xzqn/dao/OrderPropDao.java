package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnOrderProp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/18 18:48
 * @Description:
 */
@Repository
@Transactional
public interface OrderPropDao extends JpaRepository<XzqnOrderProp,Integer> {
    List<XzqnOrderProp> findAllByPublishId(Integer publishId);
    List<XzqnOrderProp> findAllByOrderCateId(Integer orderCateId);
}
