package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnHelp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: hx
 * @Date: 2019/7/16 13:45
 * @Description:
 */
@Repository
public interface HelpDao extends JpaRepository<XzqnHelp,Integer> {

}
