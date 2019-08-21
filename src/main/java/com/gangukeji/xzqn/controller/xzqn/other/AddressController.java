package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.ServiceAddrDao;
import com.gangukeji.xzqn.dao.UserDao;
import com.gangukeji.xzqn.entity.XzqnServiceAddrs;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: hx
 * @Date: 2019/6/22 15:36
 * @Description:
 */
@RequestMapping
@RestController
public class AddressController {
    Gson gson = new Gson();
    @Resource
    UserDao userDao;
    @Resource
    ServiceAddrDao addrDao;

    @PostMapping({"sendUserAddr/findById"})
    @Log("地址--详情")
    public Result findAddr(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int addrId = jsonObject.get("addrId").getAsInt();
        XzqnServiceAddrs address = addrDao.findById(addrId).get();
        return ResultUtils.success(200, "获取发单用户地址成功", address);
    }

    private Integer getSendUserId(Integer userId) {
        return userDao.findById(userId).get().getSendUserId();
    }

    @PostMapping({"sendUserAddr/add"})
    @Log("地址--添加")
    public Result addSendAddr(@RequestBody String data) {
        XzqnServiceAddrs address = gson.fromJson(data, XzqnServiceAddrs.class);
        address.setSendUserId(getSendUserId(address.getUserId()));
        Integer addrId = addrDao.save(address).getId();
        HashMap<Object, Object> map = new HashMap<>();
        map.put("addrId", addrId);
        return ResultUtils.success(200, "新增发单用户地址成功", map);
    }

    @PostMapping({"sendUserAddr/update"})
    @Log("地址--编辑")
    public Result updateSendAddr(@RequestBody String data) {
        XzqnServiceAddrs address = gson.fromJson(data, XzqnServiceAddrs.class);
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int addrId = jsonObject.get("addrId").getAsInt();
        XzqnServiceAddrs save = addrDao.findById(addrId).get();
        //防止覆盖原有userId
        address.setUserId(null);
        address.setSendUserId(null);
        Utils.copyPropertiesIgnoreNull(address, save);
        addrDao.save(save);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("addrId", addrId);
        return ResultUtils.success(200, "更新发单用户地址成功", map);
    }

    @PostMapping({"sendUserAddr/delete"})
    @Log("地址--删除")
    public Result deleteSendAddr(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int addrId = jsonObject.get("addrId").getAsInt();
        addrDao.deleteById(addrId);
        return ResultUtils.success(-1, "地址删除成功", "addrId:" + addrId);
    }

    @PostMapping({"sendUserAddr/findAll"})
    @Log("地址--全部")
    public Result findSendAddr(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        Integer userId = jsonObject.get("userId").getAsInt();
        List<XzqnServiceAddrs> resps = addrDao.findAllByUserId(userId);
        resps = resps.stream().sorted(Comparator.comparing(XzqnServiceAddrs::getUpdateTime).reversed()).collect(Collectors.toList());
        return ResultUtils.success(200, "获取发单用户地址成功", resps);
    }
}
