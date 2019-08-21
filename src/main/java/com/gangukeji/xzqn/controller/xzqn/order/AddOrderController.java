package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.controller.xzqn.other.WebSocket;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnServiceLog;
import com.gangukeji.xzqn.entity.XzqnServiceOrderStatus;
import com.gangukeji.xzqn.entity.XzqnServicePublish;
import com.gangukeji.xzqn.entity.XzqnOrderImg;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: hx
 * @Date: 2019/6/18 19:41
 * @Description:
 */
@RestController
public class AddOrderController {

    Gson gson = new Gson();
    @Resource
    SendUserDao sendUserDao;//需要更新发单用户name phone ...
    @Resource
    ServicePublishDao publishDao;
    @Resource
    ServiceLogDao logDao;
    @Resource
    UserFactorDao factorDao;
    @Resource
    StatusDao statusDao;
    @Autowired
    OrderImgDao orderImgDao;
    @Autowired
    WebSocket webSocket;
    @Resource
    UserDao userDao;//获取发单用户id

    /**
     * 立即下单
     */
    @PostMapping("serviceOrder/add")
    @Log("立即下单")
    public Result addOrderV3(@RequestBody String data) throws Exception {
        data = data.replaceAll("feeItemIds", "fuck feeItemIds");
        XzqnServicePublish publish = gson.fromJson(data, XzqnServicePublish.class);
        String json = getJson(data);
        publish.setJson(json);
        publish.setStatus(4);
        publish.setIsCancel(false);
        publish.setCateId(12);
        publish.setReceiveUserIdList("");
        publish.setSendUserId(userDao.getSendUserIdByUserId(publish.getUserId()));// 1todo兼容老版本 查找可能会拿sendUserId查
        publish.setIsCheck(0);

        Integer publishId = publishDao.save(publish).getId();

        //保存后设置
        String factorName = factorDao.findNameById(publish.getFactorId());
        publish.setFactorName(factorName);
        orderThing(data, publishId, publish.getUserId());

        //发送websocket消息
        webSocket.sendMessage("有新的订单来了");

        return ResultUtils.success(200, "下单成功", publish);
    }
    @PostMapping("serviceOrder/add/v2")
    @Log("立即下单")
    public Result addOrderV4(@RequestBody String data) throws Exception {
        data = data.replaceAll("feeItemIds", "fuck feeItemIds");
        XzqnServicePublish publish = gson.fromJson(data, XzqnServicePublish.class);
        String json = getJson(data);
        publish.setJson(json);
        publish.setStatus(4);
        publish.setIsCancel(false);
        publish.setCateId(12);
        publish.setReceiveUserIdList("");
        publish.setSendUserId(userDao.getSendUserIdByUserId(publish.getUserId()));// 1todo兼容老版本 查找可能会拿sendUserId查
        publish.setIsCheck(0);

        Integer publishId = publishDao.save(publish).getId();

        //保存后设置
        String factorName = factorDao.findNameById(publish.getFactorId());
        publish.setFactorName(factorName);
        orderThing(data, publishId, publish.getUserId());
        saveImg(data,publishId);

        //发送websocket消息
        webSocket.sendMessage("有新的订单来了");

        return ResultUtils.success(200, "下单成功", publish);
    }

    /**
     * 保存下单图片,可不保存
     * @param data
     * @param publishId
     */
    private void saveImg(String data, Integer publishId) {
        try {
            JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("imgs").getAsJsonArray();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> imgs = new Gson().fromJson(jsonArray, type);
            StringBuilder stringBuilder = new StringBuilder();
            imgs.forEach(img -> stringBuilder.append(img).append("@"));
            XzqnOrderImg orderImg = new XzqnOrderImg();
            orderImg.setImgs(stringBuilder.toString());
            orderImg.setPublishId(publishId);
            orderImgDao.save(orderImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取前端传的project json字段
     *
     * @param data
     * @return
     */
    private String getJson(@RequestBody String data) {
        try {
            return new JsonParser().parse(data).getAsJsonObject().get("project").toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 下单需要做的其它操作
     * 1保存日志
     * 2保存分类
     * 3更新下单用户信息
     *
     * @param data
     */
    private void orderThing(String data, Integer publishId, int userId) {
        Integer sendUserId = userDao.getSendUserIdByUserId(userId);
        updateSendUser(data, sendUserId);//ok
        initLog(publishId, sendUserId);//ok
//        saveCateAndProp(data, publishId);//ok
    }

    /**
     * 保存下单填写的信息到sendUser表
     *
     * @param data
     */
    private void updateSendUser(String data, int sendUserId) {
        // name phone companyName companyPhone
        Map map = gson.fromJson(data, Map.class);
        String name = (String) map.get("name");
        String phone = (String) map.get("phone");
        String companyName = (String) map.get("companyName");
        String companyPhone = (String) map.get("companyPhone");
        sendUserDao.updateUserInfo(name, phone, companyName, companyPhone, sendUserId);
    }

    /**
     * 保存订单详情要展示的日志 N 条 待测试
     *
     * @param
     */
    private void initLog(int publishId, int sendUserId) {
        List<XzqnServiceOrderStatus> statusList = statusDao.findAll();
        List<XzqnServiceLog> logList = new ArrayList<>();
        statusList.forEach(e -> {
            XzqnServiceLog log = new XzqnServiceLog(0, publishId, sendUserId, e.getStatus(),e.getReceiveName(),e.getReceiveDes(),e.getSendName(),e.getSendDes());
            logList.add(log);
        });
        //设置第一条日志
        logList.get(0).setTime(new Date(System.currentTimeMillis()));
        logList.get(0).setUserType(1);
        logDao.saveAll(logList);
    }

//    /**
//     * 保存cateIds propIds ok
//     *
//     * @param data
//     */
//    private void saveCateAndProp(String data, Integer publishId) {
//        //把cateId取到 cateName就有了 然后设置publishId orderCate搞定
//        try {
//            CateJson cateJson = gson.fromJson(data, CateJson.class);
//            List<CateJson.OrderCateList> orderCateListList = cateJson.getOrderCateList();
//            for (CateJson.OrderCateList orderCateList : orderCateListList) {
//                for (CateJson.OrderCateList.CatePartList catePartList : orderCateList.getCatePartList()) {
//                    Integer cateId = catePartList.getCateId();
//                    List<Integer> props = catePartList.getProps();
//                    Integer orderCateId = orderCateDao.save(new XzqnOrderCate(null, publishId, 0, cateId)).getId();
//                    props.forEach(propId -> orderPropDao.save(new XzqnOrderProp(null, publishId, 0, propId, orderCateId)));
//
//                }
//            }
//        } catch (JsonSyntaxException e) {
//            System.out.println("不保存分类和属性");
//        }
//    }

    @PostMapping("serviceOrder/update")
    @Log("修改订单")
    public Result updateOrder(@RequestBody String data,@RequestParam(value="publishId") Integer publishId) throws Exception {
        //有ID修改，没有ID新增
        XzqnServicePublish publish=publishDao.findById(publishId).get();
        data = data.replaceAll("feeItemds", "fuck feeItemIds");
         publish = gson.fromJson(data, XzqnServicePublish.class);
        String json = getJson(data);
        publish.setJson(json);
        publish.setStatus(4);
        publish.setIsCancel(false);
        publish.setCateId(12);
        publish.setReceiveUserIdList("");
        publish.setSendUserId(userDao.getSendUserIdByUserId(publish.getUserId()));// 1todo兼容老版本 查找可能会拿sendUserId查
        publish.setIsCheck(0);

        publishDao.save(publish);

        //保存后设置
        String factorName = factorDao.findNameById(publish.getFactorId());
        publish.setFactorName(factorName);
        orderThing(data, publishId, publish.getUserId());
        saveImg(data,publishId);

        return ResultUtils.success(200, "修改订单成功", publish);
    }


}
