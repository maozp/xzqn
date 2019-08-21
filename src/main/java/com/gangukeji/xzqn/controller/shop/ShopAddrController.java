package com.gangukeji.xzqn.controller.shop;

import com.gangukeji.xzqn.dao.ShopAddrDao;
import com.gangukeji.xzqn.entity.shop.XzqnShopAddr;
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
 * @Date: 2019/6/11 19:30
 * @Description: 商城地址的增删改查
 */
@RestController
@RequestMapping("addr")
public class ShopAddrController {
    @Resource
    ShopAddrDao addrDao;

    //    @Log("地址--全部")
    @PostMapping({"findAll", "findByUserId"})
    public Result findByUserId(@RequestBody String data) {
        Integer userId = null;
        try {
            JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
            userId = jsonObject.get("userId").getAsInt();
        } catch (Exception e) {
            return ResultUtils.error(-1, "userId错误");
        }
        List<XzqnShopAddr> resps = addrDao.findAllByUserId(userId);
        //排序根据创建日期
        resps = resps.stream().sorted(Comparator.comparing(XzqnShopAddr::getCreateTime).reversed()).collect(Collectors.toList());
        return ResultUtils.success(200, "获取用户地址成功", resps);
    }

    @PostMapping({"findById","findByAddrId"})
//    @Log("地址--详情")
    public Result findAddr(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        XzqnShopAddr addr = null;
        try {
            Integer addrId = jsonObject.get("addrId").getAsInt();
            addr = addrDao.findById(addrId).get();
        } catch (Exception e) {
            return ResultUtils.error(-1, "地址异常");
        }
        return ResultUtils.success(200, "获取用户地址成功", addr);
    }
    @PostMapping({"add"})
//    @Log("地址--添加")
    public Result addrAdd(@RequestBody String data) {
        XzqnShopAddr post = new Gson().fromJson(data, XzqnShopAddr.class);
//        空处理
        if (post.getUserId() == null) {
            return ResultUtils.error(-1, "userId为null");
        }
        XzqnShopAddr save = addrDao.save(post);
        return ResultUtils.success(200, "新增用户地址成功", save);
    }
    @PostMapping({"update"})
//    @Log("地址--更新")
    public Result addrUpdate(@RequestBody String data) {
        XzqnShopAddr post = new Gson().fromJson(data, XzqnShopAddr.class);
        XzqnShopAddr addr = null;
        try {
            addr = addrDao.findById(post.getAddrId()).get();
        } catch (Exception e) {
            return ResultUtils.error(-1, "addrId不存在");
        }
        Utils.copyPropertiesIgnoreNull(post, addr);
        XzqnShopAddr save = addrDao.save(addr);
        return ResultUtils.success(200, "更新用户地址成功", save);
    }
    @PostMapping({"delete","deleteById","deleteByAddrId"})
//    @Log("地址--删除")
    public Result deleteAddr(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        Integer addrId = null;
        try {
            addrId = jsonObject.get("addrId").getAsInt();
            addrDao.deleteById(addrId);
        } catch (Exception e) {
            return ResultUtils.error(-1, "地址删除异常");
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("addrId", addrId);
        return ResultUtils.success(200, "删除发单用户地址成功", map);
    }
}
