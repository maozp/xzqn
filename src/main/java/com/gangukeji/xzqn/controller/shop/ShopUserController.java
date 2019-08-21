package com.gangukeji.xzqn.controller.shop;

import com.gangukeji.xzqn.dao.ShopUserDao;
import com.gangukeji.xzqn.entity.shop.XzqnShopUser;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.Utils;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: hx
 * @Date: 2019/6/11 19:31
 * @Description:
 */
@RestController
@RequestMapping("shop/user")
public class ShopUserController {
    @Resource
    ShopUserDao shopUserDao;
    /*
    * 用户详情
    * */
    @PostMapping({"findById","findByUserId"})
    private Result findById(@RequestBody String data) {
        Integer userId = new JsonParser().parse(data).getAsJsonObject().get("userId").getAsInt();
        XzqnShopUser user;
        try {
            user = shopUserDao.findById(userId).get();
        } catch (Exception e) {
            return ResultUtils.error(-1, "not exist");
        }
        return ResultUtils.success(200, "user", user);
    }
    /*
    * 更新用户
    * */
    @PostMapping({"update"})
    private Result update(@RequestBody XzqnShopUser user) {
        XzqnShopUser save = null;
        try {
            save = shopUserDao.findById(user.getUserId()).get();
        } catch (Exception e) {
            return ResultUtils.error(-1,"不存在无法更新");
        }
        Utils.copyPropertiesIgnoreNull(user, save);
        save = shopUserDao.save(save);
        return ResultUtils.success(200, "update User成功", save);
    }
}
