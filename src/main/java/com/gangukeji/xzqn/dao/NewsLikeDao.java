package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnNews;
import com.gangukeji.xzqn.entity.XzqnNewsLike;
import com.gangukeji.xzqn.entity.XzqnServiceVideoLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsLikeDao extends JpaRepository<XzqnNewsLike,Integer> {

    //点赞资讯
    //SELECT * FROM xzqn_news_log where user_id=11155 AND collect_news_id=2
    @Query(value = "SELECT n FROM XzqnNewsLike n where n.userId=?1 AND n.newsId=?2")
    XzqnNewsLike findByNewsLike(Integer userId, Integer newsId);

    @Query(value = "SELECT COUNT(news_id) FROM xzqn_news_like where news_id=?1",nativeQuery = true)
    Integer findCountLike(Integer newsId);
}
