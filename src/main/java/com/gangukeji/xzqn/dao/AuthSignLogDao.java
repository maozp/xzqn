package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.AuthSignLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthSignLogDao extends JpaRepository<AuthSignLog, Integer>, JpaSpecificationExecutor<AuthSignLog> {

    @Modifying
    @Query("update AuthSignLog set signStatus=false  where authSignId =?1")
    int updateAuthSignLogSignStatus(Integer authSignId);


    List<AuthSignLog> findAuthSignLogsByAuthSignIdAndSignStatusIsTrue(Integer authSignId);


}
