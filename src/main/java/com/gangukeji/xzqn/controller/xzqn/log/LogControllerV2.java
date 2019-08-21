package com.gangukeji.xzqn.controller.xzqn.log;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnServiceLog;
import com.gangukeji.xzqn.entity.XzqnServiceOrder;
import com.gangukeji.xzqn.entity.XzqnServiceOrderStatus;
import com.gangukeji.xzqn.entity.XzqnGrabRecord;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.entity.view.LogView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: hx
 * @Date: 2019/6/20 11:46
 * @Description:
 */
@Slf4j
@RestController
public class LogControllerV2 {
    @Resource
    ServiceLogDao logDao;
    @Autowired
    StatusDao statusDao;
    @Autowired
    ServicePublishDao publishDao;
    @Autowired
    ServiceOrderDao orderDao;
    @Autowired
    GrabRecordDao grabRecordDao;
    @Autowired
    UserDao userDao;
    /**
     * 查询服务日志 userType orderId     基本上不用判断publish了
     *
     * @return
     */
    @SuppressWarnings("Duplicates")
    @PostMapping("serviceOrder/log/v2")
    @Log("日志--订单v2")
    public Result orderLogV2(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int userType = jsonObject.get("userType").getAsInt();
        String logTop = "logTop";
        List<XzqnServiceOrderStatus> statusList = statusDao.findAll();
        Map<Integer, XzqnServiceOrderStatus> statusMap = new HashMap<>();
        statusList.forEach(e -> statusMap.put(e.getStatus(), e));
        List<LogView> logViews = new ArrayList<>();
//        if (data.contains("publish")) {
//            int publishId = jsonObject.get("publishId").getAsInt();
//            List<XzqnServiceLog> publishList = logDao.findAllByPublishId(publishId);
//            publishList.forEach(e -> {
//                boolean isOk = false;
//                if (e.getTime() != null) {
//                    isOk = true;
//                }
//                XzqnServiceOrderStatus orderStatus = statusMap.get(e.getStatus());
//                if (userType == 0) {
//                    try {
//                        logViews.add(new LogView(orderStatus.getSendName(), orderStatus.getSendDes(), e.getStatus(), e.getTime(), isOk, orderStatus.getSendTop()));
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                } else {
//                    try {
//                        logViews.add(new LogView(orderStatus.getReceiveName(), orderStatus.getReceiveDes(), e.getStatus(), e.getTime(), isOk, orderStatus.getReceiveTop()));
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            });
//        }
        if (data.contains("order")) {
            int orderId = jsonObject.get("orderId").getAsInt();
            List<XzqnServiceLog> orderList = logDao.findAllByOrderId(orderId);
            orderList.forEach(e -> {
                boolean isOk = false;
                if (e.getTime() != null) {
                    isOk = true;
                }
                XzqnServiceOrderStatus orderStatus = statusMap.get(e.getStatus());
                //发单日志添加
                if (userType == 0) {
                    try {
                        logViews.add(new LogView(orderStatus.getSendName(), orderStatus.getSendDes(), e.getStatus(), e.getTime(), isOk, orderStatus.getSendTop()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //接单日志添加
                else {
                    try {
                        logViews.add(new LogView(orderStatus.getReceiveName(), orderStatus.getReceiveDes(), e.getStatus(), e.getTime(), isOk, orderStatus.getReceiveTop()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        //一样多
        logViews.remove(0);
        //设置logTop
        for (LogView e : logViews) {
            if (e.getIsOk()) {
                logTop = e.getLogTop();
            }
        }
        //是否完成,最后一个节点设置
        boolean tagOk = false;
        for (LogView e : logViews) {
            if (e.getStatus() == 11) {
                if (e.getIsOk()) {
                    tagOk = true;
                }
            }
        }
        if (tagOk) {
            logViews.forEach(e -> {
                if (e.getStatus() == 12) {
                    e.setIsOk(true);
                }
            });
        }

        //不同用户日志特殊处理
        if (userType == 0) {
            buildSendLogView(logViews, jsonObject);
        } else {
            buildReceiveLogView(logViews, jsonObject);
        }
        return ResultUtils.success(200, logTop, logViews);
    }

    /**
     * 发单方增加下单成功
     *
     * @param logViews
     * @param jsonObject
     */
    private void buildSendLogView(List<LogView> logViews, JsonObject jsonObject) {
        int publishId = orderDao.getPublishIdByOrderId(jsonObject.get("orderId").getAsInt());
        Date publishCreateTime = publishDao.getCreateTimeById(publishId);
        logViews.add(0, new LogView("下单成功", "下单成功", 4, publishCreateTime, true, "下单成功"));
        //移除状态5
        logViews.removeIf(e -> e.getStatus() == 5);
    }

    /**
     * 师傅抢单成功的日志status=5的改成isOk time从grabRecord查
     */
    private void buildReceiveLogView(List<LogView> logViews, JsonObject jsonObject) {
        int orderId = jsonObject.get("orderId").getAsInt();
        XzqnServiceOrder order = orderDao.findById(orderId).get();
        Integer userId = userDao.getUserIdByReceiveUserId(order.getReceiveUserId());
        XzqnGrabRecord grabRecord = grabRecordDao.findByPublishIdAndUserId(order.getPublishId(), userId);
        logViews.forEach(e -> {
            if (e.getStatus() == 5) {
                e.setIsOk(true);
                e.setTime(grabRecord.getCreateTime());
            }
        });
    }
}
