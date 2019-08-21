package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.shop.XzqnShopProductComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * {@link XzqnShopProductComment}
 */
@Repository
@Transactional
public interface ShopProductCommentDao  extends JpaRepository<XzqnShopProductComment,Integer> {
}
