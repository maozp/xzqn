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

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.http.HttpResponse;
import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.*;
import com.gangukeji.xzqn.utils.CodeUtil;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.pay.WXPayConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.AlgorithmParameters;
import java.security.Security;
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
    @Resource
    SmsDao smsDao;

    //小程序
    private final static String appid = WXPayConfig.appid;
    //appsecret
    private final static String secret = WXPayConfig.secret;
    private final static String mchId = WXPayConfig.mchId;
    private final static String key = WXPayConfig.key;
    private final static String notifyUrl = WXPayConfig.notifyUrl;
//    private final static String spbillCreateIp = "47.111.171.242";

    //APP
    private final static String appid3 = WXPayConfig.appid3;
    //appsecret
    private final static String secret3 = WXPayConfig.secret3;

   //小程序微信授权登录
    public String getOpenid(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        return new JsonParser().parse(Objects.requireNonNull(restTemplate.getForEntity("https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code", String.class).getBody())).getAsJsonObject().get("openid").getAsString();
    }
//    //小程序微信授权登录
    public JsonObject getOpenidAndKey(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        return new JsonParser().parse(Objects.requireNonNull(restTemplate.getForEntity("https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code", String.class).getBody())).getAsJsonObject();
    }



    //APP授权登录
    //https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
    public String getOpenid2(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        return new JsonParser().parse(Objects.requireNonNull(restTemplate.getForEntity("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid3 + "&secret=" + secret3 + "&js_code=" + code + "&grant_type=authorization_code", String.class).getBody())).getAsJsonObject().get("openid").getAsString();
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
            userId = initUser("","", username, password).getId();
        } catch (Exception e) {
            System.out.printf("25eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee注册异常" + e.getMessage());
            return ResultUtils.error(-1, "注册异常");
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("userId", userId);
        return ResultUtils.success(200, "注册成功", map);
    }

//    //手机注册
//    @PostMapping("app/phone/register")
//    @Log
//    public Result registerPhoneApp(@RequestBody String data) {
//        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
//        Integer userId;
//        try {
//            String userPhone = jsonObject.get("userPhone").getAsString();
//            //判断手机号是否已存在
//            XzqnUser user = userDao.findByUserPhone(userPhone);
//            if (user != null) {
//                return ResultUtils.error(-1, "手机号已存在");
//            }
//            Integer code = smsDao.findByPhoneAndCode(userPhone);
//            Integer inputCode=jsonObject.get("inputCode").getAsInt();
//            if(!code.equals(inputCode)){
//                return ResultUtils.error(-1, "验证码不正确");
//            }
//            userId = initUser3("", userPhone).getId();
//        } catch (Exception e) {
//            System.out.printf("25eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee注册异常" + e.getMessage());
//           return ResultUtils.error(-1, "注册异常");
//       }
//        HashMap<Object, Object> map = new HashMap<>();
//        map.put("userId", userId);
//        return ResultUtils.success(200, "注册成功", map);
//    }
    //手机登陆
    @PostMapping("app/phone/login")
    @Log
    public Result loginApp2(@RequestBody String data) {
        //将Json字符串转换成JsonObject对象：
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        Integer userId;
        try {
            String userPhone = jsonObject.get("userPhone").getAsString();
            //判断手机号是否已存在
            XzqnUser user = userDao.findByUserPhone(userPhone);
            if (user == null) {
                Integer code = smsDao.findByPhoneAndCode(userPhone);
                //Integer code =1111;
                Integer inputCode=jsonObject.get("inputCode").getAsInt();
                if(!code.equals(inputCode)){
                    return ResultUtils.error(-1, "验证码不正确");
                }
                userId = initUser3("", userPhone).getId();
                user=userDao.findByUserPhone(userPhone);
            }else{
                Integer code = smsDao.findByPhoneAndCode(userPhone);
                //Integer code =1111;
                Integer inputCode=jsonObject.get("inputCode").getAsInt();
                if(!code.equals(inputCode)){
                    return ResultUtils.error(-1, "验证码不正确");
                }
                userId = userDao.findByUserPhone(userPhone).getId();
            }
            return ResultUtils.success(200, "登录成功", user);

        } catch (Exception e) {
            System.out.printf("25eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee登录异常" + e.getMessage());
            return ResultUtils.error(-1, "登录异常");
        }

    }

    private XzqnUser initUser3(String openid, String userPhone) {
        //设置 发单 接单 用户参数
        XzqnUserReceive receive = new XzqnUserReceive();
        XzqnUserSend send = new XzqnUserSend();
        receive.setIsAuth(false);
        receive.setIsGuarantee(false);
        receive.setIsBan(false);
        receive.setAuthType("未认证");
        receive.setCompanyName("赣谷科技");
        receive.setNickname("");
        receive.setName("");
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
        receive.setPhone("");
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
        send.setNickname("");
        send.setName("");
        send.setPhone("");
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
        user.setUsername(null);
        user.setPassword(null);
        user.setUserPhone(userPhone);
        user.setOpenid(openid);
        user.setName("小正青年"+ RandomUtils.nextInt(10000, 99999));
        user.setUserHeadImg("https://www.xiaozheng8.com/1561174082137609928.png");
        user.setUserLevel(1);
        user.setMark("小正青年大有可为");
        user.setCity("南昌");
        //保存
        userDao.save(user);
        Integer userId=user.getId();
        String Rongname=user.getName();
        String headImg=user.getUserHeadImg();

        StringBuffer res = new StringBuffer();
        String url = "https://api.cn.ronghub.com/user/getToken.json";
        String App_Key = "sfci50a7s3pvi"; //融云开发者平台分配的 App Key。
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
        nameValuePair.add(new BasicNameValuePair("name",Rongname));
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
        user.setRongToken(object.get("token").getAsString());
        userDao.save(user);
        return user;
    }

    /**
     * 登录 2
     */
    @PostMapping("user/login")
    public Result login2(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        XzqnUser user;
        String openid = null;
        try {
            String code = jsonObject.get("code").getAsString();
            openid = getOpenid(code);
            user = userDao.findByOpenid(openid);
            if (user == null) {
                return ResultUtils.success(200, "数据库没有,创建新用户", initUser(openid, "",null, null));
            }
            return ResultUtils.success(200, "登录成功", user);
        } catch (Exception e) {
            System.out.println(e + "3eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            return ResultUtils.error(-1, "code异常或数据库异常" + e.getMessage());
        }
    }

    /**
     * 登录 3
     */
    @PostMapping("user/loginV2")
    public Result login2V2(@RequestBody String data) {
        //将Json字符串转换成JsonObject对象：
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        XzqnUser user;
        String openid = null;
        try {
            String code = jsonObject.get("code").getAsString();
            String encryptedData = jsonObject.get("encryptedData").getAsString();
            String iv = jsonObject.get("iv").getAsString();
            JsonObject jsonOb=getOpenidAndKey(code);
            openid=jsonOb.get("openid").getAsString();
            String session_key=jsonOb.get("session_key").getAsString();

            // 解密encryptedData,获取unionId相关信息
            JSONObject json = decryptionUserInfo(encryptedData, session_key, iv);
            String unionid=json.get("unionId").toString();


            user = userDao.findByUnionid(unionid);
            if (user == null) {
                return ResultUtils.success(200, "数据库没有,创建新用户", initUser(openid,unionid, null, null));
            }
            return ResultUtils.success(200, "登录成功", user);
        } catch (Exception e) {
            System.out.println(e + "3eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            return ResultUtils.error(-1, "code异常或数据库异常" + e.getMessage());
        }
    }
    /**
     * 小程序解密用户数据
     *
     * @param encryptedData
     * @param sessionKey
     * @param iv
     * @return
     */
    public static JSONObject decryptionUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decodeBase64(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decodeBase64(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);

        try {
            // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    private XzqnUser initUser(String openid,String unionid, String username, String password) {
        //设置 发单 接单 用户参数
        XzqnUserReceive receive = new XzqnUserReceive();
        XzqnUserSend send = new XzqnUserSend();
        receive.setIsAuth(false);
        receive.setIsGuarantee(false);
        receive.setIsBan(false);
        receive.setAuthType("未认证");
        receive.setCompanyName("赣谷科技");
        receive.setNickname("");
        receive.setName("");
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
        receive.setPhone("");
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
        send.setNickname("");
        send.setName("");
        send.setPhone("");
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
        user.setUnionid(unionid);
        user.setUserPhone(null);
        user.setRongToken(null);
        user.setName("小正青年"+ RandomUtils.nextInt(10000, 99999));
        user.setUserHeadImg("https://www.xiaozheng8.com/1561174082137609928.png");
        user.setUserLevel(1);
        user.setMark("小正青年大有可为");
        user.setCity("南昌");
        //保存
        userDao.save(user);
        Integer userId=user.getId();
        String Rongname=user.getName();
        String headImg=user.getUserHeadImg();

        //获取融云token
        StringBuffer res = new StringBuffer();
        String url = "https://api.cn.ronghub.com/user/getToken.json";
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
        nameValuePair.add(new BasicNameValuePair("name",Rongname));
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
        user.setRongToken(object.get("token").getAsString());
        userDao.save(user);
        return user;
    }

    /**
     * 登录 3
     */
    @PostMapping("user/wxapp/login")
    public Result login3(@RequestBody String data) {
        //将Json字符串转换成JsonObject对象：
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        XzqnUser user;
        try {
            String openid = jsonObject.get("openid").getAsString();
            String unionid= jsonObject.get("unionid").getAsString();
            user = userDao.findByUnionid(unionid);
            if (user == null) {
                return ResultUtils.success(200, "数据库没有,创建新用户", initUser2(openid, unionid,null, null));
            }
            System.out.println(user.getId());
            return ResultUtils.success(200, "登录成功", user);
        } catch (Exception e) {
            System.out.println(e + "3eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            return ResultUtils.error(-1, "code异常或数据库异常" + e.getMessage());
        }
    }

    private XzqnUser initUser2(String openid,String unionid, String username, String password) {
        //设置 发单 接单 用户参数
        XzqnUserReceive receive = new XzqnUserReceive();
        XzqnUserSend send = new XzqnUserSend();
        receive.setIsAuth(false);
        receive.setIsGuarantee(false);
        receive.setIsBan(false);
        receive.setAuthType("未认证");
        receive.setCompanyName("赣谷科技");
        receive.setNickname("");
        receive.setName("");
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
        receive.setPhone("");
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
        send.setNickname("");
        send.setName("");
        send.setPhone("");
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
        user.setUnionid(unionid);
        user.setUserPhone(null);
        user.setName("小正青年"+ RandomUtils.nextInt(10000, 99999));
        user.setUserHeadImg("https://www.xiaozheng8.com/1561174082137609928.png");
        user.setUserLevel(1);
        user.setMark("小正青年大有可为");
        user.setCity("南昌");
        //保存
        userDao.save(user);
        Integer userId=user.getId();
        String Rongname=user.getName();
        String headImg=user.getUserHeadImg();

        StringBuffer res = new StringBuffer();
        String url = "https://api.cn.ronghub.com/user/getToken.json";
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
        nameValuePair.add(new BasicNameValuePair("name",Rongname));
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
        user.setRongToken(object.get("token").getAsString());
        userDao.save(user);
        return user;
    }

    //绑定手机号
    @PostMapping("bindPhone")
    public Result bindPhone(@RequestBody String data) {
        //将Json字符串转换成JsonObject对象：
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();

            Integer userId=jsonObject.get("userId").getAsInt();
            String userPhone = jsonObject.get("userPhone").getAsString();
            //判断手机号是否已存在
            XzqnUser user = userDao.findByUserPhone(userPhone);
            if (user == null) {
                Integer code = smsDao.findByPhoneAndCode(userPhone);
                Integer inputCode=jsonObject.get("inputCode").getAsInt();
                if(!code.equals(inputCode)){
                    return ResultUtils.error(-1, "验证码不正确");
                }
                XzqnUser users=userDao.findById(userId).get();
                users.setUserPhone(userPhone);
                userDao.save(users);
                return ResultUtils.success(200, "绑定成功", users);
            }else{
                return ResultUtils.error(-1, "手机号已绑定");
            }


    }


}
