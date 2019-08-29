package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnNewsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface NewsCommentDao extends JpaRepository<XzqnNewsComment,Integer> {
}
