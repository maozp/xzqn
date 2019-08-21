package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnServiceProp2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @Author: hx
 * @Date: 2019/6/15 11:26
 * @Description:
 */
@Repository
@Transactional
public interface ServicePropDao2 extends JpaRepository<XzqnServiceProp2,Integer> {

}
