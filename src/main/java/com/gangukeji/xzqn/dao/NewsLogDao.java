package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnNews;
import com.gangukeji.xzqn.entity.XzqnNewsLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsLogDao extends JpaRepository<XzqnNewsLog,Integer> {

    //收藏资讯
    //SELECT * FROM xzqn_news_log where user_id=11155 AND collect_news_id=2
    @Query(value = "SELECT n FROM XzqnNewsLog n where n.userId=?1 AND n.collectNewsId=?2")
    XzqnNewsLog findByNewslog(Integer userId,Integer collectNewsId);

    //查询收藏视频
    //SELECT * FROM xzqn_service_video_log where user_id=11154 ORDER BY collect_time ASC
    //SELECT * FROM xzqn_service_video_log  left join xzqn_service_videos on xzqn_service_video_log.collect_video_id=xzqn_service_videos.id WHERE user_id=11154
    @Query("select n.collectNewsId from XzqnNewsLog n where n.userId=?1 ORDER BY n.collectTime DESC ")
    List<Integer> findByCollectAndTime(Integer userId, Pageable pageable);

}
