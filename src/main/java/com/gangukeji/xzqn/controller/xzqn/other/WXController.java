package com.gangukeji.xzqn.controller.xzqn.other;


import com.gangukeji.xzqn.serviceImpl.WXserviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Map;

@RestController
@RequestMapping("/weixin")
public class WXController {
    @Autowired
    private WXserviceImpl wxPayService;

    @PostMapping("/apppay")
    public Map wxAdd(@RequestParam(value = "user_id") String user_id, @RequestParam(value = "total_fee") String total_fee
            , @RequestParam(value = "oid") String oid) throws Exception {
        return wxPayService.dounifiedOrder(user_id, total_fee, oid);
    }


    /**
     * 支付异步结果通知，我们在请求预支付订单时传入的地址
     * 官方文档 ：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_7&index=3
     */
    @PostMapping(value = "/notify")
    public String wxPayNotify(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("======================微信支付异步结果通知开始=================================");
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxPayService.payBack(resXml);
            return result;
        } catch (Exception e) {
            System.out.println("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }
}

