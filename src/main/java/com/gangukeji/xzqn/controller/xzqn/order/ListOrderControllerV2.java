package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnServiceOrder;
import com.gangukeji.xzqn.entity.XzqnServiceOrderStatus;
import com.gangukeji.xzqn.entity.XzqnServicePublish;
import com.gangukeji.xzqn.entity.XzqnGrabRecord;
import com.gangukeji.xzqn.entity.view.OrderDetailView;
import com.gangukeji.xzqn.utils.ListToPageUtil;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @Author: hx
 * @Date: 2019/6/18 19:52
 * @Description:
 */
@SuppressWarnings("ALL")
@RestController
@Slf4j
public class ListOrderControllerV2 {
    @Resource
    ServicePublishDao publishDao;
    @Resource
    ServiceOrderDao orderDao;
    @Resource
    UserDao userDao;
    @Resource
    UserFactorDao factorDao;
    @Resource
    StatusDao statusDao;
    @Resource
    GrabRecordDao grabRecordDao;

    @Log("查全部")
    @PostMapping({
            "serviceOrder/sendFindAll/v2",
            "serviceOrder/sendFindUnFinish/v2",
            "serviceOrder/sendFindFinish/v2",
            "serviceOrder/receiveFindAll/v2",
            "serviceOrder/receiveFindUnFinish/v2",
            "serviceOrder/receiveFindFinish/v2",
/*            "serviceOrder/sendFindAll/v2",
            "serviceOrder/sendFindUnFinish/v2",
            "serviceOrder/sendFindFinish/v2",
            "serviceOrder/receiveFindAll/v2",
            "serviceOrder/receiveFindUnFinish/v2",
            "serviceOrder/receiveFindFinish/v2",*/
    })
    public Result findAllOrder(@RequestBody String data, HttpServletRequest request) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int userId = jsonObject.get("userId").getAsInt();
        List<OrderDetailView> orderDetailViewList = new ArrayList<>();
        String uri = request.getRequestURI();
        /**
         * 发单查
         */
        if (uri.contains("send")) {
            Integer sendUserId = getSendUserId(userId);
            /**
             *  完成 order:status>10(sendUserId)
             */
            if (uri.contains("FindFinish")) {
                List<XzqnServiceOrder> orderList = orderDao.findBySendUserIdAndStatusMoreThenValue(sendUserId, 10);
                for (XzqnServiceOrder order : orderList) {
                    OrderDetailView view = new OrderDetailView();
                    view.setOrder(order);
                    view.setPublish(publishDao.findById(order.getPublishId()).get());
                    orderDetailViewList.add(view);
                }
            }
            /**
             *  发单查未完成 publish:status=4(sendUserId)&order:all(sendUserId)
             */
            if (uri.contains("UnFinish")) {
                List<XzqnServiceOrder> orderList = orderDao.findBySendUserIdAndStatusLessThanValue(sendUserId, 11);
                List<XzqnServicePublish> publishList = publishDao.findBySendUserIdAndStatusLessThanValue(sendUserId, 5);
                //
                for (XzqnServiceOrder order : orderList) {
                    OrderDetailView view = new OrderDetailView();
                    view.setOrder(order);
                    XzqnServicePublish publish = publishDao.findById(order.getPublishId()).get();
                    view.setPublish(publish);
                    orderDetailViewList.add(view);
                }
                for (XzqnServicePublish publish : publishList) {
                    OrderDetailView view = new OrderDetailView();
                    view.setPublish(publish);
                    orderDetailViewList.add(view);
                }
            }

            /**
             *  发单查全部 publish:status=4(sendUserId)&order:all(sendUserId)
             */
            if (uri.contains("FindAll")) {
                List<XzqnServicePublish> publishList = publishDao.findAllBySendUserIdAndStatus(sendUserId, 4);
                List<XzqnServiceOrder> orderList = orderDao.findAllBySendUserId(sendUserId);
                for (XzqnServiceOrder order : orderList) {
                    OrderDetailView view = new OrderDetailView();
                    view.setOrder(order);
                    view.setPublish(publishDao.findById(order.getPublishId()).get());
                    orderDetailViewList.add(view);
                }
                for (XzqnServicePublish publish : publishList) {
                    OrderDetailView view = new OrderDetailView();
                    view.setPublish(publish);
                    orderDetailViewList.add(view);
                }

            }
            //设置status名描述
            setStatusNameAndDesV2(true, orderDetailViewList);
        }
        /**
         * 师傅查
         */
        else if (uri.contains("receive")) {
            // TODO: 2019/6/21 bug修复
            Integer receiveUserId = getReceiveUserId(userId);
            /**
             * 师傅查完成  order:status>9(receiveUserId)
             */
            if (uri.contains("FindFinish")) {
                List<XzqnServiceOrder> orderList = orderDao.findByReceiveUserIdAndStatusMoreThenValue(receiveUserId, 9);
                for (XzqnServiceOrder order : orderList) {
                    OrderDetailView view = new OrderDetailView();
                    view.setOrder(order);
                    view.setPublish(publishDao.findById(order.getPublishId()).get());
                    orderDetailViewList.add(view);
                }
                //设置status名描述
                setStatusNameAndDesV2(false, orderDetailViewList);
            }
            /**
             * 师傅查未完成order:status<10(receiveUserId)
             */
            if (uri.contains("UnFinish")) {
                List<XzqnServiceOrder> orderList = orderDao.findByReceiveUserIdAndStatusLessThenValue(receiveUserId, 10);
                for (XzqnServiceOrder order : orderList) {
                    OrderDetailView view = new OrderDetailView();
                    view.setOrder(order);
                    view.setPublish(publishDao.findById(order.getPublishId()).get());
                    orderDetailViewList.add(view);
                }
                //设置status名描述
                setStatusNameAndDesV2(false, orderDetailViewList);
                // 加队列中的publish 并根据grabStatus设置状态描述
//                addGrabPublish(orderDetailViewList,jsonObject);
            }
            /**
             * 师傅查全部order(receiveUserId)
             */
            if (uri.contains("FindAll")) {
                List<XzqnServiceOrder> orderList = orderDao.findAllByReceiveUserId(receiveUserId);
                for (XzqnServiceOrder order : orderList) {
                    OrderDetailView view = new OrderDetailView();
                    view.setOrder(order);
                    view.setPublish(publishDao.findById(order.getPublishId()).get());
                    orderDetailViewList.add(view);
                }
                //设置status名描述
                setStatusNameAndDesV2(false, orderDetailViewList);
                // 加队列中的publish 并根据grabStatus设置状态描述
                addGrabPublish(orderDetailViewList,jsonObject);
            }
            Collections.reverse(orderDetailViewList);

        }

        //设置factorName
        setFactorName(orderDetailViewList);
        //排序 分页(被迫无奈,发单查要查两张表,所以做个假分页) 移除 已取消订单
        orderDetailViewList = orderDetailViewList.stream().sorted(Comparator.comparing(OrderDetailView::getCreateTime).reversed()).collect(toList());
        orderDetailViewList.removeIf(e -> e.publish.getIsCancel());
        orderDetailViewList = ListToPageUtil.go(jsonObject, orderDetailViewList);
        return ResultUtils.success(200, "查询成功", orderDetailViewList);

    }

    /**
     * 加队列中的publish 并根据grabStatus设置状态描述
     * 0抢了 -1 抢失败了  有order了就不用设置publish了
     * @param orderDetailViewList
     * @param jsonObject
     */
    private void addGrabPublish(List<OrderDetailView> orderDetailViewList, JsonObject jsonObject) {
        int userId = jsonObject.get("userId").getAsInt();
        //抢了待确认数据库grabStatus=0   前端显示1
        List<XzqnGrabRecord> grabRecordWaitList=grabRecordDao.findByUserIdAndGrabStatus(userId, 0);
        Set<Integer> publishIdSetWait = new HashSet<>();
        grabRecordWaitList.forEach(e->publishIdSetWait.add(e.getPublishId()));
        List<XzqnServicePublish> publishWaitList = publishDao.findAllById(publishIdSetWait);
        publishWaitList.forEach(publish->{
            OrderDetailView orderView=new OrderDetailView();
            orderView.setStatusDes("已抢单");
            orderView.setStatusName("等待发单方确认");
            publish.setGrabStatus(0);
            orderView.setPublish(publish);
            orderDetailViewList.add(orderView);
        });
        //已被抢(没抢成功)数据 grabStatus=-1 前端显示
        List<XzqnGrabRecord> grabRecordUnSuccessList=grabRecordDao.findByUserIdAndGrabStatus(userId, -1);
        Set<Integer> publishIdSetUnSuccess = new HashSet<>();
        grabRecordUnSuccessList.forEach(e->publishIdSetUnSuccess.add(e.getPublishId()));
        List<XzqnServicePublish> publishUnSuccessList = publishDao.findAllById(publishIdSetUnSuccess);
        publishUnSuccessList.forEach(publish->{
            OrderDetailView orderView=new OrderDetailView();
            publish.setGrabStatus(-1);
            orderView.setStatusDes("已抢单");
            orderView.setStatusName("抢单失败");
            orderView.setPublish(publish);
            orderDetailViewList.add(orderView);
        });
    }

    /**
     * 状态名描述设置
     *
     * @param isSend
     * @param orderDetailViewList
     */
    private void setStatusNameAndDesV2(boolean isSend, List<OrderDetailView> orderDetailViewList) {
        List<XzqnServiceOrderStatus> statusList = statusDao.findAll();
        Map<Integer, XzqnServiceOrderStatus> statusMap = new HashMap<>();
        statusList.forEach(e -> statusMap.put(e.getStatus(), e));
        orderDetailViewList.forEach(orderDetailView -> {
            if (isSend) {
                Integer status = orderDetailView.getStatus();
                XzqnServiceOrderStatus statusNow = statusMap.get(status);
                XzqnServiceOrderStatus statusNext = statusMap.get(status + 1);
                XzqnServiceOrderStatus statusLast = statusMap.get(status - 1);

                try {
                    orderDetailView.setStatusNameLast(statusLast.getSendListName());
                    orderDetailView.setStatusDesLast(statusLast.getSendListDes());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                try {
                    orderDetailView.setStatusNameNext(statusNext.getSendListName());
                    orderDetailView.setStatusDesNext(statusNext.getSendListDes());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                try {
                    orderDetailView.setStatusName(statusNow.getSendListName());
                    orderDetailView.setStatusDes(statusNow.getSendListDes());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


            } else {
                Integer status = orderDetailView.getStatus();
                XzqnServiceOrderStatus statusNow = statusMap.get(status);
                XzqnServiceOrderStatus statusNext = statusMap.get(status + 1);
                XzqnServiceOrderStatus statusLast = statusMap.get(status - 1);

                try {
                    orderDetailView.setStatusNameLast(statusLast.getReceiveListName());
                    orderDetailView.setStatusDesLast(statusLast.getReceiveListDes());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                try {
                    orderDetailView.setStatusNameNext(statusNext.getReceiveListName());
                    orderDetailView.setStatusDesNext(statusNext.getReceiveListDes());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                try {
                    orderDetailView.setStatusName(statusNow.getReceiveListName());
                    orderDetailView.setStatusDes(statusNow.getReceiveListDes());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


            }

        });
    }

    /**
     * 计费系数名设置
     *
     * @param orderDetailViewList
     */
    private void setFactorName(List<OrderDetailView> orderDetailViewList) {
        //设置factor
        orderDetailViewList.forEach(e1 -> {
            XzqnServicePublish publish = e1.getPublish();
            try {
                publish.setFactorName(factorDao.findById(publish.getFactorId()).get().getName());
            } catch (Exception e) {
//                e.printStackTrace();
                publish.setFactorName("政府");
            }

        });

    }


    private Integer getSendUserId(Integer userId) {
        return userDao.getSendUserIdByUserId(userId);
    }

    private Integer getReceiveUserId(Integer userId) {
        return userDao.getReceiveUserIdByUserId(userId);
    }
//    /**
//     * 设置抢单记录
//     * 和师傅看未完成,全部订单列表,的保持一致
//     * @param publishList
//     * @param userId
//     */
//    private void buildMultiGrabDes(List<XzqnServicePublish> publishList, Integer userId) {
//        for (XzqnServicePublish publish : publishList) {
//            XzqnGrabRecord grabRecord = grabRecordDao.findByPublishIdAndUserId(publish.getId(), userId);
//            int grabStatus;
//            //抢单记录为空
//            if (grabRecord == null) {
//                publish.setStatusName("抢单");
//                publish.setStatusDes("未抢单");
//                grabStatus=2;//  没抢2 所以查不到记录
//            }
////            有记录
//            else {
//                grabStatus = grabRecord.getGrabStatus();
//                if (grabStatus == 0) {
//                    publish.setStatusName("等待发单方确认");
//                    publish.setStatusDes("已抢单");
//                    grabStatus = 0;
//                }
//                if (grabStatus == 1) {
//                    publish.setStatusName("抢单成功");
//                    publish.setStatusDes("已抢单");
//                    grabStatus=1;
//                }
//                if (grabStatus == -1) {
//                    publish.setStatusName("抢单失败");
//                    publish.setStatusDes("已抢单");
//                    grabStatus=-1;
//                }
//            }
//            publish.setGrabStatus(grabStatus);
//        }
//    }
}
