package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnNews;
import com.gangukeji.xzqn.entity.XzqnNewsComment;
import com.gangukeji.xzqn.entity.shop.XzqnShopCate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/5 14:07
 * @Description:
 */
@Repository
@Transactional
public interface ShopCateDao extends JpaRepository<XzqnShopCate,Integer> {
    List<XzqnShopCate> findAllByParentId(Integer id);

    //SELECT * FROM xzqn_news_comment where news_id=1 ORDER BY comment_time DESC
    @Query("select n from XzqnShopCate n where n.parentId=?1 ORDER BY n.cateId ASC ")
    List<XzqnShopCate> findByCateId(Integer parentId, Pageable pageable);
}
