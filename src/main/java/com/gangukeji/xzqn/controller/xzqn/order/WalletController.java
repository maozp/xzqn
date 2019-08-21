package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnUserReceive;
import com.gangukeji.xzqn.entity.XzqnServicePay;
import com.gangukeji.xzqn.utils.BigDecimalUtil;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/29 16:07
 * @Description:
 */
@RestController
public class WalletController {
    @Autowired
    ServicePayDao payDao;
    @Autowired
    ServiceOrderDao orderDao;
    @Autowired
    ServicePublishDao publishDao;
    @Autowired
    ReceiveUserDao receiveUserDao;
    @Autowired
    UserDao userDao;

    /**
     * 我的钱包 和 钱包记录
     * @param data
     * @return
     */
    @PostMapping("money/record/findAll")
    @Log
    public Result record(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int userId = jsonObject.get("userId").getAsInt();
        List<XzqnServicePay> servicePayList = payDao.findByUserIdOrderByPayIdDesc(userId);
        BigDecimal ketixian = new BigDecimal(0);
        BigDecimal shouru = new BigDecimal(0);
        BigDecimal yitixian = new BigDecimal(0);
        //设置totalFee 和订单到描述
        for (XzqnServicePay e : servicePayList) {
            if (e.getMoney().doubleValue() >= 0) {
                e.setDes("订单收入");
                e.setDetail("订单收入");
                buildServicePay(e);
                shouru = BigDecimalUtil.add(shouru.doubleValue(), e.getMoney().doubleValue());
            } else {
                e.setDes("提现");
                e.setDetail("已提现至微信钱包");
                yitixian = BigDecimalUtil.add(yitixian.doubleValue(), e.getMoney().doubleValue());
            }
            ketixian = BigDecimalUtil.add(ketixian.doubleValue(), e.getMoney().doubleValue());
        }
        DecimalFormat df = new DecimalFormat("0.00");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ketixian", df.format(ketixian));
        hashMap.put("shouru", df.format(shouru));
        hashMap.put("yitixian", df.format(yitixian.abs()));
        hashMap.put("record", servicePayList);
        return ResultUtils.success(200, "查询钱包记录成功", hashMap);
    }

    /**
     * 设置发单方的头像昵称到支付记录
     * @param servicePay
     */
    private void buildServicePay(XzqnServicePay servicePay) {
        try {
            Integer orderId = servicePay.getOrderId();
            Object object = orderDao.getPublishIdAndSendUserIdByOrderId(orderId);
            int publishId = (int) ((Object[]) object)[0];
            int sendUserId = (int) ((Object[]) object)[1];
            int receiveUserId = userDao.getReceiveUserIdBySendUserId(sendUserId);
            XzqnUserReceive userReceive = receiveUserDao.findById(receiveUserId).get();
            servicePay.setDetail(publishDao.getServiceDes(publishId));
            //设置发单方详情
            servicePay.setImg(userReceive.getHeadImg());
            servicePay.setName(userReceive.getName());
        } catch (Exception e) {
            //取款记录没有信息抛的异常
            System.out.println(e.getMessage());
        }
    }

//    @PostMapping("money/record/test")
//    public Result test() {
//        XzqnServicePay servicePay = new XzqnServicePay();
//        servicePay.setOrderId(1);
//        buildServicePay(servicePay);
//        return null;
//    }

    /**
     * 取钱
     * @param data
     * @return
     */
    @PostMapping("money/record/out")
    @Log
    public Result withdrawal(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int userId = jsonObject.get("userId").getAsInt();
        BigDecimal money = jsonObject.get("money").getAsBigDecimal();
        //前端必须传负的金额
        if (money.doubleValue() >= 0) {
            return ResultUtils.error(-1, "提现必须为负数");
        }
        //判断余额是否够
        if (!enough(jsonObject)) {
            return ResultUtils.error(-1, "钱不够");
        }
        XzqnServicePay servicePay = new XzqnServicePay();
        servicePay.setMoney(money);
        servicePay.setSerializeNo(System.currentTimeMillis() + "" + RandomUtils.nextInt(10000, 99999));
        servicePay.setUserId(userId);
        XzqnServicePay save = payDao.save(servicePay);
        return ResultUtils.success(200, "提现成功", save);
    }

    /**
     * 判断余额是否够取
     *
     * @param jsonObject
     * @return
     */
    private boolean enough(JsonObject jsonObject) {
        int userId = jsonObject.get("userId").getAsInt();
        BigDecimal money = jsonObject.get("money").getAsBigDecimal();
        List<XzqnServicePay> servicePayList = payDao.findByUserIdOrderByPayIdDesc(userId);
        BigDecimal totalFee = new BigDecimal(0);
        for (XzqnServicePay e : servicePayList) {
            totalFee = totalFee.add(e.getMoney());
        }
        return totalFee.doubleValue()-money.doubleValue() >= 0;
    }

    /**
     * 回执单
     * @param data
     * @return
     */
    @PostMapping("money/record/findByOrderId")
    @Log
    public Result findByOrderId(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int orderId = jsonObject.get("orderId").getAsInt();
        XzqnServicePay servicePay = payDao.findByOrderId(orderId);
        buildServicePay(servicePay);
        servicePay.setMoney(servicePay.getMoney().abs());
        return ResultUtils.success(200, "查询付款回执单成功", servicePay);
    }

    /**
     * 钱包记录详情
     * @param data
     * @return
     */
    @PostMapping("money/record/findByPayId")
    @Log
    public Result findByPayId(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int payId = jsonObject.get("payId").getAsInt();
        XzqnServicePay servicePay = payDao.findByPayId(payId);
        buildServicePay(servicePay);
        return ResultUtils.success(200, "查询付款回执单成功", servicePay);
    }
}
