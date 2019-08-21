package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.TokenWithUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @描述
 * @创建时间 2019/5/12 15:53
 * @创建人 73119
 */
@Repository
@Transactional
@Deprecated
public interface TokenWithUserIdDao extends JpaRepository<TokenWithUserId, Integer> {

    TokenWithUserId findByUserIdAndToken(Integer userId, String token);

    void deleteByUserId(Integer userId);
}
