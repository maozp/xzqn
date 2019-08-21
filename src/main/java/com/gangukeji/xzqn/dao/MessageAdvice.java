package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnMessageAdvice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface MessageAdvice extends JpaRepository<XzqnMessageAdvice,Integer> {
}
