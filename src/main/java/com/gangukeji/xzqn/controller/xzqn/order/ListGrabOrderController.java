package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnServiceOrderStatus;
import com.gangukeji.xzqn.entity.XzqnServicePublish;
import com.gangukeji.xzqn.entity.view.ReceiveDataInOrder;
import com.gangukeji.xzqn.utils.LocationUtils;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: hx
 * @Date: 2019/6/18 19:49
 * @Description:
 */
@Deprecated
@RestController
public class ListGrabOrderController {
    @Resource
    ReceiveUserDao receiveUserDao;
    @Resource
    ServicePublishDao publishDao;
    @Resource
    UserDao userDao;
    @Resource
    UserFactorDao factorDao;
    @Autowired
    StatusDao statusDao;

    /**
     * 抢单列表
     *
     * @param data
     * @return
     */
    @Log("抢单列表")
    @PostMapping({"serviceOrder/findAllGrab"})
    public Result findGrabAll(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        //设置分页和排序
        PageRequest pageRequest = null;
        String msg = null;
        //可以把分页抽出来   orders优先考虑 status 在考虑id
        Sort orders = (new Sort(Sort.Direction.ASC, "status")).and(new Sort(Sort.Direction.DESC, "id"));
        try {
            int page = jsonObject.get("page").getAsInt();
            int size = jsonObject.get("size").getAsInt();
            pageRequest = new PageRequest(page, size, orders);
            msg = "抢单列表page:" + page + "&size:" + size;
        } catch (Exception e) {
            pageRequest = new PageRequest(0, 10, orders);
            msg = "未排序只给10条";
        }
        //查找抢单列表
        int[] statusArray = new int[]{4, 5};
        List<XzqnServicePublish> grabList = publishDao.findAllByStatusInAndIsCancel(statusArray, pageRequest, false);

        // 1算距离 2factor 3状态名描述 4去掉 cancel
        setGrabList(grabList, jsonObject);
        return ResultUtils.success(200, msg, grabList);
    }

    /**
     * 抢单列表
     *
     * @param data
     * @return
     */
    @Log("抢单列表")
    @PostMapping({"serviceOrder/findAllGrab/search"})
    public Result findGrabAllSort(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        PageRequest pageRequest = initPage(jsonObject);
        String name = jsonObject.get("name").getAsString();
        //查找抢单列表
        int[] statusArray = new int[]{4, 5};
        List<XzqnServicePublish> grabList = publishDao.findAllByStatusInAndIsCancelAndJsonContaining(statusArray,pageRequest,false,name);
        // 1算距离 2factor 3状态名描述 4去掉 cancel
        setGrabList(grabList, jsonObject);
        return ResultUtils.success(200, "", grabList);
    }

    private PageRequest initPage(JsonObject jsonObject) {
        Sort orders = (new Sort(Sort.Direction.ASC, "status")).and(new Sort(Sort.Direction.DESC, "id"));
        int page = jsonObject.get("page").getAsInt();
        int size = jsonObject.get("size").getAsInt();
        return new PageRequest(page, size, orders);
    }

    /**
     * 1算距离 2factorName 3状态名描述 4去掉 cancel
     *
     * @param grabList
     * @param jsonObject
     */
    private void setGrabList(List<XzqnServicePublish> grabList, JsonObject jsonObject) {
        //设置距离 通过前端传的userId
        Integer receiveUserId = null;
        try {
            receiveUserId = userDao.getReceiveUserIdByUserId(jsonObject.get("userId").getAsInt());
            Optional<ReceiveDataInOrder> dataInOrder = receiveUserDao.findDataInOrder(receiveUserId);
            grabList.forEach(e -> e.setDistance(LocationUtils.getDistance(dataInOrder.get().getLat(), dataInOrder.get().getLng(), e.getLat(), e.getLng())));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            grabList.forEach(e1 -> e1.setDistance(0d));
        }
        //设置factor
        try {
            grabList.forEach(e -> e.setFactorName(factorDao.findById(e.getFactorId()).get().getName()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            grabList.forEach(e1 -> e1.setFactorName("政府"));
        }
        //设置status名描述v2
        List<XzqnServiceOrderStatus> statusList = statusDao.findAll();
        Map<Integer, XzqnServiceOrderStatus> statusMap = new HashMap<>();
        statusList.forEach(e -> statusMap.put(e.getStatus(), e));
        grabList.forEach(grabView -> {
            Integer status = grabView.getStatus();
            XzqnServiceOrderStatus statusNow = statusMap.get(status);
            grabView.setStatusName(statusNow.getReceiveName());
            grabView.setStatusDes(statusNow.getReceiveDes());

        });
        //判断师傅是否抢了该单   默认0  抢了  1
        if (receiveUserId != null) {
            for (XzqnServicePublish e : grabList) {
                if (e.getReceiveUserIdArray().contains(receiveUserId)) {
                    e.setStatusName("您已抢该订单");
                    e.setStatusDes("已抢单");
                    e.setGrabStatus(1);
                }
            }
        }

    }
}
