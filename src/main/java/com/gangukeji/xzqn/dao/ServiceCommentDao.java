package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnServiceComment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/5/14 17:50
 * @Description:
 */
@Repository
@Transactional
public interface ServiceCommentDao extends JpaRepository<XzqnServiceComment,Integer> {
    List<XzqnServiceComment> findAllByOrderId(Integer orderId, Pageable pageRequest);
}
