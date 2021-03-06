package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnServiceVideoLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceVideoLikeDao extends JpaRepository<XzqnServiceVideoLike,Integer> {

    //点赞视频
    //SELECT * FROM xzqn_news_log where user_id=11155 AND collect_news_id=2
    @Query(value = "SELECT n FROM XzqnServiceVideoLike n where n.userId=?1 AND n.videoId=?2")
    XzqnServiceVideoLike findByVideoLike(Integer userId, Integer videoId);

    //查询点赞数
    @Query(value = "SELECT COUNT(video_id) FROM xzqn_service_video_like WHERE video_id=?1",nativeQuery = true)
    Integer findCountLike(Integer videosId);
}
