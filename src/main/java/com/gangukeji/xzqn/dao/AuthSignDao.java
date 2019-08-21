package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.AuthSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthSignDao extends JpaRepository<AuthSign,Integer>, JpaSpecificationExecutor<AuthSign> {

    AuthSign findAuthSignByAuthId(Integer authId);
}
