package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.GrabRecordDao;
import com.gangukeji.xzqn.dao.ServiceOrderDao;
import com.gangukeji.xzqn.dao.ServicePublishDao;
import com.gangukeji.xzqn.entity.XzqnGrabRecord;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @Author: hx
 * @Date: 2019/6/19 14:23
 * @Description:
 */
@RestController
public class TimeController {
    @Resource
    ServicePublishDao publishDao;
    @Resource
    ServiceOrderDao orderDao;
    Gson gson = new Gson();
    @Autowired
    GrabRecordDao grabRecordDao;

    /**
     * @param data
     * @return 下单到现在过的时间
     */
    @PostMapping("clock/get")
    @Log
    public Result clockGet(@RequestBody String data) {
        Map map = gson.fromJson(data, Map.class);
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        Long pass;
        try {
            int publishId = jsonObject.get("publishId").getAsInt();
            pass = (System.currentTimeMillis() - publishDao.getCreateTimeById(publishId).getTime()) / 1000;
        } catch (Exception e) {
            try {
                int orderId = jsonObject.get("orderId").getAsInt();
                Date time = orderDao.getCreateTimeById(orderId);
                pass = (System.currentTimeMillis() - time.getTime()) / 1000;
            } catch (Exception ex) {
                System.out.println(e.getMessage());
                return ResultUtils.error(-1, "给个正确的publishId或者orderId我才能帮你算时间" + ex.getMessage());
            }
        }
        return ResultUtils.success(200, "获得计时器", pass);
    }

    /**
     * 师傅抢单过了多长时间
     * @param data
     * @return
     */
    @PostMapping("clock/get/grabTime")
    @Log
    public Result grabTime(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int userId = jsonObject.get("userId").getAsInt();
        int publishId = jsonObject.get("publishId").getAsInt();
        XzqnGrabRecord grabRecord = grabRecordDao.findByPublishIdAndUserId(publishId, userId);
        Date time = grabRecord.getCreateTime();
        return ResultUtils.success(200, "grabTime",(System.currentTimeMillis() - time.getTime()) / 1000 );
    }
}
