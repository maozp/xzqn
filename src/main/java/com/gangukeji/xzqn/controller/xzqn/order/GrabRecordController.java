package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnGrabRecord;
import com.gangukeji.xzqn.utils.ListToString;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.StringToListUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/7/1 15:53
 * @Description:
 */
@RestController
@Slf4j
public class GrabRecordController {
    @Autowired
    GrabRecordDao grabRecordDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ServicePublishDao publishDao;
    @Autowired
    ReceiveUserDao receiveUserDao;
    @Autowired
    FollowDao followDao;

    /**
     * 发单方拒绝师傅,确认师傅在setStatus里面
     *
     * @param data
     * @return
     */
    @Log
    @PostMapping({"serviceOrder/grabRecord/update"})
    public Result grabRecordUpdate(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int publishId = jsonObject.get("publishId").getAsInt();
        int receiveUserId = jsonObject.get("receiveUserId").getAsInt();
        Integer userId = userDao.getUserIdByReceiveUserId(receiveUserId);
        grabRecordDao.updateGrabStatusByPublishIdWhereUserIdIs(publishId, userId, -1);

        //publish更新,删师傅的id
        removeReceiveUser(publishId, receiveUserId);
        HashMap<String, Integer> map = new HashMap<>();
        map.put("publishId", publishId);
        map.put("receiveUserId", receiveUserId);
        return ResultUtils.success(200, "您拒绝了一位师傅", map);
    }

    /**
     * 师傅放弃抢单
     *
     * @param data
     * @return
     */
    @Log
    @PostMapping({"serviceOrder/grabRecord/giveUp"})
    public Result giveUp(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int publishId = jsonObject.get("publishId").getAsInt();
        int userId = jsonObject.get("userId").getAsInt();
        //删除记录
        grabRecordDao.deleteByPublishIdAndUserId(publishId, userId);
        //publish更新,删师傅的id
        int receiveUserId = userDao.getReceiveUserIdByUserId(userId);
        removeReceiveUser(publishId, receiveUserId);
        HashMap<String, Integer> map = new HashMap<>();
        map.put("publishId", publishId);
        map.put("userId", userId);
        return ResultUtils.success(200, "师傅放弃抢单", map);
    }

    private void removeReceiveUser(int publishId, int receiveUserId) {
        String receiveUserIdString = publishDao.getReceiveUserIdList(publishId);
        List<Integer> receiveUserIdList = StringToListUtil.go(receiveUserIdString, ",");
        receiveUserIdList.removeIf(e -> e == receiveUserId);
        receiveUserIdString = ListToString.goInt(receiveUserIdList, ",");
        publishDao.updateReceiveUserIdListAllByPublishId(receiveUserIdString, publishId);
    }

    /**
     * 根据publishId查找抢单抢单记录
     *
     * @param data
     * @return 师傅列表和抢单记录
     */
    @Log
    @PostMapping({"serviceOrder/grabRecord/find"})
    public Result grabRecordFind(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int publishId = jsonObject.get("publishId").getAsInt();
        List<XzqnGrabRecord> grabRecordList = grabRecordDao.findByPublishIdOrderByIdDesc(publishId);
        grabRecordList.forEach(e -> {
            Integer receiveUserId = userDao.getReceiveUserIdByUserId(e.getUserId());
            e.setUserReceive(receiveUserDao.findById(receiveUserId).get());
        });
        //移除被拒绝的记录不显示在订单详情确认的师傅界面
        grabRecordList.removeIf(e -> e.getGrabStatus() == -1);
        return ResultUtils.success(200, "抢单记录0默认,-1拒绝1同意该师傅", grabRecordList);
    }

    @Log
    @PostMapping({"serviceOrder/grabRecord/find/v2"})
    public Result grabRecordFindV2(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int publishId = jsonObject.get("publishId").getAsInt();
        List<XzqnGrabRecord> grabRecordList = grabRecordDao.findByPublishIdOrderByIdDesc(publishId);
        grabRecordList.forEach(e -> {
            Integer receiveUserId = userDao.getReceiveUserIdByUserId(e.getUserId());
            e.setUserReceive(receiveUserDao.findById(receiveUserId).get());
            e.setStar(getStart(e.getUserId(), receiveUserId));
        });
        //移除被拒绝的记录不显示在订单详情确认的师傅界面
        grabRecordList.removeIf(e -> e.getGrabStatus() == -1);
        return ResultUtils.success(200, "抢单记录0默认,-1拒绝1同意该师傅", grabRecordList);
    }

//    private boolean getStart(Integer userId, Integer receiveUserId) {
//        Integer userIdFl = userDao.getUserIdByReceiveUserId(receiveUserId);
//        Boolean star;
//        try {
//            star = followDao.isStarUser(userId, userIdFl);
//        } catch (Exception e) {
//            star = false;
//        }
//        return star;
//    }
    private boolean getStart(Integer userId, Integer receiveUserId) {
        Integer userIdFl = userDao.getUserIdByReceiveUserId(receiveUserId);
        Boolean star;
        try {
            star = followDao.isStarUser(userId, userIdFl);
        } catch (Exception e) {
            star = false;
        }
        if (star == null) {
            return false;
        }
        return star;
    }

    /**
     * 啥时候抢的单
     *
     * @param data publishId userId
     * @return passTime
     */
    @Log
    @PostMapping({"serviceOrder/grabRecord/passTime"})
    public Result getGrabTime(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int publishId = jsonObject.get("publishId").getAsInt();
        int userId = jsonObject.get("userId").getAsInt();
        XzqnGrabRecord grabRecord = grabRecordDao.findByPublishIdAndUserId(publishId, userId);
        long passTime = (grabRecord.getCreateTime().getTime() - new Date().getTime()) / 1000;
        return ResultUtils.success(200, "passTime", passTime);
    }
}
