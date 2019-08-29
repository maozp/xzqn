package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsDao extends JpaRepository<XzqnNews,Integer> {

    @Query("select n from XzqnNews n ORDER BY n.newsDate DESC ")
    List<XzqnNews> findAllOrderByNewsDate();

    @Query("select n from XzqnNews n where n.isCollect=1 ORDER BY n.collectTime DESC ")
    List<XzqnNews> findByCollectAndTime();
}
