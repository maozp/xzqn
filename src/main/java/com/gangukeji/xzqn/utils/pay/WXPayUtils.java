package com.gangukeji.xzqn.utils.pay;

import com.gangukeji.xzqn.utils.MD5;
import com.gangukeji.xzqn.utils.XmlUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;


/**
 * @Author: hx
 * @Date: 2019/6/12 17:01
 * @Description: 支付工具设置
 */
public class WXPayUtils {
    /**
     * map转string 用于微信支付
     * @param paraMap
     * @return
     */
    public static String combineMap(SortedMap<String, String> paraMap) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();

                    buf.append(key + "=" + val);

                    buf.append("&");
                }
            }
            buff = buf.toString();
            if (!buff.isEmpty()) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }

    /**
     * 微信支付要求map排好序,然后转xml
     * @param openid
     * @param total_fee
     * @param spbill_create_ip
     * @param orderNo
     * @param nonce_str
     * @param body
     * @param packageParams
     * @return
     */
    public static SortedMap<String, String> initMap(String openid, String total_fee, String spbill_create_ip, String orderNo, String nonce_str, String body, SortedMap<String, String> packageParams) {
        packageParams.put("appid", WXPayConfig.appid);
        packageParams.put("mch_id", WXPayConfig.mchId);
//            packageParams.put("sign_type", "MD5");
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", orderNo);//商户订单号
        packageParams.put("total_fee", total_fee);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", WXPayConfig.notifyUrl);
        packageParams.put("trade_type", "JSAPI");
        packageParams.put("openid", openid);
        return packageParams;
    }

    /**
     *
     * 随机数生成
     * @return
     */
    public static String randomString() {
        return String.valueOf(RandomUtils.nextInt(100000000, 999999999)) + RandomUtils.nextInt(100000000, 999999999) + RandomUtils.nextInt(100000000, 999999999);
    }

    /**
     * 构造xml用于统一下单
     * @param openid
     * @param total_fee
     * @param spbill_create_ip
     * @param orderNo
     * @param nonce_str
     * @param body
     * @param mapToSign
     * @return
     */
    public static String getXML(String openid, String total_fee, String spbill_create_ip, String orderNo, String nonce_str, String body, String mapToSign) {
        return "<xml>" + "<appid>" + WXPayConfig.appid + "</appid>"
                + "<body><![CDATA[" + body + "]]></body>"
                + "<mch_id>" + WXPayConfig.mchId + "</mch_id>"
                + "<nonce_str>" + nonce_str + "</nonce_str>"
                + "<notify_url>" + WXPayConfig.notifyUrl + "</notify_url>"
                + "<openid>" + openid + "</openid>"
                + "<out_trade_no>" + orderNo + "</out_trade_no>"
                + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                + "<total_fee>" + total_fee + "</total_fee>"
                + "<trade_type>" + "JSAPI" + "</trade_type>"
                + "<sign>" + mapToSign + "</sign>"
                + "</xml>";
    }
    /**
     * 微信支付要求map排好序,然后转xml
     * @param openid
     * @param total_fee
     * @param spbill_create_ip
     * @param orderNo
     * @param nonce_str
     * @param body
     * @param packageParams
     * @return
     */
    public static SortedMap<String, String> initMap2(String openid, String total_fee, String spbill_create_ip, String orderNo, String nonce_str, String body, SortedMap<String, String> packageParams) {
        packageParams.put("appid", WXPayConfig.appid2);
        packageParams.put("mch_id", WXPayConfig.mchId);
//            packageParams.put("sign_type", "MD5");
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", orderNo);//商户订单号
        packageParams.put("total_fee", total_fee);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", WXPayConfig.notifyUrl);
        packageParams.put("trade_type", "JSAPI");
        packageParams.put("openid", openid);
        return packageParams;
    }
    /**
     * 构造xml用于统一下单
     * @param openid
     * @param total_fee
     * @param spbill_create_ip
     * @param orderNo
     * @param nonce_str
     * @param body
     * @param mapToSign
     * @return
     */
    public static String getXML2(String openid, String total_fee, String spbill_create_ip, String orderNo, String nonce_str, String body, String mapToSign) {
        return "<xml>" + "<appid>" + WXPayConfig.appid2 + "</appid>"
                + "<body><![CDATA[" + body + "]]></body>"
                + "<mch_id>" + WXPayConfig.mchId + "</mch_id>"
                + "<nonce_str>" + nonce_str + "</nonce_str>"
                + "<notify_url>" + WXPayConfig.notifyUrl + "</notify_url>"
                + "<openid>" + openid + "</openid>"
                + "<out_trade_no>" + orderNo + "</out_trade_no>"
                + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                + "<total_fee>" + total_fee + "</total_fee>"
                + "<trade_type>" + "JSAPI" + "</trade_type>"
                + "<sign>" + mapToSign + "</sign>"
                + "</xml>";
    }

    /**
     * 签名构造,需要两次签名
     * 第一次用于统一下单
     * 第二次帮小程序签好名,然后发起支付
     * @param data
     * @param key
     * @param sign
     * @return
     * @throws Exception
     */
    public static String generateSignature(final Map<String, String> data, String key, String sign) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(sign)) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        return MD5.md5(sb.toString(), "").toUpperCase();
    }

    /**
     * 获取xml中的元素
     * @param xml
     * @param e
     * @return
     * @throws Exception
     */
    public static String getElement(String xml,String e) throws Exception{
        return XmlUtil.toMap(xml).get(e);
    }
}
