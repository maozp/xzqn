package com.gangukeji.xzqn.controller.xzqn.other;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.gangukeji.xzqn.dao.AuthSignDao;
import com.gangukeji.xzqn.dao.ReceiveUserDao;
import com.gangukeji.xzqn.dao.UserDao;
import com.gangukeji.xzqn.entity.*;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping
public class AuthSignController {

    @Resource
    AuthSignDao authSignDao;
    @Resource
    UserDao userDao;

    Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @PostMapping("/sign")
    public Result doSign(@RequestBody String data,@RequestParam Integer userId) throws Exception{

        if(authSignDao.findXzqnAuthSignByAndUserId(userId)==null){
            XzqnAuthSign authSign = gson.fromJson(data, XzqnAuthSign.class);
            Date now = new Date();
            System.out.println("师傅第一次签到");
            Integer usersId = authSign.getUserId();
            Integer receiveUserId = userDao.getReceiveUserIdByUserId(usersId);
            authSign.setReceiveUserId(receiveUserId);
            authSign.setSignTime(now);
            authSign.setSignLastTime(now);
            authSign.setSignPoint(1);
            authSign.setSignContinueDays(1);
            authSign.setSignTotalDays(1);
            authSign.setIsSign(1);
            authSignDao.save(authSign);
//            Integer authId = authSignDao.save(authSign).getId();
//            HashMap<Object, Object> map = new HashMap<>();
//            map.put("id", authId);
            return ResultUtils.success(200, "签到成功", authSign);
        }
        XzqnAuthSign authSign = authSignDao.findXzqnAuthSignByAndUserId(userId);
        Date now = new Date();
        if (authSign != null) {
            if(authSign.getSignLastTime().getTime()>getStartOfDay(now).getTime()&&
            authSign.getSignLastTime().getTime()<getEndOfDay(now).getTime()){
                System.out.println("用户今天已经签到过了，正常使用");
                return ResultUtils.error(200,"用户已经签到了");
            }
            //判断昨天是否签到过
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DATE, -1);
            if(authSign.getSignLastTime().getTime()< getStartOfDay(calendar.getTime()).getTime()){
                System.out.println("昨天没有连续签到");
                authSign.setSignContinueDays(0);
            }

            System.out.println("师傅是否签到");
            authSign.setSignLastTime(now);
            authSign.setSignPoint(authSign.getSignPoint()+3);
            authSign.setSignContinueDays(authSign.getSignContinueDays() + 1);
            authSign.setSignTotalDays(authSign.getSignTotalDays()+ 1);
            authSign.setIsSign(1);
            authSignDao.save(authSign);
//            Integer authId = authSignDao.save(authSign).getId();
//            HashMap<Object, Object> map = new HashMap<>();
//            map.put("id", authId);
            return ResultUtils.success(200, "签到成功",authSign);
        }
        return ResultUtils.success();
    }

    /****
     * 获得某天最大时间 2019-08-22 23:59:59
     * @param date 日期
     * @return
     */

    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /****
     * 获得某天最小时间 2019-08-22 00:00:00
     * @param date 日期
     * @return
     */
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    @PostMapping("/signFind")
    public Result signView(@RequestBody String data){
        int userId = new JsonParser().parse(data).getAsJsonObject().get("userId").getAsInt();
        XzqnAuthSign resp = authSignDao.findTopByUserIdOrderByIdDesc(userId);
        return ResultUtils.success(200, "查找签到信息成功", resp);
    }
}
