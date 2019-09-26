package com.gangukeji.xzqn.controller.shop;

import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 *  pc后台商城统计
 */
@RestController
@CrossOrigin("*")
@RequestMapping("index")
public class PCindexController {

    @Autowired
    UserDao userDao;
    @Autowired
    ReceiveUserDao receiveUserDao;
    @Autowired
    ServiceOrderDao serviceOrderDao;
    @Autowired
    ShopUserDao shopUserDao;
    @Autowired
    ShopOrderMasterDao shopOrderMasterDao;
    @Autowired
    ServicePublishDao servicePublishDao;


    @PostMapping("/xzqn/statis")
    public Result order(){
        //小正青年订单总数
        Integer orderNums=serviceOrderDao.findByOrderNums();
        //小正青年用户总数
        Integer peopleNums=userDao.findByPeopleNums();
        //小正青年已完成支付总数
        Integer FinishOrderNums=serviceOrderDao.findByFinishOrderNums();
        //小正青年未完成支付总数
        Integer UnfinishOrderNums=serviceOrderDao.findByUnfinishOrderNums();
        //小正青年昨日付款订单数
        Integer YestodayOrderPayNums=serviceOrderDao.findByYestodayOrderPayNums();
        //小正青年昨日付款金额数
        BigDecimal YestodayMoneyNums=serviceOrderDao.findByYestodayMoneyNums();

        //小正青年今日付款订单数
        Integer TodayOrderPayNums=serviceOrderDao.findByTodayOrderPayNums();
        //小正青年今日付款金额数
        BigDecimal TodayMoneyNums=serviceOrderDao.findByTodayMoneyNums();


        HashMap<String,Object> map=new HashMap<>();
        map.put("orderNums",orderNums);
        map.put("peopleNums",peopleNums);
        map.put("FinishOrderNums",FinishOrderNums);
        map.put("UnfinishOrderNums",UnfinishOrderNums);
        map.put("YestodayOrderPayNums",YestodayOrderPayNums);
        map.put("YestodayMoneyNums",YestodayMoneyNums);
        map.put("TodayOrderPayNums",TodayOrderPayNums);
        map.put("TodayMoneyNums",TodayMoneyNums);

        return ResultUtils.success(200,"查询小正青年统计数据成功",map);
    }

    //7天完成订单
    @PostMapping("/xzqn/weekOrder")
    public Result weekOrder() {
        Integer weekOrder = serviceOrderDao.findByWeekOrderNums();
        HashMap<String,Object> map=new HashMap<>();
        map.put("weekFinshOrderNums",weekOrder);
        return ResultUtils.success(200,"查询最近7天完成订单成功",map);
    }

    //今日订单数
    @PostMapping("/xzqn/todayOrder")
    public Result todayOrder() {
        Integer todayOrder = serviceOrderDao.findByTodayOrderNums();
        HashMap<String,Object> map=new HashMap<>();
        map.put("todayOrderNums",todayOrder);
        return ResultUtils.success(200,"查询今天订单数成功",map);
    }

    //用户总数
    @PostMapping("/xzqn/totalUser")
    public Result totalUser() {
        //小正青年用户总数
        Integer peopleNums=userDao.findByPeopleNums();
        //7天注册用户数
        Integer weekPeopleNums = userDao.findByWeekPeopleNums();
        ///今天注册用户数
        Integer todayPeopleNums = userDao.findByTodayPeopleNums();


        HashMap<String,Object> map=new HashMap<>();
        map.put("totalPeopleNums",peopleNums);
        map.put("weekPeopleNums",weekPeopleNums);
        map.put("todayPeopleNums",todayPeopleNums);
        return ResultUtils.success(200,"查询用户数据成功",map);

    }
    //订单总数
    @PostMapping("/xzqn/totalOrder")
    public Result totalOrder() {
//        //小正青年服务订单总数
//        Integer orderNums=serviceOrderDao.findByOrderNums();
//        //7天服务订单数
//        Integer weekOrder = serviceOrderDao.findByWeekOrderNums();
//        ///今天服务订单数
//        Integer todayOrder = serviceOrderDao.findByTodayOrderNums();
        //小正青年订单数
        Integer orderNums=servicePublishDao.findByOrderNums();
        //7天订单数
        Integer weekOrder = servicePublishDao.findByWeekOrderNums();
        //今日订单数
        Integer todayOrder = servicePublishDao.findByTodayOrderNums();
        //本月订单数
        Integer monthOrder = servicePublishDao.findByMonthOrderNums();
        //本月成交金额数
        BigDecimal monthMoney = serviceOrderDao.findByMonthOrderMoney();
        //未接过单的用户
        Integer unReceiveUser = serviceOrderDao.findByUnreceiveUser();
        //订单好评率
        String  OrderLikeNums= serviceOrderDao.findByOrderLikeNums();

        //小正青年已完成支付总数
        Integer FinishPayOrderNums=serviceOrderDao.findByFinishOrderNums();
        //小正青年未完成支付总数
        Integer UnfinishPayOrderNums=serviceOrderDao.findByUnfinishOrderNums();


        HashMap<String,Object> map=new HashMap<>();
        map.put("totalOrderNums",orderNums);
        map.put("weekOrderNums",weekOrder);
        map.put("todayOrderNums",todayOrder);
        map.put("monthOrderNums",monthOrder);
        map.put("monthMoneyNums",monthMoney);
        map.put("unReceiveUser",unReceiveUser);
        map.put("orderLikeNums",OrderLikeNums);
        map.put("FinishPayOrderNums",FinishPayOrderNums);
        map.put("UnfinishPayOrderNums",UnfinishPayOrderNums);
        return ResultUtils.success(200,"查询小正青年订单数据成功",map);
    }



    //7天折线订单图
    @PostMapping("/xzqn/orderChart")
    public Result orderChart() {
        List<String> orderChart = serviceOrderDao.findByorderChart();
        List<String> orderChartNums = serviceOrderDao.findByorderChartNums();
        HashMap<String,Object> map=new HashMap<>();
        map.put("date",orderChart);
        map.put("count",orderChartNums);
        return ResultUtils.success(200,"查询7天折线订单数成功",map);

    }
    //7天折线金额图
    @PostMapping("/xzqn/moneyChart")
    public Result moneyChart() {
        List<String> moneyChart = serviceOrderDao.findByorderChart();
        List<String> moneyChartMoney = serviceOrderDao.findByorderChartMoney();
        HashMap<String,Object> map=new HashMap<>();
        map.put("date",moneyChart);
        map.put("money",moneyChartMoney);
        return ResultUtils.success(200,"查询7天折线金额成功",map);

    }
    //分析用户地区
    @PostMapping("/xzqn/userCity")
    public Result userCity() {
        List<String> citys= userDao.findByUserCity();
        List<Integer> userNums= userDao.findByUserNums();
        HashMap<String,Object> map=new HashMap<>();
        map.put("citys",citys);
        map.put("userNums",userNums);
        return ResultUtils.success(200,"查询用户地区折线图成功",map);
    }

    //6个月接单量比较
    @PostMapping("/xzqn/monthChart")
    public Result monthOrder() {
        List<String> orderChart = serviceOrderDao.findByMonthOrderChartDate();
        List<String> orderChartNums = serviceOrderDao.findByMonthOrderChartCount();
        HashMap<String,Object> map=new HashMap<>();
        map.put("month",orderChart);
        map.put("count",orderChartNums);
        return ResultUtils.success(200,"查询前6月折线订单数成功",map);
    }
    //6个月交易金额比较
    @PostMapping("/xzqn/monthMoneyChart")
    public Result moneyOrder() {
        List<String> orderChart = serviceOrderDao.findByMonthOrderChartDate();
        List<String> orderChartNums = serviceOrderDao.findByMonthMoneyChartCount();
        HashMap<String,Object> map=new HashMap<>();
        map.put("month",orderChart);
        map.put("money",orderChartNums);
        return ResultUtils.success(200,"查询前6月折线交易金额成功",map);
    }




    @PostMapping("/xzyt/statis")
    public Result statis(){
        //小正鱼塘订单总数
        Integer orderNums=shopOrderMasterDao.findByOrderNums();
        //小正鱼塘用户总数
        Integer peopleNums=shopUserDao.findByPeopleNums();
        //小正鱼塘已完成总数
        Integer FinishOrderNums=shopOrderMasterDao.findByFinishOrderNums();
        //小正鱼塘未完成总数
        Integer UnfinishOrderNums=shopOrderMasterDao.findByUnfinishOrderNums();
        //小正鱼塘昨日完成订单数
        Integer YestodayOrderPayNums=shopOrderMasterDao.findByYestodayOrderPayNums();
        //小正鱼塘昨日完成金额数
        BigDecimal YestodayMoneyNums=shopOrderMasterDao.findByYestodayMoneyNums();

        //小正鱼塘今日完成订单数
        Integer TodayOrderPayNums=shopOrderMasterDao.findByTodayOrderPayNums();
        //小正鱼塘今日完成金额数
        BigDecimal TodayMoneyNums=shopOrderMasterDao.findByTodayMoneyNums();


        HashMap<String,Object> map=new HashMap<>();
        map.put("orderNums",orderNums);
        map.put("peopleNums",peopleNums);
        map.put("FinishOrderNums",FinishOrderNums);
        map.put("UnfinishOrderNums",UnfinishOrderNums);
        map.put("YestodayOrderPayNums",YestodayOrderPayNums);
        map.put("YestodayMoneyNums",YestodayMoneyNums);
        map.put("TodayOrderPayNums",TodayOrderPayNums);
        map.put("TodayMoneyNums",TodayMoneyNums);

        return ResultUtils.success(200,"查询小正鱼塘统计数据成功",map);
    }

    //用户总数
    @PostMapping("/xzyt/totalUser")
    public Result xzytUser() {
        //小正鱼塘用户总数
        Integer peopleNums=shopUserDao.findByPeopleNums();
        //7天注册用户数
        Integer weekPeopleNums = shopUserDao.findByWeekPeopleNums();
        ///今天注册用户数
        Integer todayPeopleNums = shopUserDao.findByTodayPeopleNums();


        HashMap<String,Object> map=new HashMap<>();
        map.put("totalPeopleNums",peopleNums);
        map.put("weekPeopleNums",weekPeopleNums);
        map.put("todayPeopleNums",todayPeopleNums);
        return ResultUtils.success(200,"查询小正鱼塘用户数据成功",map);

    }

    //订单总数
    @PostMapping("/xzyt/totalOrder")
    public Result xzytOrder() {
        //小正鱼塘订单总数
        Integer orderNums=shopOrderMasterDao.findByOrderNums();
        //7天订单数
        Integer weekOrder = shopOrderMasterDao.findByWeekOrderNums();
        ///今天订单数
        Integer todayOrder = shopOrderMasterDao.findByTodayOrderNums();

        //小正鱼塘已完成总数
        Integer FinishOrderNums=shopOrderMasterDao.findByFinishOrderNums();
        //小正鱼塘未完成总数
        Integer UnfinishOrderNums=shopOrderMasterDao.findByUnfinishOrderNums();


        HashMap<String,Object> map=new HashMap<>();
        map.put("totalOrderNums",orderNums);
        map.put("weekOrderNums",weekOrder);
        map.put("todayOrderNums",todayOrder);
        map.put("FinishOrderNums",FinishOrderNums);
        map.put("UnfinishOrderNums",UnfinishOrderNums);
        return ResultUtils.success(200,"查询小正鱼塘订单数据成功",map);
    }

    //7天折线订单图
    @PostMapping("/xzyt/orderChart")
    public Result xzytOrderChart() {
        List<String> orderChart = serviceOrderDao.findByorderChart();
        List<String> orderChartNums = shopOrderMasterDao.findByorderChartNums();
        HashMap<String,Object> map=new HashMap<>();
        map.put("date",orderChart);
        map.put("count",orderChartNums);
        return ResultUtils.success(200,"查询小正鱼塘折线订单数成功",map);

    }

    //7天折线金额图
    @PostMapping("/xzyt/moneyChart")
    public Result xzytMoneyChart() {
        List<String> moneyChart = serviceOrderDao.findByorderChart();
        List<String> moneyChartMoney = shopOrderMasterDao.findByorderChartMoney();
        HashMap<String,Object> map=new HashMap<>();
        map.put("date",moneyChart);
        map.put("money",moneyChartMoney);
        return ResultUtils.success(200,"查询小正鱼塘折线金额成功",map);

    }


}
