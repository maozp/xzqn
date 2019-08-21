package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnServiceOrder;
import com.gangukeji.xzqn.entity.XzqnServiceOrderStatus;
import com.gangukeji.xzqn.entity.XzqnServicePublish;
import com.gangukeji.xzqn.entity.XzqnUserReceive;
import com.gangukeji.xzqn.entity.view.OrderDetailView;
import com.gangukeji.xzqn.entity.view.ReceiveDataInOrder;
import com.gangukeji.xzqn.utils.LocationUtils;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: hx
 * @Date: 2019/6/18 19:51
 * @Description:
 */
@Slf4j(topic = "distance test")
@RestController
public class DetailOrderController {
    Gson gson = new Gson();
    @Resource
    ServicePublishDao publishDao;
    @Resource
    ServiceOrderDao orderDao;
    @Resource
    ReceiveUserDao receiveUserDao;
    @Autowired
    StatusDao statusDao;
    @Autowired
    UserDao userDao;

    @Log("查详情")
    @PostMapping({"serviceOrder/receiveFindById", "serviceOrder/sendFindById"})
    public Result findOrderDetailV3(@RequestBody String data, HttpServletRequest request) {
            String uri = request.getRequestURI();
        OrderDetailView orderDetailView = routerView(data, uri);
        List<XzqnServiceOrderStatus> statusList = statusDao.findAll();
        Map<Integer, String> sendDesMap = new HashMap<>();
        Map<Integer, String> sendNameMap = new HashMap<>();
        Map<Integer, String> receiveDesMap = new HashMap<>();
        Map<Integer, String> receiveNameMap = new HashMap<>();
        statusList.forEach(orderStatus -> {
            sendDesMap.put(orderStatus.getStatus(), orderStatus.getSendDes());
            sendNameMap.put(orderStatus.getStatus(), orderStatus.getSendName());
            receiveDesMap.put(orderStatus.getStatus(), orderStatus.getReceiveDes());
            receiveNameMap.put(orderStatus.getStatus(), orderStatus.getReceiveName());
        });
        setDistance(orderDetailView,data);
        buildStatusDes(uri, orderDetailView, sendDesMap, sendNameMap, receiveDesMap, receiveNameMap);
        buildImg(orderDetailView,statusList);
        return ResultUtils.success(200, "查询成功", orderDetailView);
    }

    private void buildImg(OrderDetailView orderDetailView,List<XzqnServiceOrderStatus> statusList ) {
        String img="";
        for (XzqnServiceOrderStatus orderStatus:statusList) {
            if (orderDetailView.getStatus().equals(orderStatus.getStatus())) {
                img = orderStatus.getImg();
                break;
            }
        }
        orderDetailView.setImg(img);
    }

    private void buildStatusDes(String uri, OrderDetailView orderDetailView, Map<Integer, String> sendDesMap, Map<Integer, String> sendNameMap, Map<Integer, String> receiveDesMap, Map<Integer, String> receiveNameMap) {
        if (uri.contains("receive")) {
            //设置status名描述receive
            try {
                orderDetailView.setStatusName(receiveNameMap.get(orderDetailView.getStatus()));
                orderDetailView.setStatusDes(receiveDesMap.get(orderDetailView.getStatus()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                orderDetailView.setStatusNameLast(receiveNameMap.get(orderDetailView.getStatus()-1));
                orderDetailView.setStatusDesLast(receiveDesMap.get(orderDetailView.getStatus()-1));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                orderDetailView.setStatusNameNext(receiveNameMap.get(orderDetailView.getStatus()+1));
                orderDetailView.setStatusDesNext(receiveDesMap.get(orderDetailView.getStatus()+1));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            //设置status名描述send
            try {
                orderDetailView.setStatusName(sendNameMap.get(orderDetailView.getStatus()));
                orderDetailView.setStatusDes(sendDesMap.get(orderDetailView.getStatus()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                orderDetailView.setStatusNameLast(sendNameMap.get(orderDetailView.getStatus()-1));
                orderDetailView.setStatusDesLast(sendDesMap.get(orderDetailView.getStatus()-1));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                orderDetailView.setStatusNameNext(sendNameMap.get(orderDetailView.getStatus()+1));
                orderDetailView.setStatusDesNext(sendDesMap.get(orderDetailView.getStatus()+1));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        List<XzqnServiceOrderStatus> statusList = statusDao.findAll();
        statusList.forEach(status -> {
            if (status.getStatus().equals(orderDetailView.getStatus())) {
                orderDetailView.setSendClick(status.getSendClick());
            }
        });
    }

    /**
     * set distance
     *
     * @param orderDetailView
     */
    private void setDistance(OrderDetailView orderDetailView,String data) {
        //设置与订单的距离
        try {
            Integer receiveUserId = orderDetailView.getOrder().getReceiveUserId();
            log.error("receiveUserId:"+receiveUserId);
            XzqnUserReceive userReceive = receiveUserDao.findById(receiveUserId).get();
            BigDecimal lat = userReceive.getLat();
            BigDecimal lng = userReceive.getLng();
            log.error(lat+"");
            log.error(lng+"");
            orderDetailView.setDistance(LocationUtils.getDistance(lat, lng, orderDetailView.getPublish().getLat(), orderDetailView.getPublish().getLng()));
            log.error(orderDetailView.getPublish().getLat()+"");
            log.error(orderDetailView.getPublish().getLng()+"");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //publish没有receiveUserId所以拿userId设置与订单的距离
            try {
                int userId = new JsonParser().parse(data).getAsJsonObject().get("userId").getAsInt();
                Integer receiveUserId = userDao.getReceiveUserIdByUserId(userId);
                ReceiveDataInOrder receiveDataInOrder = receiveUserDao.findDataInOrder(receiveUserId).get();
                orderDetailView.setDistance(LocationUtils.getDistance(receiveDataInOrder.getLat(), receiveDataInOrder.getLng(), orderDetailView.getPublish().getLat(), orderDetailView.getPublish().getLng()));
            } catch (Exception ex) {
                //没传用户id,师傅看不到距离
                System.out.println(ex.getMessage());
                orderDetailView.setDistance(10086d);
            }
        }
    }

    /**
     * 状态描述不同需要路由下
     *
     * @param data
     * @param uri
     * @return
     */
    private OrderDetailView routerView(String data, String uri) {


        OrderDetailView orderDetailView = new OrderDetailView();
        if (uri.contains("send")) {
            if (data.contains("publishId")) {
                orderDetailView = sendPublishDetail(data);
            }
            if (data.contains("orderId")) {
                orderDetailView = sendOrderDetail(data);
            }

        }
        if (uri.contains("receive")) {
            if (data.contains("publishId")) {
                orderDetailView = receivePublishDetail(data);//先用用发单的publish视图,好像可以通用
            }
            if (data.contains("orderId")) {
                orderDetailView = receiveOrderDetail(data);
            }


        }
        return orderDetailView;
    }

    /**
     * 发单看下单
     */
    public OrderDetailView sendPublishDetail(String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int publishId = jsonObject.get("publishId").getAsInt();
        OrderDetailView orderDetailView = new OrderDetailView();
        XzqnServicePublish publish = getPublish(publishId);
        orderDetailView.setPublish(publish);
        return orderDetailView;
    }

    /**
     * 接单看下单
     */
    public OrderDetailView receivePublishDetail(String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int publishId = jsonObject.get("publishId").getAsInt();
        OrderDetailView orderDetailView = new OrderDetailView();
        XzqnServicePublish publish = getPublish(publishId);
        orderDetailView.setPublish(publish);
        return orderDetailView;
    }

    /**
     * 发单看订单
     */
    public OrderDetailView sendOrderDetail(String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int orderId = jsonObject.get("orderId").getAsInt();
        OrderDetailView orderDetailView = new OrderDetailView();
        //获得order
        XzqnServiceOrder order = getOrder(orderId);
        orderDetailView.setOrder(order);
        //获得publish
        XzqnServicePublish publish = getPublish(order.getPublishId());
        orderDetailView.setPublish(publish);
        return orderDetailView;
    }

    /**
     * 接单看订单
     */
    public OrderDetailView receiveOrderDetail(String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int orderId = jsonObject.get("orderId").getAsInt();
        OrderDetailView orderDetailView = new OrderDetailView();
        //获得order
        XzqnServiceOrder order = getOrder(orderId);
        orderDetailView.setOrder(order);
        //获得publish
        XzqnServicePublish publish = getPublish(order.getPublishId());
        orderDetailView.setPublish(publish);
        return orderDetailView;
    }


    private XzqnServicePublish getPublish(Integer publishId) {
        XzqnServicePublish publish = publishDao.findById(publishId).get();
        return publish;
    }

    private XzqnServiceOrder getOrder(int orderId) {
        XzqnServiceOrder order = orderDao.findById(orderId).get();
        return order;
    }
}
