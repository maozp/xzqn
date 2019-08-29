package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnReceiveReport;
import com.gangukeji.xzqn.entity.XzqnServiceOrder;
import com.gangukeji.xzqn.entity.XzqnServicePublish;
import com.gangukeji.xzqn.entity.XzqnGrabRecord;
import com.gangukeji.xzqn.entity.XzqnPreServiceImage;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
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
public class SetStatusControllerV3 {
    @Resource
    ServicePublishDao publishDao;
    @Resource
    GrabRecordDao grabRecordDao;
    @Resource
    ServiceOrderDao orderDao;
    @Resource
    UserDao userDao;
    @Resource
    ServiceLogDao logDao;
    @Resource
    ReceiveReportDao reportDao;
    @Autowired
    PreServiceImageDao preServiceImageDao;
    @Autowired
    OrderImgDao orderImgDao;
    @Resource
    AuthSignDao authSignDao;
    // 订单状态改变全写一个接口
    @PostMapping("serviceOrder/setStatus/v3")
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
         *                  保存日志并且status+1
         * 传4抢单                         status
         * 传5发单方确认师傅                status   publishId  receiveUserId
         * 传6师傅出发                     status
         * 传7师傅到达                     status
         * 传8师傅开始施工                 status
         * 传9师傅提交报告                 status
         * 传10发单方确认支付              status
         * 最终状态status=11
         */
        //发起抢单
        if (STATUS == 4) {
            return grabOrderv3(data);
        }
        //发单方确认师傅
        if (STATUS == 5) {
            int publishId = jsonObject.get("publishId").getAsInt();
            int receiveUserId = jsonObject.get("receiveUserId").getAsInt();
            XzqnServicePublish publish = publishDao.findById(publishId).get();
            publishDao.updatePublishStatus(STATUS, publishId);
            XzqnServiceOrder order = buildOrder(publish, receiveUserId);
            int orderId = order.getOrderId();
            updateBatchLogList(publishId, receiveUserId, orderId);
            updateLog(STATUS + 1, 1, 1, orderId);
            updateGrabRecord(receiveUserId, publishId);
            orderImgDao.updateOrderImg(orderId,publishId);
            return ResultUtils.success(200, "操作成功orderId:", orderId);
        }
        int id = jsonObject.get("orderId").getAsInt();
        Integer orderStatus = orderDao.getStatus(id);
        if (STATUS != orderStatus) {
            return ResultUtils.error(-1, "当前状态值非订单当前状态");
        }
        //师傅开始出发
        if (STATUS == 6) {
            int orderId = jsonObject.get("orderId").getAsInt();
            orderDao.updateOrderStatus(STATUS + 1, orderId);
            updateLog(STATUS + 1, 1, 2, orderId);
            return ResultUtils.success(200, "师傅开始出发成功:", STATUS + 1);
        }
        //师傅到达
        if (STATUS == 7) {
            int orderId = jsonObject.get("orderId").getAsInt();
            orderDao.updateOrderStatus(STATUS + 1, orderId);
            updateLog(STATUS + 1, 1, 2, orderId);
            return ResultUtils.success(200, "师傅到达成功", STATUS + 1);
        }
        //开始施工
        if (STATUS == 8) {
            int orderId = jsonObject.get("orderId").getAsInt();
            addPreServiceImage(orderId,jsonObject);
            orderDao.updateOrderStatus(STATUS + 1, orderId);
            updateLog(STATUS + 1, 1, 2, orderId);
            return ResultUtils.success(200, "开始施工成功", STATUS + 1);
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
        try {
            int receiveUserId = jsonObject.get("receiveUserId").getAsInt();
            authSignDao.addSignCard(receiveUserId);
        }catch (Exception e){

        }

        return ResultUtils.error(-1, "订单流程到头了哥们,恭喜通关");
    }

    /**
     * 开始施工前上传照片保存到xzqn_pre_service_image表
     * @param orderId
     * @param jsonObject
     */
    private void addPreServiceImage(int orderId, JsonObject jsonObject) {
        XzqnPreServiceImage entity = new XzqnPreServiceImage();

        JsonArray jsonArray = jsonObject.get("images").getAsJsonArray();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> stringList = new Gson().fromJson(jsonArray, type);
        StringBuilder stringBuilder = new StringBuilder();
        stringList.forEach(s -> stringBuilder.append(s).append("@"));

        entity.setOrderId(orderId);
        entity.setAddress(jsonObject.get("address").getAsString());
        entity.setImages(stringBuilder.toString());
        entity.setLat(jsonObject.get("lat").getAsBigDecimal());
        entity.setLng(jsonObject.get("lng").getAsBigDecimal());
        preServiceImageDao.save(entity);
    }

    private void updateGrabRecord(int receiveUserId, int publishId) {
        Integer userId = userDao.getUserIdByReceiveUserId(receiveUserId);
        grabRecordDao.updateByPublishIdGrabStatusId(publishId, -1);
        grabRecordDao.updateGrabStatusByPublishIdWhereUserIdIs(publishId, userId, 1);
    }

    @PostMapping("serviceOrder/noPay/v3")
    @Log("noPay")
    public Result noPay(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int orderId = jsonObject.get("orderId").getAsInt();

        orderDao.updateIsPayByOrderId(true, orderId);
        orderDao.updateOrderStatus(11, orderId);
        updateLog(11, 1, 2, orderId);
        return ResultUtils.success(200, "支付成功,但没付钱", "");
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
     * 师傅抢单v3
     */
    @Transactional
    public Result grabOrderv3(String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int userId = jsonObject.get("userId").getAsInt();
        int publishId = jsonObject.get("publishId").getAsInt();
        int receiveUserId = getReceiveUserId(userId);
        XzqnServicePublish publish = publishDao.findById(publishId).get();
        //判断是否抢过
        if (isGrab(receiveUserId, publish, userId)) {
            return ResultUtils.success(200, "该师傅已抢该订单", "该师傅已抢该订单");
        }
        //发单方已确认 不可抢
        if (publish.getStatus() == 5) {
            return ResultUtils.error(-1, "订单已被抢");
        }
        //生成抢单记录
        XzqnGrabRecord grabRecord = new XzqnGrabRecord();
        grabRecord.setPublishId(publishId);
        grabRecord.setUserId(userId);
        grabRecord.setGrabStatus(0);
        grabRecordDao.save(grabRecord);
        buildPublish(publish, receiveUserId);
        Map<String, Object> map = new HashMap<>();
        map.put("publishId", publishId);
        map.put("userId", userId);
        return ResultUtils.success(200, "抢单成功,待发单方确认", map);
    }

    /**
     * 防止师傅重复抢单
     *
     * @param receiveUserId
     * @param publish
     * @return
     */
    private boolean isGrab(int receiveUserId, XzqnServicePublish publish, int userId) {
//        if (publish.getReceiveUserIdArray().contains(receiveUserId)) {
//            return true;
//        }
//        return false;
        return grabRecordDao.existsByUserIdAndPublishId(userId, publish.getId());
    }

    /**
     * 发单方确认师傅update publishId对应日志的orderId receiveUserId
     *
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
     */
    private XzqnServiceOrder buildOrder(XzqnServicePublish publish, int receiveUserId) {
        XzqnServiceOrder order = new XzqnServiceOrder();
        BeanUtils.copyProperties(publish, order, "id");
        order.setOrderId(null);
        order.setStatus(6);
        order.setSendUserId(publish.getSendUserId());
        order.setIsPay(false);
        order.setReceiveUserId(receiveUserId);
        order.setPublishId(publish.getId());
        order.setOrderNo("XZQN_" + System.nanoTime());
        return orderDao.save(order);
    }

    /**
     * 存储抢单师傅队列
     *
     * @param publish
     * @param receiveUserId
     */
    private void buildPublish(XzqnServicePublish publish, int receiveUserId) {
        publishDao.updateReceiveUserIdListByPublishId(receiveUserId + ",", publish.getId());

    }

    /**
     * 获取师傅id
     *
     * @param userId
     * @return
     */
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
            orderNo = orderNo + RandomUtils.nextInt(100000000, 999999999);
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
     *
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
