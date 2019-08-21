package com.gangukeji.xzqn.service;

import com.gangukeji.xzqn.dao.AuthSignDao;
import com.gangukeji.xzqn.dao.AuthSignLogDao;
import com.gangukeji.xzqn.entity.AuthSign;
import com.gangukeji.xzqn.entity.AuthSignLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AuthSignService {
    private final AuthSignDao authSignDao;
    private final AuthSignLogDao authSignLogDao;

    @Autowired
    public AuthSignService(AuthSignDao authSignDao, AuthSignLogDao authSignLogDao) {
        this.authSignDao = authSignDao;
        this.authSignLogDao = authSignLogDao;
    }

    /****
     * 打卡
     * @param authId 用户id
     * @return
     */
    @Transactional()
    public int createSign(Integer authId) {
        AuthSign authSign = authSignDao.findAuthSignByAuthId(authId);
        Date now = new Date();
        /*判断是否是第一次签到*/
        if (authSign == null) {
            System.out.println("用户第一次签到");
            authSign = AuthSign.builder()
                    .createTime(now)
                    .lastModifyTime(now)
                    .count(1)
                    .authId(authId)
                    .signCount(1)
                    .build();
            authSignDao.save(authSign);
            AuthSignLog authSignLog = AuthSignLog.builder()
                    .createTime(now)
                    .updateTime(now)
                    .authSignId(authSign.getId())
                    .signStatus(true)
                    .build();
            authSignLogDao.save(authSignLog);
            return 1;

        } else {
            //今天已经签到过了
            if (authSign.getLastModifyTime().getTime() > getStartOfDay(now).getTime()
                    && authSign.getLastModifyTime().getTime() < getEndOfDay(now).getTime()) {
                System.out.println("用户已经签到过了");
                return authSign.getSignCount();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DATE, -1);

            //非连续签到
            if (authSign.getLastModifyTime().getTime() < getStartOfDay(calendar.getTime()).getTime()) {
                System.out.println("已经不是连续签到");
                //签到次数重新开始
                //修改签到log表中的状态
                authSignLogDao.updateAuthSignLogSignStatus(authSign.getId());
                authSign.setCount(authSign.getCount() + 1);
                authSign.setSignCount(1);
                authSign.setLastModifyTime(now);
                AuthSignLog authSignLog = AuthSignLog.builder()
                        .createTime(now)
                        .updateTime(now)
                        .signStatus(true)
                        .authSignId(authSign.getId())
                        .build();
                authSignLogDao.save(authSignLog);
                authSignDao.save(authSign);
                return 1;
            } else {
                System.out.println("连续签到");
                //正常打卡
                authSign.setSignCount(authSign.getSignCount() + 1);
                authSign.setCount(authSign.getCount() + 1);
                authSign.setLastModifyTime(now);

                authSignDao.save(authSign);
                AuthSignLog authSignLog = AuthSignLog.builder()
                        .signStatus(true)
                        .createTime(now)
                        .updateTime(now)
                        .authSignId(authSign.getId())
                        .build();
                authSignLogDao.save(authSignLog);
                return authSign.getSignCount();
            }
        }
    }

    /****
     * 用户每天第一次进入签到打卡页面初始化一下数据
     * @param authId
     */
    @Transactional()
    public void init(Integer authId) {
        AuthSign authSign = authSignDao.findAuthSignByAuthId(authId);
        Date now = new Date();
        //曾经签到过
        if (authSign != null) {
            //判断今天是否签到过
            if (authSign.getLastModifyTime().getTime() > getStartOfDay(now).getTime()
                    && authSign.getLastModifyTime().getTime() < getEndOfDay(now).getTime()) {
                System.out.println("用户今天已经签到过了，正常使用");
                return;
            }
            //判断昨天是否签到过
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DATE, -1);
            if (authSign.getLastModifyTime().getTime() < getStartOfDay(calendar.getTime()).getTime()) {
                System.out.println("昨天没有签到");
                //昨天没有连续签到
                if (authSign.getSignCount() == 7) {
                    System.out.println("签到次数为7的时候初始化数据");
                    //初始化数据
                    authSign.setSignCount(0);
                    authSignLogDao.updateAuthSignLogSignStatus(authSign.getId());
                    authSignDao.save(authSign);
                    return;
                }
                System.out.println("签到次正常");
            } else {
                System.out.println("昨天签到了");
                //昨天签到过
                if (authSign.getSignCount() == 7) {
                    System.out.println("签到次数为7的时候初始化数据");
                    //初始化数据
                    authSign.setSignCount(0);
                    authSignLogDao.updateAuthSignLogSignStatus(authSign.getId());
                    authSignDao.save(authSign);
                    return;
                }
                System.out.println("签到次正常");
            }
        }

    }


    /*根据用户id查询签到list*/
    public List<AuthSignLog> data(Integer authId) {
        AuthSign authSign = authSignDao.findAuthSignByAuthId(authId);
        //没有签到过，返回null
        if (authSign != null) {
            //如果今天签到过
            return authSignLogDao.findAuthSignLogsByAuthSignIdAndSignStatusIsTrue(authSign.getId());
        }
        return new ArrayList<>();
    }

    public Boolean isOrSign(Integer authId) {
        Date now = new Date();
        AuthSign authSign = authSignDao.findAuthSignByAuthId(authId);
        //没有签到过，返回null
        //如果今天签到过
        return authSign != null && authSign.getLastModifyTime().getTime() > getStartOfDay(now).getTime() && authSign.getLastModifyTime().getTime() < getEndOfDay(now).getTime();
    }


    /****
     * 获得某天最大时间 2019-05-09 23:59:59
     * @param date 日期
     * @return
     */

    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /****
     * 获得某天最小时间 2019-05-09 00:00:00
     * @param date 日期
     * @return
     */
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }
    
}
