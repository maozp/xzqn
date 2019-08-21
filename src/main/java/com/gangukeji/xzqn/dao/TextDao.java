package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * {@link XzqnText}
 */
@Repository
@Transactional
public interface TextDao extends JpaRepository<XzqnText,Integer> {
}
