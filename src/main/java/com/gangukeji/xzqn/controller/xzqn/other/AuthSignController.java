package com.gangukeji.xzqn.controller.xzqn.other;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.gangukeji.xzqn.dao.AuthSignDao;
import com.gangukeji.xzqn.dao.AuthSignLogDao;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * @description 积分签到功能
 * @author mao
 * @date 2019/8/24 16:21
 * @保重
 */
@RestController
@RequestMapping
public class AuthSignController {

    @Resource
    AuthSignDao authSignDao;
    @Resource
    UserDao userDao;
    @Resource
    AuthSignLogDao authSignLogDao;
    Date now = new Date();

    Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 签到
     * @param data
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping("/sign")
    public Result doSign(@RequestBody String data,@RequestParam Integer userId) throws Exception{

        //判断用户是否是第一次签到 判断数据库用户id
        if(authSignDao.findXzqnAuthSignByAndUserId(userId)==null){
            XzqnAuthSign authSign = gson.fromJson(data, XzqnAuthSign.class);
            System.out.println("师傅第一次签到");
            authSign.setUserId(userId);
            Integer receiveUserId = userDao.getReceiveUserIdByUserId(userId);
            authSign.setReceiveUserId(receiveUserId);
            authSign.setSignTime(now);
            authSign.setSignLastTime(now);
            authSign.setSignPoint(1);
            //authSign.setSignContinueDays(1);
            authSign.setSignTotalDays(1);
            authSign.setIsSign(1);
            authSign.setSignLackDays(0);
            authSign.setSignCard(0);
            authSignDao.save(authSign);

            XzqnAuthSignLog authSignLog=gson.fromJson(data,XzqnAuthSignLog.class);
            authSignLog.setSignTime(now);
            authSignLog.setIsSign(1);
            //authSignLog.setSignContinueDays(1);
            authSignLog.setSignPoint(1);
            authSignLog.setSignContent("签到奖励");
            authSignLog.setUserId(authSign.getUserId());
            authSignLogDao.save(authSignLog);
            return ResultUtils.success(200, "签到成功", authSign);
        }
        XzqnAuthSign authSign = authSignDao.findXzqnAuthSignByAndUserId(userId);
        XzqnAuthSignLog authSignLog=gson.fromJson(data,XzqnAuthSignLog.class);
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
                //authSign.setSignContinueDays(0);
                authSign.setSignLastTime(now);
                authSign.setSignPoint(authSign.getSignPoint()+3);
                //authSign.setSignContinueDays(authSignLogDao.selectContinueDays(userId)+1);
                authSign.setSignTotalDays(authSign.getSignTotalDays()+ 1);
                authSign.setIsSign(1);
                calendar.setTime(now);
                int day1=calendar.get(Calendar.DAY_OF_YEAR);
                calendar.setTime(authSign.getSignTime());
                int day2=calendar.get(Calendar.DAY_OF_YEAR);
                int day=day1-day2+2;
                authSign.setSignLackDays(day-authSign.getSignTotalDays());
                authSignDao.save(authSign);

                //authSignLog.setSignContinueDays(0);
                authSignLog.setSignContent("签到奖励");
                authSign.setSignPoint(authSign.getSignPoint()-2);
            }

            authSign.setSignLastTime(now);
            authSign.setSignPoint(authSign.getSignPoint()+3);
            //authSign.setSignContinueDays(authSignLogDao.selectContinueDays(userId)+1);
            authSign.setSignTotalDays(authSign.getSignTotalDays()+ 1);
            authSign.setIsSign(1);

            calendar.setTime(now);
            int day1=calendar.get(Calendar.DAY_OF_YEAR);
            calendar.setTime(authSign.getSignTime());
            int day2=calendar.get(Calendar.DAY_OF_YEAR);
            int day=day1-day2+2;
            authSign.setSignLackDays(day-authSign.getSignTotalDays());
            authSignDao.save(authSign);

            authSignLog.setSignTime(now);
            authSignLog.setIsSign(1);
            //authSignLog.setSignContinueDays(authSignLogDao.selectContinueDays(userId));
            authSignLog.setUserId(authSign.getUserId());
            authSignLog.setSignContent("连续签到奖励");
            authSignLogDao.save(authSignLog);
//            Integer authId = authSignDao.save(authSign).getId();
//            HashMap<Object, Object> map = new HashMap<>();
//            map.put("id", authId);
            return ResultUtils.success(200, "签到成功",authSign);
        }
        return ResultUtils.success();
    }

    /**
     * 签到
     * @param data
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping("/signV2")
    public Result doSignV2(@RequestBody String data,@RequestParam Integer userId) throws Exception{

        XzqnAuthSign authSign = authSignDao.findXzqnAuthSignByAndUserId(userId);
        XzqnAuthSignLog authSignLog=gson.fromJson(data,XzqnAuthSignLog.class);

        //判断用户是否是第一次签到
        if(authSign.getSignLastTime()==null){
            System.out.println("师傅第一次签到");
            authSign.setUserId(userId);
            Integer receiveUserId = userDao.getReceiveUserIdByUserId(userId);
            authSign.setReceiveUserId(receiveUserId);
            authSign.setSignTime(now);
            authSign.setSignLastTime(now);
            authSign.setSignPoint(1);
            //authSign.setSignContinueDays(1);
            authSign.setSignTotalDays(1);
            authSign.setIsSign(1);
            authSign.setSignLackDays(0);
            authSign.setSignCard(0);
            authSignDao.save(authSign);
            authSignLog.setSignTime(now);
            authSignLog.setIsSign(1);
            //authSignLog.setSignContinueDays(1);
            authSignLog.setSignPoint(1);
            authSignLog.setSignContent("签到奖励");
            authSignLog.setUserId(authSign.getUserId());
            authSignLogDao.save(authSignLog);
        }else{
            //判断是否签到
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
                authSign.setSignPoint(authSign.getSignPoint()+1);
                authSign.setSignLastTime(now);
                authSign.setSignTotalDays(authSign.getSignTotalDays()+1);
                authSignDao.save(authSign);

                authSignLog.setSignPoint(1);
                authSignLog.setIsSign(1);
                authSignLog.setSignContent("签到奖励");
                authSignLogDao.save(authSignLog);
            }else {
                System.out.println("昨天有连续签到");
                authSign.setSignPoint(authSign.getSignPoint()+3);
                authSign.setSignLastTime(now);
                authSign.setSignTotalDays(authSign.getSignTotalDays()+1);
                authSignDao.save(authSign);


                authSignLog.setSignPoint(3);
                authSignLog.setSignTime(now);
                authSignLog.setIsSign(1);
                authSignLog.setSignContent("连续签到奖励");
                authSignLogDao.save(authSignLog);
            }
        }

        return ResultUtils.success(200, "签到成功", "签到成功");

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

    //查看签到信息
    @PostMapping("/signFind")
    public Result signView(@RequestBody String data,@RequestParam(value="userId") Integer userId){

        if(authSignDao.findTopByUserIdOrderByIdDesc(userId)==null){
            XzqnAuthSign authSign = gson.fromJson(data, XzqnAuthSign.class);
            authSign.setIsSign(0);
            authSign.setSignPoint(0);
            authSign.setSignLackDays(0);
            authSign.setSignCard(0);

            authSignDao.save(authSign);
            return ResultUtils.success(200, "查找签到信息成功", authSign);
        }
        XzqnAuthSign authSign = authSignDao.findTopByUserIdOrderByIdDesc(userId);
        Date now = new Date();
        if(authSign.getSignLastTime()==null){
            return ResultUtils.success(200, "查找签到信息成功", authSign);
        }
        if (authSign.getSignLastTime().getTime() > getStartOfDay(now).getTime() &&
                authSign.getSignLastTime().getTime() < getEndOfDay(now).getTime()) {
            authSign.setIsSign(1);
        }else{
            authSign.setIsSign(0);
        }
        //判断昨天是否签到过
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, -1);
        if(authSign.getSignLastTime().getTime()< getStartOfDay(calendar.getTime()).getTime()){
            System.out.println("昨天没有连续签到");
            //authSign.setSignContinueDays(0);
        }
        authSignDao.save(authSign);
        return ResultUtils.success(200, "查找签到信息成功", authSign);
    }

    //查看补签弹框日期
    @PostMapping("/findSignDate")
    public Result findDate(@RequestParam(value="userId") Integer userId)throws Exception{
        return ResultUtils.success(200,"查看已签到日期",authSignLogDao.findAllByDays(userId));
    }
    //补签接口
    @PostMapping("replacementDate")
    public Result replacement(@RequestBody String data,@RequestParam(value="userId") Integer userId)throws Exception{
        XzqnAuthSignLog authSignLog=gson.fromJson(data,XzqnAuthSignLog.class);
        XzqnAuthSign authSign = authSignDao.findXzqnAuthSignByAndUserId(userId);
        if(authSign.getSignCard()>=1){
            authSignLog.setIsSign(1);
            authSignLog.setSignPoint(2);
            authSignLog.setSignContent("补签积分");
            authSignLogDao.save(authSignLog);
            authSign.setSignCard(authSign.getSignCard()-1);
            authSign.setSignTotalDays(authSign.getSignTotalDays()+1);
            authSign.setSignLackDays(authSign.getSignLackDays()-1);
            authSignDao.save(authSign);
            return ResultUtils.success(200,"补签成功",authSignLogDao.findAllByDays(authSignLog.getUserId()));
        }else {
            return ResultUtils.error(0,"尚未获得补签卡");
        }

    }
    //查看积分明细
    @PostMapping("/signPoint")
    public Result signPoint(@RequestParam(value="userId") Integer userId)throws Exception {
        if(authSignDao.findTopByUserIdOrderByIdDesc(userId)==null){
            return ResultUtils.success(200,"积分明细为空","");
        }
        return ResultUtils.success(200,"查看积分明细信息",authSignLogDao.selectPoint(userId));
    }
    //查看连续签到天数
    @PostMapping("/continueDays")
    public Result ContinueDays(@RequestParam(value="userId") Integer userId)throws Exception{
        if(authSignDao.findTopByUserIdOrderByIdDesc(userId)==null){
            return ResultUtils.success(200,"查看连续签到天数",0);
        }else{
            XzqnAuthSign authSign = authSignDao.findXzqnAuthSignByAndUserId(userId);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DATE, -1);
            //如果最后签到时间<昨天0点
            if(authSign.getSignLastTime()==null || authSign.getSignLastTime().getTime()< getStartOfDay(calendar.getTime()).getTime()  ){
                return ResultUtils.success(200,"查看连续签到天数",0);
            }else{
                return ResultUtils.success(200,"查看连续签到天数",authSignLogDao.selectContinueDays(userId));
            }
        }
    }


}
