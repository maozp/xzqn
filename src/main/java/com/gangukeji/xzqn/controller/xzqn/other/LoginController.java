package com.gangukeji.xzqn.controller.xzqn.other;

/**
 * @Author: hx
 * @Date: 2019/5/14 9:26
 * @Description: 登录, 注册, 生成token, 验证token
 */
/*

<xml>

<appid>wxd930ea5d5a258f4f</appid>

<mch_id>1518288801</mch_id>

<device_info>1000</device_info>

<body>test</body>

<nonce_str>ibuaiVcKdpRxkhJA</nonce_str>

<sign>9A0A8659F005D6984697E2CA0A9CF3B7</sign>

</xml>
*/

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.*;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.pay.WXPayConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@RestController
@RequestMapping
public class LoginController {
    //    @RefreshScope
    @Value("(${spring.application.name})")
    private String name;

    @RequestMapping("/")
    public String test() {
        return name;
    }

    @Resource
    UserDao userDao;
    @Resource
    UserReceiveDao userReceiveDao;
    @Resource
    UserSendDao userSendDao;

    private final static String appid = WXPayConfig.appid;
    //appsecret
    private final static String secret = WXPayConfig.secret;
    private final static String mchId = WXPayConfig.mchId;
    private final static String key = WXPayConfig.key;
    private final static String notifyUrl = WXPayConfig.notifyUrl;
//    private final static String spbillCreateIp = "47.111.171.242";

    public String getOpenid(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        return new JsonParser().parse(Objects.requireNonNull(restTemplate.getForEntity("https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code", String.class).getBody())).getAsJsonObject().get("openid").getAsString();
    }

    @PostMapping("app/user/login")
    @Log
    public Result loginApp(@RequestBody String data) {
        //将Json字符串转换成JsonObject对象：
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        Integer userId;
        try {
            String username = jsonObject.get("username").getAsString();
            String password = jsonObject.get("password").getAsString();
            userId = userDao.findByUsernameAndPassword(username, password).getId();
        } catch (Exception e) {
            System.out.printf("25eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee登录异常|账号密码错误" + e.getMessage());
            return ResultUtils.error(-1, "登录异常|账号密码错误");
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("userId", userId);
        return ResultUtils.success(200, "登录成功", map);
    }

    @PostMapping("app/user/register")
    @Log
    public Result registerApp(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        Integer userId;
        try {
            String username = jsonObject.get("username").getAsString();
            //判断账号是否已存在
            XzqnUser user = userDao.findByUsername(username);
            if (user != null) {
                return ResultUtils.error(-1, "账号已存在");
            }
            String password = jsonObject.get("password").getAsString();
            userId = initUser("", username, password).getId();
        } catch (Exception e) {
            System.out.printf("25eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee注册异常" + e.getMessage());
            return ResultUtils.error(-1, "注册异常");
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("userId", userId);
        return ResultUtils.success(200, "注册成功", map);
    }

    /**
     * 登录 2
     */
    @PostMapping("user/login")
    public Result login2(@RequestBody String data) {
        //将Json字符串转换成JsonObject对象：
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        XzqnUser user;
        String openid = null;
        try {
            String code = jsonObject.get("code").getAsString();
            openid = getOpenid(code);
            user = userDao.findByOpenid(openid);
            if (user == null) {
                return ResultUtils.success(200, "数据库没有,创建新用户", initUser(openid, null, null));
            }
            return ResultUtils.success(200, "登录成功", user);
        } catch (Exception e) {
            System.out.println(e + "3eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            return ResultUtils.error(-1, "code异常或数据库异常" + e.getMessage());
        }
    }

    private XzqnUser initUser(String openid, String username, String password) {
        //设置 发单 接单 用户参数
        XzqnUserReceive receive = new XzqnUserReceive();
        XzqnUserSend send = new XzqnUserSend();
        receive.setIsAuth(false);
        receive.setIsGuarantee(false);
        receive.setIsBan(false);
        receive.setAuthType("未认证");
        receive.setCompanyName("赣谷科技");
        receive.setNickname("小正青年");
        receive.setName("小正青年");
        receive.setIntroduceIds("3,5");
        receive.setIntroduce("视频监控@");
        receive.setAddrDes("南昌");
        receive.setArea("南昌");
        receive.setAddrName("南昌");
        receive.setLocation("南昌");
        receive.setEnterArea("南昌");
        receive.setMark("小正青年大有可为");
        receive.setDes("新手上路");
        receive.setGoodAt("擅长维修各种监控设备");
        receive.setPromise("售后30天保修");
        receive.setHeadImg("https://www.xiaozheng8.com/file/1559396271977395978.png");
        receive.setPhone("156" + RandomUtils.nextInt(10000000, 99999999));
        receive.setLat(new BigDecimal(29.545380));
        receive.setLng(new BigDecimal(115.944220));
        receive.setTurnover90(new BigDecimal(0));
        receive.setServeTime(new BigDecimal(0));
        receive.setBackPercentage(new BigDecimal(0));
        receive.setStarts(0);
        receive.setBadCount(0);
        receive.setGoodCount(0);
        receive.setComplaintCount(0);
        receive.setServeCount(0);
        receive.setRate(0);

        send.setIsVip(false);
        send.setMark("小正青年大有可为");
        send.setNickname("小正青年");
        send.setName("小正青年");
        send.setPhone("156" + RandomUtils.nextInt(10000000, 99999999));
        send.setCompanyName("赣谷科技");
        send.setCompanyAddr("金沙大道");
        send.setDepartmentName("人力资源部");
        send.setIsCheck(0);
        send.setHeadImg("https://www.xiaozheng8.com/file/1559617531395582138.JPEG");
        //保存
        //设置基本用户参数
        XzqnUser user = new XzqnUser();
        user.setReceiveUserId(userReceiveDao.save(receive).getId());
        user.setSendUserId(userSendDao.save(send).getId());
        user.setUsername(username);
        user.setPassword(password);
        user.setOpenid(openid);
        //保存
        return userDao.save(user);
    }

    /**
     * 登录 3
     */
    @PostMapping("user/login2")
    public Result login3(@RequestBody String data) {
        //将Json字符串转换成JsonObject对象：
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        XzqnUser user;
        String openid = null;
        try {
            String code = jsonObject.get("code").getAsString();
            openid = getOpenid(code);
            user = userDao.findByOpenid(openid);
            if (user == null) {
                return ResultUtils.success(200, "数据库没有,创建新用户", initUser2(openid, null, null));
            }
            return ResultUtils.success(200, "登录成功", user);
        } catch (Exception e) {
            System.out.println(e + "3eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            return ResultUtils.error(-1, "code异常或数据库异常" + e.getMessage());
        }
    }

    private XzqnUser initUser2(String openid, String username, String password) {
        //设置 发单 接单 用户参数
        XzqnUserReceive receive = new XzqnUserReceive();
        XzqnUserSend send = new XzqnUserSend();
        receive.setIsAuth(false);
        receive.setIsGuarantee(false);
        receive.setIsBan(false);
        receive.setAuthType("未认证");
        receive.setCompanyName("赣谷科技");
        receive.setNickname("小正青年");
        receive.setName("小正青年");
        receive.setIntroduceIds("3,5");
        receive.setIntroduce("视频监控@");
        receive.setAddrDes("南昌");
        receive.setArea("南昌");
        receive.setAddrName("南昌");
        receive.setLocation("南昌");
        receive.setEnterArea("南昌");
        receive.setMark("小正青年大有可为");
        receive.setDes("新手上路");
        receive.setGoodAt("擅长维修各种监控设备");
        receive.setPromise("售后30天保修");
        receive.setHeadImg("https://www.xiaozheng8.com/file/1559396271977395978.png");
        receive.setPhone("156" + RandomUtils.nextInt(10000000, 99999999));
        receive.setLat(new BigDecimal(29.545380));
        receive.setLng(new BigDecimal(115.944220));
        receive.setTurnover90(new BigDecimal(0));
        receive.setServeTime(new BigDecimal(0));
        receive.setBackPercentage(new BigDecimal(0));
        receive.setStarts(0);
        receive.setBadCount(0);
        receive.setGoodCount(0);
        receive.setComplaintCount(0);
        receive.setServeCount(0);
        receive.setRate(0);

        send.setIsVip(false);
        send.setMark("小正青年大有可为");
        send.setNickname("小正青年");
        send.setName("小正青年");
        send.setPhone("156" + RandomUtils.nextInt(10000000, 99999999));
        send.setCompanyName("赣谷科技");
        send.setCompanyAddr("金沙大道");
        send.setDepartmentName("人力资源部");
        send.setIsCheck(0);
        send.setHeadImg("https://www.xiaozheng8.com/file/1559617531395582138.JPEG");
        //保存
        //设置基本用户参数
        XzqnUser user = new XzqnUser();
        user.setReceiveUserId(userReceiveDao.save(receive).getId());
        user.setSendUserId(userSendDao.save(send).getId());
        user.setUsername(username);
        user.setPassword(password);
        user.setOpenid(openid);
        //保存
        return userDao.save(user);
    }


}
