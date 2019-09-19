package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnNews;
import com.gangukeji.xzqn.entity.XzqnServiceVideos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceVideosDao extends JpaRepository<XzqnServiceVideos,Integer> {

    @Query("select n from XzqnServiceVideos n ORDER BY n.videoDate DESC ")
    List<XzqnServiceVideos> findAllOrderByVideosDate(Pageable pageable);

    @Query("select n from XzqnServiceVideos n where n.id=?1 ")
    List<XzqnServiceVideos> findByVideos(Integer id);

}
