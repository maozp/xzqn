package com.gangukeji.xzqn.dao;

import com.gangukeji.xzqn.entity.XzqnSms;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;


/**
 * @Author: hx
 * @Date: 2019/7/23 16:58
 * @Description:
 */
@Repository
@Transactional
public interface SmsDao extends JpaRepository<XzqnSms, Integer> {
    XzqnSms findTop1ByPhoneOrderByIdDesc(String phone);
    XzqnSms getDistinctFirstByPhone(String phone);
    XzqnSms getDistinctTopByPhone(String phone);
    XzqnSms getDistinctTopByPhoneOrderByIdDesc(String phone);
    int deleteByPhone(String phone);

    int deleteByCreateTimeBefore(Date time);
}
