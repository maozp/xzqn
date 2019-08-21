package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnServiceOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * {@link XzqnServiceOrderStatus}
 */
@Repository
@Transactional
public interface StatusDao  extends JpaRepository<XzqnServiceOrderStatus,Integer> {
    XzqnServiceOrderStatus findFirstByStatus(Integer status);
}
