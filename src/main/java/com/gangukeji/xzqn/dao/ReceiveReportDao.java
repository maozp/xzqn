package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnReceiveReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/5/20 18:04
 * @Description:
 */
@Repository
@Transactional
public interface ReceiveReportDao  extends JpaRepository<XzqnReceiveReport,Integer> {
    XzqnReceiveReport findTopByOrderIdOrderByIdDesc(Integer orderId);
}