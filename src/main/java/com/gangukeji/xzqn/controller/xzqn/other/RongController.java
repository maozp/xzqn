package com.gangukeji.xzqn.controller.xzqn.other;

import com.aliyuncs.http.HttpResponse;
import com.gangukeji.xzqn.dao.UserDao;
import com.gangukeji.xzqn.entity.XzqnUser;
import com.gangukeji.xzqn.utils.CodeUtil;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  没用到  直接在登录获取
 */
@Deprecated
@RestController
@RequestMapping("/RongYun")
public class RongController {

    @Resource
    UserDao userDao;

//    @PostMapping("Token")
//    public String token(@RequestBody String data){
//        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
//        String userId= jsonObject.get("userId").getAsString();
//        //String portraitUri= jsonObject.get("portraitUri").getAsString();
//        //String name= jsonObject.get("name").getAsString();
//
//        StringBuffer res = new StringBuffer();
//        String url = "https://api.cn.ronghub.com/user/getToken.json";
//        String App_Key = "sfci50a7s3pvi"; //开发者平台分配的 App Key。
//        String App_Secret = "duJHa7WHKnGX";
//        String Timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳，从 1970 年 1 月 1 日 0 点 0 分 0 秒开始到现在的秒数。
//        String Nonce = String.valueOf(Math.floor(Math.random() * 1000000));//随机数，无长度限制。
//        StringBuilder toSign = new StringBuilder(App_Secret).append(Nonce).append(Timestamp);
//        String Signature  = CodeUtil.hexSHA1(toSign.toString());//数据签名。
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("App-Key", App_Key);
//        httpPost.setHeader("Timestamp", Timestamp);
//        httpPost.setHeader("Nonce", Nonce);
//        httpPost.setHeader("Signature", Signature );
//        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
//        List<BasicNameValuePair> nameValuePair = new ArrayList<>(1);
//        nameValuePair.add(new BasicNameValuePair("userId", userId));
//        //nameValuePair.add(new BasicNameValuePair("name",name));
//        //nameValuePair.add(new BasicNameValuePair("portraitUri",portraitUri));
//        HttpResponse httpResponse = null;
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,"utf-8"));
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            CloseableHttpResponse result = httpClient.execute(httpPost);
//            BufferedReader br = new BufferedReader(new InputStreamReader(result.getEntity().getContent()));
//            String line = null;
//            while ((line = br.readLine()) != null) {
//                res.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        JsonObject object = new JsonParser().parse(res.toString()).getAsJsonObject();
//        return object.toString();
//    }

    @PostMapping("/information")
    public Result information(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        Integer userId = jsonObject.get("userId").getAsInt();

        XzqnUser user = userDao.findById(userId).get();
        return ResultUtils.success(200,"获取昵称头像成功",user);
    }

    //客服ID
    @PostMapping("/serviceId")
    public Result serviceId() {
        HashMap<String,String> map=new HashMap<>();
        map.put("serviceId","11253");
        return ResultUtils.success(200,"获取客服融云id成功",map);
    }

}
