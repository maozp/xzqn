package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnUserReceive;
import com.gangukeji.xzqn.entity.XzqnUserSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
/**
 * {@link XzqnUserReceive}
 */
@Repository
@Transactional
public interface UserReceiveDao extends JpaRepository<XzqnUserReceive,Integer> {
}
