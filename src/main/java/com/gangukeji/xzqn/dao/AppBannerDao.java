package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnAppBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppBannerDao extends JpaRepository<XzqnAppBanner,Integer> {
}
