package com.gangukeji.xzqn.controller.shop;

import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.shop.XzqnShopOrderDetail;
import com.gangukeji.xzqn.entity.shop.XzqnShopOrderMaster;
import com.gangukeji.xzqn.utils.MD5;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.pay.IpUtils;
import com.gangukeji.xzqn.utils.pay.WXPayConfig;
import com.gangukeji.xzqn.utils.pay.WXPayUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 */
@Slf4j
@RestController
@RequestMapping("shop/order")
public class ShopOrderController {
    @Resource
    private ShopOrderMasterDao masterDao;
    @Resource
    private ShopOrderDetailDao detailDao;
    @Resource
    private ShopProductInfoDao productDao;
    @Resource
    ShopUserDao shopUserDao;

    /**
     * 通过orderId查订单
     */
    @PostMapping({"findById"})
    private Result findByIdOrder(@RequestBody String data) {
        XzqnShopOrderMaster master;
        try {
            Integer orderId = new JsonParser().parse(data).getAsJsonObject().get("orderId").getAsInt();
            master = masterDao.findById(orderId).get();
            List<XzqnShopOrderDetail> details = detailDao.findAllByOrderId(orderId);
            details.forEach(detail -> {
                String pic = productDao.findById(detail.getProductId()).get().getPicList()[0];
                detail.setPic(pic);
            });
            master.setDetails(details);
            master.setProductCount(details.size());
        } catch (Exception e) {
            return ResultUtils.error(-1, "not exist");
        }
        return ResultUtils.success(200, "orderId查找成功", master);
    }


    /**
     * 通过订单状态 statusList数组 userId查订单
     * 6种status时间 1未支付 2已支付 3出货中 4寄货中 5已完成 6已评价
     */
    @PostMapping({"findByStatusAndUserId", "findByUserIdAndStatus"})
    private Result findAllOrder(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        //默认返回100条数据
        PageRequest pageRequest = new PageRequest(0, 100);
        try {
            pageRequest = new PageRequest(jsonObject.get("page").getAsInt(), jsonObject.get("size").getAsInt());
        } catch (Exception e) {
            System.out.println("未分页");
        }
        JsonArray jsonArray = jsonObject.get("statusList").getAsJsonArray();
        int userId = jsonObject.get("userId").getAsInt();
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        List<Integer> statusList = new Gson().fromJson(jsonArray, type);
        //分页查订单并排序
        Page<XzqnShopOrderMaster> masterList = masterDao.findAllByUserIdAndOrderStatusInOrderByOrderIdDesc(userId, statusList, pageRequest);
        List<XzqnShopOrderMaster> resp = masterList.getContent();
        //构造订单详情
        setOrderDetails(resp);
        return ResultUtils.success(200, "订单根据statusList和userId查找成功", resp);
    }

    /**
     * 设置订单主表的订单详情
     * @param masterList
     */
    private void setOrderDetails(List<XzqnShopOrderMaster> masterList) {
        masterList.forEach(master -> {
            List<XzqnShopOrderDetail> detailList = detailDao.findAllByOrderId(master.getOrderId());
            detailList.forEach(detail -> {
                //首张图片为商品封面
                String pic = productDao.findById(detail.getProductId()).get().getPicList()[0];
                detail.setPic(pic);
            });

            master.setDetails(detailList);
        });
    }

    /**
     * 根据  userId 查用户所有订单
     */
    @PostMapping({"findAllByUserId", "findByUserId"})
    private Result findAllByUserId(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        //默认查100条
        PageRequest pageRequest = new PageRequest(0, 100);
        try {
            pageRequest = new PageRequest(jsonObject.get("page").getAsInt(), jsonObject.get("size").getAsInt());
        } catch (Exception e) {
            System.out.println("未分页");
        }
        int userId = jsonObject.get("userId").getAsInt();
        Page<XzqnShopOrderMaster> masterList = masterDao.findAllByUserId(userId, pageRequest);
        List<XzqnShopOrderMaster> resp = masterList.getContent();
        //设置订单详情
        setOrderDetails(resp);
        //去除取消了的订单
        resp = resp.stream().filter(e -> e.getOrderStatus() != -1).collect(Collectors.toList());
        //id值排序
        resp = resp.stream().sorted(Comparator.comparing(XzqnShopOrderMaster::getOrderId).reversed()).collect(Collectors.toList());
        return ResultUtils.success(200, "订单根据userId查找成功", resp);
    }

    /**
     * 更新订单状态
     * 6种status时间 1未支付 2已支付 3出货中 4寄货中 5已完成 6已评价
     */
    @PostMapping("setStatus")
    private Result setStatus(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int status = 0;
        int orderId = 0;
        try {
            status = jsonObject.get("status").getAsInt();
            orderId = jsonObject.get("orderId").getAsInt();
        } catch (Exception e) {
            return ResultUtils.error(-1, "参数错误status orderId没传");
        }
        masterDao.updateOrderStatus(status, orderId);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("status", status);
        return ResultUtils.success(200, "设置状态成功", map);
    }

    /**
     * 订单支付
     * 不能修改价格支付,否则会支付失败,如果需要测试,请重新下一张一分钱的订单
     * 支付参数在{@link com.gangukeji.xzqn.utils.pay.WXPayConfig}中设置
     */
    @PostMapping("pay")
    private Map<String, String> pay(@RequestBody String data, HttpServletRequest request) {
        SortedMap<String, String> respMap = null;
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        String totalFee;
        try {
            int orderId = jsonObject.get("orderId").getAsInt();
            XzqnShopOrderMaster master = masterDao.findByOrderId(orderId);
            Integer userId = master.getUserId();
            try {
                //前端测试传金额,否则支付订单原价
                totalFee = jsonObject.get("totalFee").getAsString();
            } catch (Exception e) {
                totalFee = master.getOrderMoney().multiply(new BigDecimal(100)).toString().replace(".00", "");
            }
            String openid = shopUserDao.findById(userId).get().getOpenid();
            String spbillCreateIp = IpUtils.getIpAddr(request);
            String orderNo = master.getOrderNo();
            String nonce_str = WXPayUtils.randomString();
            String body = "orderId" + orderId;
            SortedMap<String, String> packageParams = new TreeMap<>();
            packageParams = WXPayUtils.initMap2(openid, totalFee, spbillCreateIp, orderNo, nonce_str, body, packageParams);
            String mapStr = WXPayUtils.combineMap(packageParams);
            String mapToSign = MD5.md5(mapStr, "&key=" + WXPayConfig.key).toUpperCase();
            String xml = WXPayUtils.getXML2(openid, totalFee, spbillCreateIp, orderNo, nonce_str, body, mapToSign);
            RestTemplate restTemplate = new RestTemplate();
            String wxResp = restTemplate.postForEntity("https://api.mch.weixin.qq.com/pay/unifiedorder", xml, String.class).getBody();
            wxResp = new String(wxResp.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String prePayId = WXPayUtils.getElement(wxResp, "prepay_id");
            String nonceStr = WXPayUtils.getElement(wxResp, "nonce_str");
            String sign = WXPayUtils.getElement(wxResp, "sign");
            respMap = new TreeMap<>();
            respMap.put("appId", WXPayConfig.appid2);
            respMap.put("nonceStr", nonceStr);
            respMap.put("package", "prepay_id=" + prePayId);
            respMap.put("signType", "MD5");
            respMap.put("timeStamp", (new Date().getTime() + ""));
            String signTwo = WXPayUtils.generateSignature(respMap, WXPayConfig.key, sign);
            respMap.put("paySign", signTwo);
        } catch (Exception e) {
            e.printStackTrace();
            return respMap;
        }
        return respMap;
    }
}
