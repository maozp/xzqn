package com.gangukeji.xzqn.utils;

import com.gangukeji.xzqn.dao.SmsDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: hx
 * @Date: 2019/7/25 9:40
 * @Description:
 */
@Component
@Slf4j
public class SmsScheduling {
    @Autowired
    SmsDao smsDao;
//    @Scheduled(fixedRate = 1000)
    @Scheduled(fixedRate = 1000*60)
    public void scheduled() {
        log.error("1000*60*5");
//        smsDao.deleteByCreateTimeBefore(new Date(System.currentTimeMillis()-1000*5));
        smsDao.deleteByCreateTimeBefore(new Date(System.currentTimeMillis()-1000*60*5));
    }
}
