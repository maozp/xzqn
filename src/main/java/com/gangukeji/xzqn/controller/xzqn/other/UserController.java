package com.gangukeji.xzqn.controller.xzqn.other;

import com.aliyuncs.http.HttpResponse;
import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnAuthContent;
import com.gangukeji.xzqn.entity.XzqnUser;
import com.gangukeji.xzqn.entity.XzqnUserReceive;
import com.gangukeji.xzqn.entity.view.Area;
import com.gangukeji.xzqn.utils.CodeUtil;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.StatusUtils2;
import com.gangukeji.xzqn.entity.view.AreaDto;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gangukeji.xzqn.utils.Utils.copyPropertiesIgnoreNull;

/**
 * @Author: hx
 * @Date: 2019/5/14 9:52
 * @Description:
 */
@SuppressWarnings("Duplicates")
@RestController
@RequestMapping
public class UserController {
    @Resource
    ReceiveUserDao receiveUserDao;
    @Resource
    UserDao userDao;
    @Resource
    AuthSkillDao authSkillDao;
    @Resource
    AuthContentDao authContentDao;
    @Resource
    AuthPersonDao authPersonDao;
    @Resource
    FollowDao followDao;


    /**
     * 更新师傅by userId
     */
    @PostMapping("receiveUser/update")
    @Log
    public Result updateReceiveV2(@RequestBody XzqnUserReceive userReceive) {
        Integer receiveUserId = userDao.getReceiveUserIdByUserId(userReceive.getUserId());
        XzqnUserReceive save = receiveUserDao.findById(receiveUserId).get();
        copyPropertiesIgnoreNull(userReceive, save);
        receiveUserDao.save(save);
        return ResultUtils.success(200, "更新接单用户信息成功", save);
    }
    /**
     * 更新师傅by receiveUserId
     */
    @PostMapping("receiveUser/updateByReceiveUserId")
    @Log
    public Result updateReceiveV3(@RequestBody XzqnUserReceive userReceive) {
        XzqnUserReceive save = receiveUserDao.findById(userReceive.getId()).get();
        copyPropertiesIgnoreNull(userReceive, save);
        receiveUserDao.save(save);
        return ResultUtils.success(200, "更新接单用户信息成功", save);
    }

    @PostMapping("receiveUser/findAll")
    @Log("查找全部师傅")
    public Result findAllReceive(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        PageRequest pageable = initPage(jsonObject);
        List<XzqnUserReceive> userReceives = receiveUserDao.findAll(pageable).getContent();
        return ResultUtils.success(200, "查找成功", userReceives);
    }
    @PostMapping("receiveUser/findAll/filter")
    @Log("filter师傅")
    public Result findAllReceiveFilter(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        PageRequest pageable = initPageFilter(jsonObject);
        List<XzqnUserReceive> userReceives = receiveUserDao.findAll(pageable).getContent();
        return ResultUtils.success(200, "查找成功", userReceives);
    }

    // TODO: 2019/6/26  
    @PostMapping("receiveUser/findAll/name")
    @Log("name师傅")
    public Result findAllReceiveByName(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        PageRequest pageable = initPage(jsonObject);
        List<XzqnUserReceive> userReceives = receiveUserDao.findAllByName(name,pageable);
        return ResultUtils.success(200, "查找成功", userReceives);
    }
    private PageRequest initPage(JsonObject jsonObject) {
        int page = jsonObject.get("page").getAsInt();
        int size = jsonObject.get("size").getAsInt();
        return new PageRequest(page, size);
    }
    private PageRequest initPageFilter(JsonObject jsonObject) {
        int page = jsonObject.get("page").getAsInt();
        int size = jsonObject.get("size").getAsInt();
        int filter = jsonObject.get("filter").getAsInt();
        Sort sort=new Sort(Sort.Direction.ASC,"id");
        if (filter == 1) {
             sort = new Sort(Sort.Direction.ASC, "turnover90");
        }
        if (filter == 2) {
             sort = new Sort(Sort.Direction.DESC, "turnover90");
        }
        if (filter == 3) {
            sort = new Sort(Sort.Direction.ASC, "rate");
        }
        if (filter == 4) {
            sort = new Sort(Sort.Direction.DESC, "rate");
        }
        return new PageRequest(page, size, sort);
    }
    @PostMapping("receiveUser/findByReceiveUserIdList")
    @Log("查找全部师傅")
    public Result findByUserIdList(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("receiveUserIdList").getAsJsonArray();
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        List<Integer> receiveUserIdList=new Gson().fromJson(jsonArray,type);
        List<XzqnUserReceive> userReceives = receiveUserDao.findAllById(receiveUserIdList);
        return ResultUtils.success(200, "查找成功", userReceives);
    }
    @SuppressWarnings("Duplicates")
/**
 * 查找师傅详情
 */
    @PostMapping("receiveUser/findByReceiveUserId")
    @Log
    public Result findReceive(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int receiveUserId = jsonObject.get("receiveUserId").getAsInt();
        Integer userId = userDao.getUserIdByReceiveUserId(receiveUserId);
        XzqnUserReceive userReceive = receiveUserDao.findById(receiveUserId).get();
        userReceive.setUserId(userId);
        buildReceiveUser(userReceive,userId);
        return ResultUtils.success(200, "成功查找师傅详情", userReceive);
    }
    /**
     * 查找师傅详情
     */
    @PostMapping("receiveUser/findById")
    @Log
    public Result findReceiveDetail(@RequestBody String data) {
        int userId = new JsonParser().parse(data).getAsJsonObject().get("userId").getAsInt();
        Integer receiveUserId = userDao.getReceiveUserIdByUserId(userId);
        XzqnUserReceive userReceive = receiveUserDao.findById(receiveUserId).get();
        userReceive.setUserId(userId);
        userReceive.setStarts(followDao.findByStarMeNums(userId));
        userReceive.setMyStarNums(followDao.findByMyStarNums(userId).toString());
        buildReceiveUser(userReceive,userId);
        return ResultUtils.success(200, "成功查找师傅详情", userReceive);
    }

    private void buildReceiveUser(XzqnUserReceive userReceive,int userId) {
        if (userReceive.getAuthType() == null) {
            userReceive.setAuthType("认证类型");
        } else {
            userReceive.setAuthType(userReceive.getAuthType());
        }
        int isCheck1= 0;
        try {
            isCheck1 = authContentDao.findByUserId(userId);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        int isCheck2= 0;
        try {
            isCheck2 = authPersonDao.findByUserId(userId);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        int isCheck3= 0;
        try {
            isCheck3 = authSkillDao.findByUserId(userId);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        userReceive.setAuthStatus(""+isCheck1+isCheck2+isCheck3);
        //设置服务内容
        XzqnAuthContent content=authContentDao.findXzqnAuthContentByUserId(userId);
        if (content != null) {
            userReceive.setIntroduce(content.getIntroduce());
            userReceive.setPromise(content.getPromise());
            userReceive.setArea(content.getArea());
            userReceive.setDes(content.getDes());
        }
    }


    @RequestMapping("user/area")
    public Result areaCate() {
        List<AreaDto> areaList = new ArrayList<>();
        areaList.add(new AreaDto(1, "抚州", false));
        areaList.add(new AreaDto(1, "南昌", false));
        areaList.add(new AreaDto(1, "宜春", false));
        areaList.add(new AreaDto(1, "上饶", false));
        areaList.add(new AreaDto(1, "九江", false));
        areaList.add(new AreaDto(1, "赣州", false));
        return ResultUtils.success(200, "查技地址成功", areaList);
    }
    @RequestMapping("user/area/v2")
    public Result areaCateV2() {
        Type type = new TypeToken<ArrayList<Area>>() {
        }.getType();
        List<Area> area = new Gson().fromJson(StatusUtils2.AREA, type);
        return ResultUtils.success(200,"area",area);
    }

    /**
     * 查看用户信息
     */

    @PostMapping("user/find")
    @Log
    public Result findUser(@RequestBody String data) {
        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer userId=jsonObject.get("userId").getAsInt();
        XzqnUser user = userDao.findById(userId).get();
        return ResultUtils.success(200, "查看用户信息成功", user);
    }

    /**
     * 更新师傅信息by userId
     */
    @PostMapping("user/update")
    @Log
    public Result updateUser(@RequestBody String data) {
        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer userId=jsonObject.get("userId").getAsInt();
        String headImg=jsonObject.get("headImg").getAsString();
        String nickName=jsonObject.get("nickName").getAsString();
        String mark=jsonObject.get("mark").getAsString();
        XzqnUser user = userDao.findById(userId).get();
        user.setUserHeadImg(headImg);
        user.setName(nickName);
        user.setMark(mark);

        StringBuffer res = new StringBuffer();
        String url = "http://api-cn.ronghub.com/user/refresh.json";
        String App_Key = "sfci50a7s3pvi"; //开发者平台分配的 App Key。
        String App_Secret = "duJHa7WHKnGX";
        String Timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳，从 1970 年 1 月 1 日 0 点 0 分 0 秒开始到现在的秒数。
        String Nonce = String.valueOf(Math.floor(Math.random() * 1000000));//随机数，无长度限制。
        StringBuilder toSign = new StringBuilder(App_Secret).append(Nonce).append(Timestamp);
        String Signature  = CodeUtil.hexSHA1(toSign.toString());//数据签名。
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("App-Key", App_Key);
        httpPost.setHeader("Timestamp", Timestamp);
        httpPost.setHeader("Nonce", Nonce);
        httpPost.setHeader("Signature", Signature );
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        List<BasicNameValuePair> nameValuePair = new ArrayList<>(1);
        nameValuePair.add(new BasicNameValuePair("userId", userId.toString()));
        nameValuePair.add(new BasicNameValuePair("name",nickName));
        nameValuePair.add(new BasicNameValuePair("portraitUri",headImg));
        HttpResponse httpResponse = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,"utf-8"));
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse result = httpClient.execute(httpPost);
            BufferedReader br = new BufferedReader(new InputStreamReader(result.getEntity().getContent()));
            String line = null;
            while ((line = br.readLine()) != null) {
                res.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject object = new JsonParser().parse(res.toString()).getAsJsonObject();


        userDao.save(user);

        return ResultUtils.success(200, "更新用户信息成功", user);
    }

}
