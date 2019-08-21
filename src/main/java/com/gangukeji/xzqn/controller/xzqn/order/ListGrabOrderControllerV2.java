package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnServiceOrderStatus;
import com.gangukeji.xzqn.entity.XzqnServicePublish;
import com.gangukeji.xzqn.entity.XzqnGrabRecord;
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
import java.util.*;

/**
 * @Author: hx
 * @Date: 2019/6/18 19:49
 * @Description:
 */

@RestController
public class ListGrabOrderControllerV2 {
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
    @Autowired
    GrabRecordDao grabRecordDao;

    /**
     * 抢单列表
     *
     * @param data
     * @return
     */
    @Log("抢单列表")
    @PostMapping({"serviceOrder/findAllGrab/v2"})
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
        setReceiveUserIdList(grabList);
        return ResultUtils.success(200, msg, grabList);
    }

    /**
     * 设置师傅抢单列表抢了该订单的师傅的ids
     * @param grabList
     */
    private void setReceiveUserIdList(List<XzqnServicePublish> grabList) {

        grabList.forEach(e->{
            List<XzqnGrabRecord> grabRecords = grabRecordDao.findByPublishIdOrderByIdDesc(e.getId());
            ArrayList<Integer> receiveUserIds = new ArrayList<>();
            buildReceiveUserIds(receiveUserIds,grabRecords);
            e.setReceiveUserIdArray(receiveUserIds);
        });
    }
    /**
     * 设置师傅抢单列表抢了该订单的师傅的ids
     */
    private void buildReceiveUserIds(ArrayList<Integer> receiveUserIds, List<XzqnGrabRecord> grabRecords) {
        grabRecords.forEach(e->{
            Integer receiveUserId = userDao.getReceiveUserIdByUserId(e.getUserId());
            receiveUserIds.add(receiveUserId);
        });
    }

    /**
     * 抢单列表
     *
     * @param data
     * @return
     */
    @Log("抢单列表")
    @PostMapping({"serviceOrder/findAllGrab/search/v2"})
    public Result findGrabAllSort(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        PageRequest pageRequest = initPage(jsonObject);
        String name = jsonObject.get("name").getAsString();
        //查找抢单列表
        int[] statusArray = new int[]{4, 5};
        List<XzqnServicePublish> grabList = publishDao.findAllByStatusInAndIsCancelAndJsonContaining(statusArray, pageRequest, false, name);
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
     * @param publishList
     * @param jsonObject
     */
    private void setGrabList(List<XzqnServicePublish> publishList, JsonObject jsonObject) {
        //设置距离 通过前端传的userId
        Integer receiveUserId = null;
        Integer userId = null;
        try {
            userId = jsonObject.get("userId").getAsInt();
            receiveUserId = userDao.getReceiveUserIdByUserId(userId);
            Optional<ReceiveDataInOrder> dataInOrder = receiveUserDao.findDataInOrder(receiveUserId);
            publishList.forEach(e -> e.setDistance(LocationUtils.getDistance(dataInOrder.get().getLat(), dataInOrder.get().getLng(), e.getLat(), e.getLng())));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            publishList.forEach(e1 -> e1.setDistance(0d));
        }
        //设置factor
        try {
            publishList.forEach(e -> e.setFactorName(factorDao.findById(e.getFactorId()).get().getName()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            publishList.forEach(e1 -> e1.setFactorName("政府"));
        }
        //设置status名描述v2
        List<XzqnServiceOrderStatus> statusList = statusDao.findAll();
        Map<Integer, XzqnServiceOrderStatus> statusMap = new HashMap<>();
        statusList.forEach(e -> statusMap.put(e.getStatus(), e));
        publishList.forEach(publish -> {
            Integer status = publish.getStatus();
            XzqnServiceOrderStatus statusNow = statusMap.get(status);
            publish.setStatusName(statusNow.getReceiveName());
            publish.setStatusDes(statusNow.getReceiveDes());

        });
        //判断师傅是否抢了该单   抢了0  抢成功1   抢失败-1  没抢2
        //userId没传
        buildMultiGrabDes(publishList, userId);


    }

    /**
     * 设置抢单记录
     * 和师傅看未完成,全部订单列表,的保持一致
     * @param publishList
     * @param userId
     */
    private void buildMultiGrabDes(List<XzqnServicePublish> publishList, Integer userId) {
        for (XzqnServicePublish publish : publishList) {
            XzqnGrabRecord grabRecord = grabRecordDao.findByPublishIdAndUserId(publish.getId(), userId);
            int grabStatus;
            //抢单记录为空
            if (grabRecord == null) {
                publish.setStatusName("抢单");
                publish.setStatusDes("未抢单");
                grabStatus=2;//  没抢2 所以查不到记录
            }
//            有记录
            else {
                grabStatus = grabRecord.getGrabStatus();
                if (grabStatus == 0) {
                    publish.setStatusName("等待发单方确认");
                    publish.setStatusDes("已抢单");
                    grabStatus = 0;
                }
                if (grabStatus == 1) {
                    publish.setStatusName("抢单成功");
                    publish.setStatusDes("已抢单");
                    grabStatus=1;
                }
                if (grabStatus == -1) {
                    publish.setStatusName("抢单失败");
                    publish.setStatusDes("已抢单");
                    grabStatus=-1;
                }
            }
            publish.setGrabStatus(grabStatus);
        }
    }
}
