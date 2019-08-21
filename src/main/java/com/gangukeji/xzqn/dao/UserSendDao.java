package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnUserSend;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * {@link XzqnUserSend}
 */
@Repository
@Transactional
public interface UserSendDao extends JpaRepository<XzqnUserSend,Integer> {


}
