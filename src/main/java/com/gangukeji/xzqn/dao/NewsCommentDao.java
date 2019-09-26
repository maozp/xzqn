package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnNews;
import com.gangukeji.xzqn.entity.XzqnNewsComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface NewsCommentDao extends JpaRepository<XzqnNewsComment,Integer> {

    //SELECT * FROM xzqn_news_comment where news_id=1 ORDER BY comment_time DESC
    @Query("select n from XzqnNewsComment n where n.newsId=?1 ORDER BY n.commentTime DESC ")
    List<XzqnNewsComment> findByComment(Integer newsId, Pageable pageable);

    @Query(value = "SELECT COUNT(news_id) FROM xzqn_news_comment where news_id=?1",nativeQuery = true)
    Integer findCountComment(Integer newsId);
}
