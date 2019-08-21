package com.gangukeji.xzqn.controller.xzqn.log;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.ServiceLogDao;
import com.gangukeji.xzqn.dao.ServiceOrderDao;
import com.gangukeji.xzqn.dao.ServicePublishDao;
import com.gangukeji.xzqn.dao.StatusDao;
import com.gangukeji.xzqn.entity.XzqnServiceOrderStatus;
import com.gangukeji.xzqn.entity.view.LogView;
import com.gangukeji.xzqn.entity.XzqnServiceLog;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
@RestController
@Deprecated
public class LogController {
    @Resource
    ServiceLogDao logDao;
    @Autowired
    StatusDao statusDao;
    @Autowired
    ServicePublishDao publishDao;
    @Autowired
    ServiceOrderDao orderDao;
    /**
     * 查询服务日志 userType orderId| publishId
     *
     * @return
     */
    @SuppressWarnings("Duplicates")
    @PostMapping("serviceOrder/log")
    @Log("日志--订单v2")
    public Result orderLogV2(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int userType = jsonObject.get("userType").getAsInt();
        String logTop="logTop";
        List<XzqnServiceOrderStatus> statusList = statusDao.findAll();
        Map<Integer, XzqnServiceOrderStatus> statusMap = new HashMap<>();
        statusList.forEach(e-> statusMap.put(e.getStatus(), e));
        List<LogView> logViews = new ArrayList<>();
        if (data.contains("publish")) {
            int publishId = jsonObject.get("publishId").getAsInt();
            List<XzqnServiceLog> publishList = logDao.findAllByPublishId(publishId);
            publishList.forEach(e -> {
                boolean isOk = false;
                if (e.getTime() != null) {
                    isOk = true;
                }
                XzqnServiceOrderStatus orderStatus = statusMap.get(e.getStatus());
                if (userType == 0) {
                    try {
                        logViews.add(new LogView(orderStatus.getSendName(), orderStatus.getSendDes(), e.getStatus(), e.getTime(), isOk,orderStatus.getSendTop()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        logViews.add(new LogView(orderStatus.getReceiveName(), orderStatus.getReceiveDes(), e.getStatus(), e.getTime(), isOk,orderStatus.getReceiveTop()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        if (data.contains("order")) {
            int orderId = jsonObject.get("orderId").getAsInt();
            List<XzqnServiceLog> orderList = logDao.findAllByOrderId(orderId);
            orderList.forEach(e -> {
                boolean isOk = false;
                if (e.getTime() != null) {
                    isOk = true;
                }
                XzqnServiceOrderStatus orderStatus = statusMap.get(e.getStatus());
                if (userType == 0) {
                    try {
                        logViews.add(new LogView(orderStatus.getSendName(), orderStatus.getSendDes(), e.getStatus(), e.getTime(), isOk,orderStatus.getSendTop()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        logViews.add(new LogView(orderStatus.getReceiveName(), orderStatus.getReceiveDes(), e.getStatus(), e.getTime(), isOk,orderStatus.getReceiveTop()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
//        一样多
        //设置logTop
        logViews.remove(0);

        for (LogView e : logViews) {
            if (e.getIsOk()) {
                logTop = e.getLogTop();
            }
        }
        //是否完成
        boolean tagOk = false;
        for (LogView e : logViews) {
            if (e.getStatus() == 11) {
                if (e.getIsOk()) {
                    tagOk = true;
                }
            }
        }
        if (tagOk) {
            logViews.forEach(e->{
                if (e.getStatus() == 12) {
                    e.setIsOk(true);
                }});

        }
        //发单方加一条日志发单成功
        buildLogView(logViews,jsonObject);

        return ResultUtils.success(200, logTop, logViews);
    }

    private void buildLogView(List<LogView> logViews, JsonObject jsonObject) {
        int publishId=orderDao.getPublishIdByOrderId(jsonObject.get("orderId").getAsInt());
        Date publishCreateTime=publishDao.getCreateTimeById(publishId);
        logViews.add(0,new LogView("下单成功","下单成功",4,publishCreateTime,true,"下单成功"));
    }

}
