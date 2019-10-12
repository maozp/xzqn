package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.FollowDao;
import com.gangukeji.xzqn.dao.ReceiveUserDao;
import com.gangukeji.xzqn.dao.UserDao;
import com.gangukeji.xzqn.entity.XzqnFollow;
import com.gangukeji.xzqn.entity.XzqnUser;
import com.gangukeji.xzqn.entity.XzqnUserReceive;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/22 15:49
 * @Description:
 */
@RestController
@RequestMapping
public class FollowController {
    @Autowired
    FollowDao followDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ReceiveUserDao receiveUserDao;

    /**
     * 关注/取关师傅
     */
    @PostMapping("user/star")
    @Log
    public Result star(@RequestBody String data) {
        XzqnFollow follow = new XzqnFollow();
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        Integer receiveUserId = jsonObject.get("receiveUserId").getAsInt();

        Integer userIdFl = userDao.getUserIdByReceiveUserId(receiveUserId);
        Integer userId = jsonObject.get("userId").getAsInt();
        boolean  star = jsonObject.get("star").getAsBoolean();
        boolean exists=followDao.existsByUserIdAndUserIdFl(userId,userIdFl);
        if (exists) {
            followDao.updateStatus(star, userId, userIdFl);
        } else {
            follow = new XzqnFollow(userId, userIdFl, star, null, new Date(), new Date());
            followDao.save(follow);
        }
        return ResultUtils.success(200, "star=" + star, follow);

    }


    @Log("查找已关注的师傅")
    @PostMapping("receiveUser/findByStar")
    public Result findStar(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int userId = jsonObject.get("userId").getAsInt();
        ArrayList<Integer> ids = new ArrayList<>();
        List<XzqnFollow> followList = followDao.findByUserIdAndStatus(userId, true);
        followList.forEach(e -> ids.add(e.getUserIdFl()));
        //返回 已关注的师傅
        List<XzqnUser> userList = userDao.findAllById(ids);
        ids.clear();
        userList.forEach(e -> ids.add(e.getReceiveUserId()));
        List<XzqnUserReceive> userReceives = receiveUserDao.findAllById(ids);
        return ResultUtils.success(200, "已关注的师傅列表", userReceives);
    }
    @Log("查找我关注的师傅")
    @PostMapping("user/isStar")
    public Result findIsStar(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int userId = jsonObject.get("userId").getAsInt();
        int receiveUserId = jsonObject.get("receiveUserId").getAsInt();
        Integer userIdFl = userDao.getUserIdByReceiveUserId(receiveUserId);
        boolean isStar= false;
        try {
            isStar = followDao.isStarUser(userId, userIdFl);
        } catch (Exception e) {
            System.out.println("未找到");
        }
        return ResultUtils.success(200,"查询是否关注成功",isStar);
    }

}
