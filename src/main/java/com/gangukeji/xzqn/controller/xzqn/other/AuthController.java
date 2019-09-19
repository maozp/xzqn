package com.gangukeji.xzqn.controller.xzqn.other;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.*;
import com.gangukeji.xzqn.entity.view.AuthView;
import com.gangukeji.xzqn.utils.AuthUtil;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @Author: hx
 * @Date: 2019/6/22 15:36
 * @Description:
 */
@RestController
@RequestMapping
public class AuthController {
    Gson gson = new Gson();
    @Resource
    ReceiveUserDao receiveUserDao;
    @Resource
    AuthPersonDao authPersonDao;
    @Resource
    AuthSkillDao authSDao;
    @Resource
    UserDao userDao;
    @Autowired
    AuthContentDao authContentDao;
    @Autowired
    WebSocket webSocket;

    @PostMapping("auth/content")
    @Log("添加服务认证")
    public Result authContent(@RequestBody XzqnAuthContent authContent) {
        Integer userId = authContent.getUserId();
        Integer receiveUserId = userDao.getReceiveUserIdByUserId(userId);
        authContent.setReceiveUserId(receiveUserId);
        boolean exists = authContentDao.existsByUserId(userId);

        if (exists) {
            authContentDao.deleteByUserId(userId);
        }
        authContent.setIsCheck(1);
        //设置introduce
        List<String> introduceList = authContent.getIntroduceList();
        if (introduceList == null) {
            introduceList = new ArrayList<>();
        }
        StringBuilder introduce = new StringBuilder();
        for (String e : introduceList) {
            introduce.append(e + "@");
        }

        authContent.setIntroduce(introduce.toString());
        authContent.setCreateTime(null);
        authContent.setUpdateTime(null);
        authContent = authContentDao.save(authContent);
        authContent.setIntroduceList(introduceList);
        updateReceiveUserByAuthContent(authContent);

        //有新的服务认证消息
        webSocket.sendMessage("有新的服务认证消息");

        return ResultUtils.success(200, "服务认证成功", authContent);
    }

    private void updateReceiveUserByAuthContent(XzqnAuthContent authContent) {
        Integer receiveUserId = userDao.getReceiveUserIdByUserId(authContent.getUserId());
        XzqnUserReceive userReceive = receiveUserDao.findById(receiveUserId).get();
        if (userReceive.getAuthStatus() == null) {
            userReceive.setAuthStatus("000");
        }
        StringBuilder builder = new StringBuilder(userReceive.getAuthStatus());
        userReceive.setAuthStatus(builder.replace(0, 1, "1").toString());
//        userReceive.setAuthUserId(authContent.getId());

        userReceive.setPromise(authContent.getPromise());
        userReceive.setArea(authContent.getArea());
        userReceive.setDes(authContent.getDes());
        userReceive.setIntroduce(authContent.getIntroduce());
        receiveUserDao.save(userReceive);
    }

    @PostMapping("auth/person")
    @Log("添加个人认证")
    public Result authP(@RequestBody String data) {
        XzqnAuthPerson authPerson = gson.fromJson(data, XzqnAuthPerson.class);
        Integer userId = authPerson.getUserId();
        Integer receiveUserId = userDao.getReceiveUserIdByUserId(userId);
        authPerson.setReceiveUserId(receiveUserId);
        boolean exists = authPersonDao.existsByUserId(userId);
        if (exists) {
            authPersonDao.deleteByUserId(userId);
        }
        authPerson.setIsCheck(1);
        Integer authId = authPersonDao.save(authPerson).getId();
        //设置 receive authUserId
        XzqnUserReceive userReceive = receiveUserDao.findById(receiveUserId).get();
        userReceive.setAuthUserId(authId);
        userReceive.setPhone(authPerson.getPhone());
        userReceive.setName(authPerson.getIdCardName());
        userReceive.setHeadImg(authPerson.getPersonalImg());

        if (userReceive.getAuthType().equals("未认证")) {
            userReceive.setAuthType("个人");
        }

        StringBuilder builder = new StringBuilder(userReceive.getAuthStatus());
        userReceive.setAuthStatus(builder.replace(1, 2, "1").toString());
        receiveUserDao.save(userReceive);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("authId", authId);
        //接单方来了
        webSocket.sendMessage("有新的接单用户需认证");
        return ResultUtils.success(200, "个人认证成功", map);
    }
    @PostMapping("auth/person/v2")
    @Log("添加个人认证")
    public Result authPV2(@RequestBody String data) throws Exception{
        XzqnAuthPerson authPerson = gson.fromJson(data, XzqnAuthPerson.class);
        boolean illegal=idCardAuth(authPerson);
        if (!illegal) {
            return ResultUtils.error(-1,"认证失败输入的身份证号码与名字不合法");
        }
        Integer userId = authPerson.getUserId();
        Integer receiveUserId = userDao.getReceiveUserIdByUserId(userId);
        authPerson.setReceiveUserId(receiveUserId);
        boolean exists = authPersonDao.existsByUserId(userId);
        if (exists) {
            authPersonDao.deleteByUserId(userId);
        }
        authPerson.setIsCheck(1);
        Integer authId = authPersonDao.save(authPerson).getId();
        //设置 receive authUserId
        XzqnUserReceive userReceive = receiveUserDao.findById(receiveUserId).get();
        userReceive.setAuthUserId(authId);
        userReceive.setPhone(authPerson.getPhone());
        userReceive.setName(authPerson.getIdCardName());
        userReceive.setHeadImg(authPerson.getPersonalImg());

        if (userReceive.getAuthType().equals("未认证")) {
            userReceive.setAuthType("个人");
        }
        StringBuilder builder = new StringBuilder(userReceive.getAuthStatus());
        userReceive.setAuthStatus(builder.replace(1, 2, "1").toString());
        receiveUserDao.save(userReceive);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("authId", authId);

        //接单方来了
        webSocket.sendMessage("有新的接单用户需认证");

        return ResultUtils.success(200, "个人认证成功", map);
    }

    private boolean idCardAuth(XzqnAuthPerson authPerson) throws Exception{
        String num = authPerson.getIdCardNum();
        String name = authPerson.getIdCardName();
        Integer receiveUserId = userDao.getReceiveUserIdByUserId(authPerson.getUserId());
        AuthView authView = AuthUtil.auth(num, name);
        ObjectMapper objectMapper = new ObjectMapper();

        receiveUserDao.updateAuthJson(objectMapper.writeValueAsString(authView),receiveUserId);
        return authView.getShowapi_res_body().getCode() == 0;
    }


    @PostMapping("auth/skill")
    @Log("添加技能认证")
    public Result authS(@RequestBody String data) {

        //解析多张图片，把多张图片地址数组中间加@成为字符串存入skillCertificate
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("skillCertificate").getAsJsonArray();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> skillCertificate= new Gson().fromJson(jsonArray, type);
        StringBuilder stringBuilder = new StringBuilder();
        skillCertificate.forEach(img -> stringBuilder.append(img).append("@"));
        XzqnAuthSkill authSkill = new XzqnAuthSkill();

        Integer userId = jsonObject.get("userId").getAsInt();
        boolean exists = authSDao.existsByUserId(userId);
        if (exists) {
            authSDao.deleteByUserId(userId);
        }
        int receiveUserId = userDao.getReceiveUserIdByUserId(userId);
        authSkill.setReceiveUserId(receiveUserId);
        authSkill.setIsCheck(1);
        authSkill.setSkillCertificate(stringBuilder.toString());
        authSkill.setUserId(userId);
        //authSkill.setSkillCertificate();
        XzqnAuthSkill save = authSDao.save(authSkill);
        Integer authId = save.getId();
        //设置 receive authSkillId
        XzqnUserReceive userReceive = receiveUserDao.findById(receiveUserId).get();
        userReceive.setAuthSkillId(authId);
        if (userReceive.getAuthType().equals("未认证")) {
            userReceive.setAuthType("技能");
        }
        StringBuilder builder = new StringBuilder(userReceive.getAuthStatus());
        userReceive.setAuthStatus(builder.replace(2, 3, "1").toString());
        receiveUserDao.save(userReceive);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("authId", authId);

        //有新的技能认证消息
        webSocket.sendMessage("有新的技能认证消息");

        return ResultUtils.success(200, "技能认证成功", map);
    }


    @PostMapping("auth/personFind")
    @Log("查找个人认证")
    public Result authFindP(@RequestBody String data) {
        int userId = new JsonParser().parse(data).getAsJsonObject().get("userId").getAsInt();
        XzqnAuthPerson authPerson = authPersonDao.findTopByUserIdOrderByIdDesc(userId);
        return ResultUtils.success(200, "查个人认证成功", authPerson);
    }

    @PostMapping("auth/skillFind")
    @Log("查找技能认证")
    public Result authFindS(@RequestBody String data) {
        int userId = new JsonParser().parse(data).getAsJsonObject().get("userId").getAsInt();
        XzqnAuthSkill resp = authSDao.findTopByUserIdOrderByIdDesc(userId);
        return ResultUtils.success(200, "查找技能认证成功", resp);
    }

    @PostMapping("auth/contentFind")
    @Log("查找服务认证")
    public Result authFindContent(@RequestBody String data) {
        int userId = new JsonParser().parse(data).getAsJsonObject().get("userId").getAsInt();
        XzqnAuthContent authContent = authContentDao.findTopByUserIdOrderByIdDesc(userId);
        String introduce = authContent.getIntroduce();
        if (introduce == null) {
            introduce = "";
        }
        authContent.setIntroduceList(Arrays.asList(introduce.split("@")));
        return ResultUtils.success(200, "查找服务认证成功", authContent);
    }
}
