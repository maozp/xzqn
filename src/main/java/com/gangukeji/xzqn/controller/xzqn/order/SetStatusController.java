package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnReceiveReport;
import com.gangukeji.xzqn.entity.XzqnServiceOrder;
import com.gangukeji.xzqn.entity.XzqnServicePublish;
import com.gangukeji.xzqn.entity.shop.XzqnShopPay;
import com.gangukeji.xzqn.utils.ListToString;
import com.gangukeji.xzqn.utils.MD5;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.pay.IpUtils;
import com.gangukeji.xzqn.utils.pay.WXPayConfig;
import com.gangukeji.xzqn.utils.pay.WXPayUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author: hx
 * @Date: 2019/6/18 19:53
 * @Description:
 */
@SuppressWarnings("Duplicates")
@Slf4j
@RestController
@Deprecated
public class SetStatusController {
    @Resource
    ServicePublishDao publishDao;
    @Resource
    ServiceOrderDao orderDao;
    @Resource
    UserDao userDao;
    @Resource
    ServiceLogDao logDao;
    @Resource
    ReceiveReportDao reportDao;

    // 订单状态改变全写一个接口
    @PostMapping("serviceOrder/setStatus")
    @Log("set--status 设置状态")
    // TODO: 2019/6/21  待优化  订单状态       switch
    public Object setStatus(@RequestBody String data, HttpServletRequest request) {
        /**
         * 取消订单
         */
        if (data.toLowerCase().contains("cancel")) {
            cancelPublish(data);
            return ResultUtils.success(200, "取消成功", data);
        }
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        final int STATUS = jsonObject.get("status").getAsInt();

        /**
         * 5  -1 抢单成功  订单status  +1 下单 status  +1 保存日志
         * 6  -1 发单方确认 订单 status  +1   保存日志
         * 7  -1 师傅上路  订单 status  +1   保存日志
         * 8  -1 师傅已到达 订单 status  +1   保存日志
         * 9  -1 师傅开始施工 订单 status  +1   保存日志
         * 10 -1 师傅提交报告 订单 status  +1   保存日志         保存施工报告
         * 11 -1 发单支付 订单 status  +1   保存日志
         * 12 -1 师傅确认收费  status  +1  保存日志
         */
        //发起抢单
        if (STATUS == 4) {
            return grabOrder(data);
        }
        int id = jsonObject.get("orderId").getAsInt();
        Integer orderStatus = orderDao.getStatus(id);
        log.error(orderStatus + "--------------");
        log.error(orderStatus + "");
        log.error(orderStatus + "--------------");
        if (STATUS != orderStatus) {
            log.error("status 传入非当前订单状态值");
            log.error("status 传入非当前订单状态值");
            log.error("status 传入非当前订单状态值");
            log.error("status 传入非当前订单状态值");
            log.error("status 传入非当前订单状态值");
            log.error("status 传入非当前订单状态值");
            return ResultUtils.error(-1, "当前状态值非订单当前状态");
        }
        //发单方确认
        if (STATUS == 5) {
            int orderId = jsonObject.get("orderId").getAsInt();
            orderDao.updateOrderStatus(STATUS + 1, orderId);
            updateLog(STATUS + 1, 1, 1, orderId);
            return ResultUtils.success(200, "操作成功", STATUS + 1);
        }
        //师傅开始出发
        if (STATUS == 6) {
            int orderId = jsonObject.get("orderId").getAsInt();
            orderDao.updateOrderStatus(STATUS + 1, orderId);
            updateLog(STATUS + 1, 1, 2, orderId);
            return ResultUtils.success(200, "操作成功", STATUS + 1);
        }
        //师傅到达
        if (STATUS == 7) {
            int orderId = jsonObject.get("orderId").getAsInt();
            orderDao.updateOrderStatus(STATUS + 1, orderId);
            updateLog(STATUS + 1, 1, 2, orderId);
            return ResultUtils.success(200, "操作成功", STATUS + 1);
        }
        //开始施工
        if (STATUS == 8) {
            int orderId = jsonObject.get("orderId").getAsInt();
            orderDao.updateOrderStatus(STATUS + 1, orderId);
            updateLog(STATUS + 1, 1, 2, orderId);
            return ResultUtils.success(200, "操作成功", STATUS + 1);
        }
        //施工完成保存施工报告
        if (STATUS == 9) {
            return addReport(data);
        }
        //发单方支付
        if (STATUS == 10) {
//            int orderId = jsonObject.get("orderId").getAsInt();
//            orderDao.updateOrderStatus(STATUS+1, orderId);//支付成功在payBack里面设置
//            updateLog(STATUS+1, 1, 1, orderId);
            return pay(data, request);
        }
        //setPay=true
//        //师傅确认支付
//        if (STATUS == 11) {
//            int orderId = jsonObject.get("orderId").getAsInt();
//            orderDao.updateOrderStatus(STATUS + 1, orderId);
//            updateLog(STATUS + 1, 1, 1, orderId);
//            return ResultUtils.success(200, "操作成功", STATUS + 1);
//        }
        //保存评价
//        if (STATUS == 11) {
//            return addComment(data);
//        }
//        if (STATUS == 12) {
//            int orderId = jsonObject.get("orderId").getAsInt();
//            orderDao.updateOrderStatus(STATUS + 1, orderId);
//            updateLog(STATUS + 1, 1, 2, orderId);
//            return ResultUtils.success(200, "操作成功", STATUS + 1);
//        }
        return ResultUtils.error(-1, "订单流程到头了哥们,恭喜通关");
    }

    @PostMapping("serviceOrder/noPay")
    @Log("noPay")
    public Result noPay(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int orderId = jsonObject.get("orderId").getAsInt();

        orderDao.updateIsPayByOrderId(true, orderId);
        orderDao.updateOrderStatus(11, orderId);
        updateLog(11, 1, 2, orderId);
        return ResultUtils.success(200,"支付成功,但没付钱","");
    }

    /**
     * 取消下单
     *
     * @param data
     */
    private void cancelPublish(String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        if (data.contains("isCancel")) {
            int publishId = jsonObject.get("publishId").getAsInt();
            publishDao.updateIsCancelAndStatus(publishId, 100);
        }
    }

    /**
     * 师傅抢单
     */
    private Result grabOrder(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int userId = jsonObject.get("userId").getAsInt();
        int publishId = jsonObject.get("publishId").getAsInt();
        int receiveUserId = getReceiveUserId(userId);
        XzqnServicePublish publish = publishDao.findById(publishId).get();
        //已被抢 不可抢
        if (publish.getStatus() == 5) {
            return ResultUtils.error(-1, "订单已被抢");
        }
        //抢单生成订单 该有的都设置
        XzqnServiceOrder order = new XzqnServiceOrder();
        buildOrder(publish, receiveUserId, order);
        order = orderDao.save(order);
        Integer orderId = order.getOrderId();
        //更新下单日志
        updateLog(order.getStatus(), 0, 2, jsonObject.get("publishId").getAsInt());
        //更新日志 batch
        updateBatchLogList(publishId, receiveUserId, orderId);
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("orderNo", order.getOrderNo());
        return ResultUtils.success(200, "抢单成功", map);
    }

    /**
     * 抢完单要update publishId对应日志的orderId receiveUserId
     * @param publishId
     * @param receiveUserId
     * @param orderId
     */
    private void updateBatchLogList(int publishId, int receiveUserId, Integer orderId) {
        logDao.updateOrderIdAndReceiveUserIdByPublishId(publishId, orderId, receiveUserId);
    }


    /**
     * 找到下单时new的status.size()条log的status对应的log 更新orderId time (userType谁操作1 发单 2 接单) sendUserId receiveUserId
     *
     * @param isOrder 0默认 1订单
     */
    private void updateLog(int status, int isOrder, int userType, int orderId) {
        Date time = new Date();
        if (isOrder != 1) {
            logDao.updateByPublishIdAndStatus(orderId, status, time, userType);
        }
        if (isOrder == 1) {
            logDao.updateByOrderIdAndStatus(orderId, status, time, userType);
        }
    }

    /**
     * 装配订单形成的参数
     *
     * @param publish
     * @param receiveUserId
     * @param order
     */
    private void buildOrder(XzqnServicePublish publish, int receiveUserId, XzqnServiceOrder order) {
        //修改下单表status=5
        publishDao.updatePublishStatus(5, publish.getId());
        BeanUtils.copyProperties(publish, order, "id");
        order.setOrderId(null);
        order.setStatus(5);
        order.setSendUserId(publish.getSendUserId());
        order.setIsPay(false);
        order.setReceiveUserId(receiveUserId);
        order.setPublishId(publish.getId());
        order.setOrderNo("XZQN_" + System.nanoTime());
    }

    private Integer getReceiveUserId(Integer userId) {

        return userDao.getReceiveUserIdByUserId(userId);
    }

    /**
     * 支付     一个订单号只能对应一个价格,否则报totalFee
     *
     * @param data
     * @param request
     * @return 小程序支付要用到的参数
     */
    private Map<String, String> pay(String data, HttpServletRequest request) {
        SortedMap<String, String> respMap = null;
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        String totalFee;
        try {
            int orderId = jsonObject.get("orderId").getAsInt();
            XzqnServiceOrder order = orderDao.findById(orderId).get();
            Integer userId = order.getUserId();
            totalFee = jsonObject.get("totalFee").getAsString().trim().replace(".", "");
            String openid = userDao.findById(userId).get().getOpenid();
            String spbillCreateIp = IpUtils.getIpAddr(request);
            String orderNo = order.getOrderNo();
            //避免相同订单号不能改价
            orderNo=orderNo + RandomUtils.nextInt(100000000, 999999999);
            String nonce_str = WXPayUtils.randomString();
            String body = "orderId" + orderId;
            SortedMap<String, String> packageParams = new TreeMap<>();
            packageParams = WXPayUtils.initMap(openid, totalFee, spbillCreateIp, orderNo, nonce_str, body, packageParams);
            String mapStr = WXPayUtils.combineMap(packageParams);
            String mapToSign = MD5.md5(mapStr, "&key=" + WXPayConfig.key).toUpperCase();
            String xml = WXPayUtils.getXML(openid, totalFee, spbillCreateIp, orderNo, nonce_str, body, mapToSign);
            RestTemplate restTemplate = new RestTemplate();
            System.out.println(xml);
            String wxResp = restTemplate.postForEntity("https://api.mch.weixin.qq.com/pay/unifiedorder", xml, String.class).getBody();
            wxResp = new String(wxResp.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String prePayId = WXPayUtils.getElement(wxResp, "prepay_id");
            String nonceStr = WXPayUtils.getElement(wxResp, "nonce_str");
            String sign = WXPayUtils.getElement(wxResp, "sign");
            respMap = new TreeMap<>();
            respMap.put("appId", WXPayConfig.appid);
            respMap.put("nonceStr", nonceStr);
            respMap.put("package", "prepay_id=" + prePayId);
            respMap.put("signType", "MD5");
            respMap.put("timeStamp", (new Date().getTime() + ""));
            String signTwo = WXPayUtils.generateSignature(respMap, WXPayConfig.key, sign);
            respMap.put("paySign", signTwo);
        } catch (Exception e) {
            e.printStackTrace();
            return respMap;
        }
        return respMap;
    }

    /**
     * 添加施工报告
     * @param data
     * @return 施工报告
     */
    public Result addReport(String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        final int STATUS = jsonObject.get("status").getAsInt();
        JsonArray jsonArray = jsonObject.get("img").getAsJsonArray();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        List<String> imgList = new Gson().fromJson(jsonArray, type);
        int orderId = jsonObject.get("orderId").getAsInt();
        int userId = jsonObject.get("userId").getAsInt();
        String des = jsonObject.get("des").getAsString();
        String content = jsonObject.get("content").getAsString();
        XzqnReceiveReport report = new XzqnReceiveReport();
        report.setOrderId(orderId);
        report.setUserId(userId);
        report.setContent(content);
        report.setDes(des);
        report.setImg(ListToString.goString(imgList, "@"));
        XzqnReceiveReport save = reportDao.save(report);
        orderDao.updateOrderStatus(STATUS + 1, orderId);
        updateLog(STATUS + 1, 1, 2, orderId);
        return ResultUtils.success(200, "施工报告添加成功", save);
    }


}
