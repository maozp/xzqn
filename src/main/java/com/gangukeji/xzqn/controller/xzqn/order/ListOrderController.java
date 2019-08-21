package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnServiceOrder;
import com.gangukeji.xzqn.entity.XzqnServiceOrderStatus;
import com.gangukeji.xzqn.entity.XzqnServicePublish;
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
@Deprecated
@RestController
@Slf4j
public class ListOrderController {
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

    @Log("查全部")
    @PostMapping({
            "serviceOrder/sendFindAll",
            "serviceOrder/sendFindUnFinish",
            "serviceOrder/sendFindFinish",
            "serviceOrder/receiveFindAll",
            "serviceOrder/receiveFindUnFinish",
            "serviceOrder/receiveFindFinish",
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
        boolean isSend = true;//是否是发单
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

        }
        /**
         * 师傅查
         */
        else if (uri.contains("receive")) {
            // TODO: 2019/6/21 bug修复
            isSend = false;
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
            }
            Collections.reverse(orderDetailViewList);

        }
        //设置status名描述
        setStatusNameAndDesV2(isSend, orderDetailViewList);
        //设置factorName
        setFactorName(orderDetailViewList);
        //排序 分页(被迫无奈,发单查要查两张表,所以做个假分页) 移除 已取消订单
        orderDetailViewList = orderDetailViewList.stream().sorted(Comparator.comparing(OrderDetailView::getCreateTime).reversed()).collect(toList());
        orderDetailViewList.removeIf(e -> e.publish.getIsCancel());
        orderDetailViewList = ListToPageUtil.go(jsonObject, orderDetailViewList);
        return ResultUtils.success(200, "查询成功", orderDetailViewList);

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
        statusList.forEach(e-> statusMap.put(e.getStatus(), e));
        orderDetailViewList.forEach(orderDetailView -> {
            if (isSend) {
                Integer status = orderDetailView.getStatus();
                XzqnServiceOrderStatus statusNow = statusMap.get(status);
                XzqnServiceOrderStatus statusNext = statusMap.get(status+1);
                XzqnServiceOrderStatus statusLast = statusMap.get(status-1);

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
                XzqnServiceOrderStatus statusNext = statusMap.get(status+1);
                XzqnServiceOrderStatus statusLast = statusMap.get(status-1);

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
}
