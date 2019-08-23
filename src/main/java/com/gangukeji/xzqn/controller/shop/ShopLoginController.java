package com.gangukeji.xzqn.controller.shop;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.ShopUserDao;
import com.gangukeji.xzqn.entity.shop.XzqnShopUser;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.pay.WXPayConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author: hx
 * @Date: 2019/6/12 11:12
 * @Description: 商城登录
 */
@Slf4j
@RestController
@RequestMapping("shop")
public class ShopLoginController {

    private final static String appid2 = WXPayConfig.appid2;
    private final static String secret2 = WXPayConfig.secret2;


    @Resource
    ShopUserDao shopUserDao;

    /**
     * 通过code请求微信服务器获取openid
     * @param code
     * @return
     * @throws Exception
     */
    public String getOpenid(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        return new JsonParser().parse(Objects.requireNonNull(restTemplate.getForEntity("https://api.weixin.qq.com/sns/jscode2session?appid=" + appid2 + "&secret=" + secret2 + "&js_code=" + code + "&grant_type=authorization_code", String.class).getBody())).getAsJsonObject().get("openid").getAsString();
    }

    /**
     * 登录返回userId
     * @param data
     * @return
     */
    @PostMapping("/login")
    @Log("shop登录")
    public Result login(@RequestBody String data){
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        XzqnShopUser user;
        String openid = null;
        try {
            String code = jsonObject.get("code").getAsString();
            openid = getOpenid(code);
            user = shopUserDao.findByOpenid(openid);
            //新用户首次登录
            if (user == null) {
                return ResultUtils.success(200, "数据库没有,创建新用户", initUser(openid, null, null));
            }
            return ResultUtils.success(200, "登录成功", user);
        } catch (Exception e) {
            System.out.println(e + "3eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            return ResultUtils.error(-1, "code异常或数据库异常" + e.getMessage());
        }
    }

    /**
     * 设置新用户基本信息
     * @param openid
     * @param username
     * @param password
     * @return
     */
    private XzqnShopUser initUser(String openid, String username, String password) {
        //设置基本用户参数
        XzqnShopUser user = new XzqnShopUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setOpenid(openid);
        user.setName("姓名未设置");
        user.setNickname("昵称未设置");
        user.setPhone("19900000000");
        user.setMark("我的空间我做主");
        user.setHeadImg("https://www.xiaozheng8.com/file/1560393110298318175.JPEG");
        //保存
        return shopUserDao.save(user);
    }
}
