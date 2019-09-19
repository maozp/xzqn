package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnNewsLog;
import com.gangukeji.xzqn.entity.XzqnServiceVideoLog;
import com.gangukeji.xzqn.entity.XzqnServiceVideos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceVideoLogDao extends JpaRepository<XzqnServiceVideoLog,Integer> {

    //收藏视频
    //SELECT * FROM xzqn_news_log where user_id=11155 AND collect_news_id=2
    @Query(value = "SELECT n FROM XzqnServiceVideoLog n where n.userId=?1 AND n.videoId=?2")
    XzqnServiceVideoLog findByVideolog(Integer userId, Integer videoId);
    //查询收藏视频
    //SELECT * FROM xzqn_service_video_log where user_id=11154 ORDER BY collect_time ASC
    //SELECT * FROM xzqn_service_video_log  left join xzqn_service_videos on xzqn_service_video_log.collect_video_id=xzqn_service_videos.id WHERE user_id=11154
    @Query("select n.videoId from XzqnServiceVideoLog n where n.userId=?1 ORDER BY n.collectTime DESC ")
    List<Integer> findByCollectAndTime(Integer userId, Pageable pageable);
}
