package com.gangukeji.xzqn.serviceImpl;


import com.gangukeji.xzqn.service.WXservice;
import com.gangukeji.xzqn.utils.pay.WXConfigUtil;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class WXserviceImpl implements WXservice {
    //@Autowired
    //JinOrderMapper jinOrderMapper;
    private static final Logger logger = LoggerFactory.getLogger("MainLogger");
    public static final String SPBILL_CREATE_IP = ""; //服务器Ip
    public static final String NOTIFY_URL = ""; //回调地址
    public static final String TRADE_TYPE_APP = ""; //交易类型

    /**
     * 调用官方SDK 获取预支付订单等参数
     * 统一下单
     * @param user_id   用户ID
     * @param total_fee 总价
     * @return
     * @throws Exception
     */
    @Override
    public Map dounifiedOrder(String user_id, String total_fee, String oid) throws Exception {
        try {
            float cardprice1 = Float.parseFloat(total_fee) * 100;//微信的支付单位是分所以要转换一些单位
            int cardmoney = (int) cardprice1;
            String totalproce = String.valueOf(cardmoney);
            System.out.println(total_fee + "元=" + totalproce + "分");
            WXConfigUtil configUtil = new WXConfigUtil();
            WXPay wxPay = new WXPay(configUtil);
            Map<String, String> data = new HashMap<>();
            //随机生成商户订单号
            String out_trade_no = "wxpay" + System.currentTimeMillis();
            //查询数据库获取订单编号
            //String out_trade_no = jinOrderMapper.selectOrderNum(oid);
            System.out.println("商户订单号------------" + out_trade_no);
            data.put("appid", configUtil.getAppID()); //appid
            data.put("mch_id", configUtil.getMchID()); //商户号
            data.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
            String body = "测试微信支付";
            data.put("body", body);//商品描述
            data.put("out_trade_no", out_trade_no); //商品订单号
            data.put("total_fee", totalproce);  // 总金额
            data.put("spbill_create_ip", SPBILL_CREATE_IP);//终端IP
            data.put("notify_url", NOTIFY_URL);//回调地址
            data.put("trade_type", TRADE_TYPE_APP);//交易类型
            //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
            data.put("attach", user_id);
            String sign = WXPayUtil.generateSignature(data, configUtil.getKey(), WXPayConstants.SignType.MD5);
            data.put("sign", sign); //生成签名
            String str = WXPayUtil.mapToXml(data);
            System.out.println("map转xml" + str);
            System.out.println("我给的数据是" + data);
            System.out.println("第一次签名------------------" + sign);
            //使用官方API请求预付订单
            Map<String, String> response = wxPay.unifiedOrder(data);
            String returnCode = response.get("return_code");    //获取返回码
            Map<String, String> param = new LinkedHashMap<>();
            //判断返回状态码是否成功
            if (returnCode.equals("SUCCESS")) {
                //成功后接受微信返回的参数
                String resultCode = response.get("result_code");
                System.out.println("获取返回码" + resultCode);
                System.out.println("获取返回码错误代码---" + response.get("err_code") + "---获取返回码错误代码描述---" + response.get("err_code_des"));
                param.put("appid", response.get("appid"));
                param.put("partnerid", response.get("mch_id"));//商户号
                param.put("package", "Sign=WXPay");
                param.put("noncestr", WXPayUtil.generateNonceStr());//随机字符串
                param.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));     //时间戳
                if (resultCode.equals("SUCCESS")) {
                    param.put("prepayid", response.get("prepay_id"));//预支付交易会话ID
                    String retutnSign = WXPayUtil.generateSignature(param, configUtil.getKey(), WXPayConstants.SignType.MD5);
                    System.out.println("第二次签名------------------" + retutnSign);
//说一下这个第二次的签名不是获取，而是拿着你请求微信支付回来的数据再去请求一次生成签名，方式一定要和第一次相同。
                    param.put("sign", retutnSign);
                    String str1 = WXPayUtil.mapToXml(param);
                    System.out.println("map转xml" + str1);
                    param.put("tradetype", response.get("trade_type")); //交易类型
                    return param;
                } else {
                    // //此时返回没有预付订单的数据
                    System.out.println("此时返回没有预付订单的数据");
                    return param;
                }

            } else {
                System.out.println("没有返回我接受到的微信参数");
                return param;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("下单失败");
    }


    /**
     * @param notifyData 异步通知后的XML数据
     * @return
     */
    @Override
    public String payBack(String notifyData) {
        System.out.println("======================微信支付异步结果逻辑处理开始=================================");
        WXConfigUtil config = null;
        try {
            config = new WXConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(notifyData);// 调用官方SDK转换成map类型数据
            System.out.println("返回的map----------------" + notifyMap);
            System.out.println("返回的错误代码--------" + notifyMap.get("err_code") + "返回的错误信息--------" + notifyMap.get("err_code_des"));
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                String userId = notifyMap.get("attach");
                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        //业务数据持久化
                        //修改数据库支付状态
                        //修改数据库支付方式
                        System.err.println("-------------------------------支付成功----------------------");
                        logger.info("微信手机支付回调成功订单号:{}", out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        logger.info("微信手机支付回调失败订单号:{}", out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                //失败的数据要不要存储？
                logger.error("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            logger.error("手机支付回调通知失败", e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }

}

