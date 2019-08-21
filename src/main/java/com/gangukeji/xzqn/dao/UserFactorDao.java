package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnUserFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * {@link XzqnUserFactor}
 */
@Repository
@Transactional
public interface UserFactorDao extends JpaRepository<XzqnUserFactor, Integer> {
    @Query("select name from XzqnUserFactor where factorId=?1")
    String findNameById(Integer factorId);
}
