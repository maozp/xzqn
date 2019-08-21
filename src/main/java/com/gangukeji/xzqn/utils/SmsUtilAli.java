package com.gangukeji.xzqn.utils;//package com.gangukeji.xzqn.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @Author: hx
 * @Date: 2019/7/24 9:29
 * @Description: 腾讯云指定模板短信发送
 * }
 */
public class SmsUtilAli implements Serializable {
    private static final long serialVersionUID = 123123L;

    public static String send(String phoneNum) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAIRN41wqB8hkXu", "tHixXTFnh7gJOT873gtGmyUdAB7qYf");
        IAcsClient client = new DefaultAcsClient(profile);
        CommonResponse response = null;
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phoneNum);
        request.putQueryParameter("TemplateCode", "SMS_171192007");
        request.putQueryParameter("SignName", "小正青年");
        HashMap<String, String> map = new HashMap<>();
        String sendCode = RandomStringUtils.randomNumeric(4);
        map.put("code", sendCode);
        ObjectMapper objectMapper = new ObjectMapper();
        request.putQueryParameter("TemplateParam", objectMapper.writeValueAsString(map));
        response = client.getCommonResponse(request);
        System.out.println(response.getData());
        return sendCode;
    }
}
