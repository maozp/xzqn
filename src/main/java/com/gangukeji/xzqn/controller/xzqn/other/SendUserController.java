package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.dao.SendUserDao;
import com.gangukeji.xzqn.dao.UserDao;
import com.gangukeji.xzqn.entity.XzqnUserSend;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: hx
 * @Date: 2019/7/27 13:27
 * @Description: 更新 查询 发单用户
 */
@RequestMapping
@RestController
@CrossOrigin("*")
public class SendUserController {
    @Autowired
    SendUserDao sendUserDao;
    @Autowired
    UserDao userDao;
    @Autowired
    WebSocket webSocket;

    @RequestMapping("auth/send/add")
    public Result addSendAuth(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        XzqnUserSend userSend = new Gson().fromJson(data, XzqnUserSend.class);
        Integer userId = jsonObject.get("userId").getAsInt();
        Integer sendUserId = userDao.getSendUserIdByUserId(userId);
        XzqnUserSend save = sendUserDao.findById(sendUserId).get();
        Utils.copyPropertiesIgnoreNull(userSend, save);
        save = sendUserDao.save(save);


        //发单方来了.mp3
        webSocket.sendMessage("有新的发单用户需认证");

        return ResultUtils.success(200, "更新发单用户认证成功", save);
    }

//    @RequestMapping("auth/send/addBySend")
//    public Result addSendAuthBySendUserId(@RequestBody Map<String, Object> map) {
//        Integer sendUserId = (Integer) map.get("sendUserId");
//        int count = sendUserDao.updateUserInfo((String) map.get("name"), (String) map.get("phone"), (String) map.get("companyName"), (String) map.get("companyPhone"), sendUserId);
//        return ResultUtils.success(200, "更新发单用户认证成功", count);
//    }

    @RequestMapping("user/send/findById")
    public Result findById(@RequestBody Map<String, Object> map) {
        int userId = (int) map.get("userId");
        Integer sendUserId = userDao.getSendUserIdByUserId(userId);
        return ResultUtils.success(200, "查询发单用户成功", sendUserDao.findById(sendUserId));
    }

    @RequestMapping("user/send/findBySendUserId")
    public Result findBySendUserId(@RequestBody Map<String, Object> map) {
        int sendUserId = (int) map.get("sendUserId");
        return ResultUtils.success(200, "查询发单用户成功", sendUserDao.findById(sendUserId));
    }
}
