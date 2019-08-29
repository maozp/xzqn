package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnNews;
import com.gangukeji.xzqn.entity.XzqnServiceVideos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceVideosDao extends JpaRepository<XzqnServiceVideos,Integer> {

    @Query("select n from XzqnServiceVideos n ORDER BY n.videodate DESC ")
    List<XzqnServiceVideos> findAllOrderByVideosDate(Pageable pageable);

    @Query("select n from XzqnServiceVideos n where n.isCollect=1 ORDER BY n.collectTime DESC ")
    List<XzqnServiceVideos> findByCollectAndTime();

}
