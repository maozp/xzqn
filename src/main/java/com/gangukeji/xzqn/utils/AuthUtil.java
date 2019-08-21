package com.gangukeji.xzqn.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gangukeji.xzqn.entity.view.AuthView;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hx
 * @Date: 2019/7/23 14:49
 * @Description:
 */
public class AuthUtil {
    public static AuthView auth(String idcard, String name) throws Exception {
        String host = "http://idcard3.market.alicloudapi.com";
        String path = "/idcardAudit";
        String method = "GET";
        String appcode = "e7e99de363c2425b9b30cefcac6d5278";
        Map<String, String> headers = new HashMap();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap();
        querys.put("idcard", idcard);
        querys.put("name", name);
        HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
        String data = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        AuthView authView = mapper.readValue(data, AuthView.class);
        return authView;
    }
}
