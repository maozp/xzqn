package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.dao.ReceiveUserDao;
import com.gangukeji.xzqn.dao.UserDao;
import com.gangukeji.xzqn.entity.XzqnUserReceive;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: hx
 * @Date: 2019/7/30 11:42
 * @Description:
 */
@RestController
public class LatLngController {
    @Autowired
    ReceiveUserDao receiveUserDao;
    @Autowired
    UserDao userDao;

    @RequestMapping("location/findByUserId")
    public Result findLocation(@RequestBody Map<String, Object> map) {
        Integer userId = (Integer) map.get("userId");
        Integer receiveUserId = userDao.getReceiveUserIdByUserId(userId);
        List<List<String>> location = receiveUserDao.getLocation(receiveUserId);
        map.put("lat", location.get(0).get(0));
        map.put("lng", location.get(0).get(1));
        return ResultUtils.success(200, "lat ,lng find", map);
    }

    @RequestMapping("location/findByReceiveUserId")
    public Result findLocationByReceiveUserId(@RequestBody Map<String, Object> map) {
        Integer receiveUserId = (Integer) map.get("receiveUserId");
        List<List<String>> location = receiveUserDao.getLocation(receiveUserId);
        map.put("lat", location.get(0).get(0));
        map.put("lng", location.get(0).get(1));
        return ResultUtils.success(200, "lat ,lng find", map);
    }

    @RequestMapping("location/updateByUserId")
    public Result updateByUserId(@RequestBody XzqnUserReceive userReceive) {
        Integer userId = userReceive.getUserId();
        BigDecimal lat = userReceive.getLat();
        BigDecimal lng = userReceive.getLng();
        int count = receiveUserDao.updateLatLng(lat, lng, userDao.getReceiveUserIdByUserId(userId));
        return ResultUtils.success(200, "更新成功data为更新了几行数据", count);
    }

    @RequestMapping("location/updateByReceiveUserId")
    public Result updateByReceiveUserId(@RequestBody XzqnUserReceive userReceive) {
        Integer receiveUserId = userReceive.getId();
        BigDecimal lat = userReceive.getLat();
        BigDecimal lng = userReceive.getLng();
        int count = receiveUserDao.updateLatLng(lat, lng, receiveUserId);
        return ResultUtils.success(200, "更新成功data为更新了几行数据", count);
    }
}
