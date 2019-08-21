package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnServiceComplain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @Author: hx
 * @Date: 2019/5/14 18:10
 * @Description:
 */
@Repository
@Transactional
public interface ServiceComplainDao extends JpaRepository<XzqnServiceComplain,Integer> {
}
