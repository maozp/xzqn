package com.gangukeji.xzqn.controller.shop;

import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnServiceOrder;
import com.gangukeji.xzqn.entity.shop.XzqnShopOrderMaster;
import com.gangukeji.xzqn.entity.shop.XzqnShopPay;
import com.gangukeji.xzqn.entity.XzqnServicePay;
import com.gangukeji.xzqn.utils.pay.WXPayConfig;
import com.gangukeji.xzqn.utils.pay.WXPayUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @Author: hx
 * @Date: 2019/6/11 14:17
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("shop/pay")
public class ShopPayController {
    @Autowired
    ServicePayDao servicePayDao;
    @Autowired
    UserDao userDao;
    @Resource
    ShopOrderMasterDao masterDao;
    @Resource
    ServiceOrderDao serviceOrderDao;
    @Resource
    ShopPayDao shopPayDao;
    @Resource
    ServiceOrderDao orderDao;
    @Resource
    ServiceLogDao logDao;

    @RequestMapping("back")
    public void payCallBack(@RequestBody String data) throws Exception {
        log.error("back接收微信交易成功响应" + data);
//        WXPayUtils.getElement(data, "transaction_id");
        String resultCode = WXPayUtils.getElement(data, "result_code");
        String mchId = WXPayUtils.getElement(data, "mch_id");
        String openid = WXPayUtils.getElement(data, "openid");
        String outTradeNo = WXPayUtils.getElement(data, "out_trade_no");
        String totalFee = WXPayUtils.getElement(data, "total_fee");
        if (outTradeNo.contains("SHOP")) {
            XzqnShopOrderMaster order = masterDao.findByOrderNo(outTradeNo);
            if (resultCode.equals("SUCCESS") && mchId.equals(WXPayConfig.mchId) && order != null) {
                log.error("SUCCESS交易成功响应" + data);
//            order.setOrderStatus(2);
                //跳过已支付->寄货中
                order.setOrderStatus(4);
                masterDao.save(order);
                XzqnShopPay shopPay = new XzqnShopPay();
                shopPay.setMoney(BigDecimal.valueOf(Long.valueOf(totalFee)).divide(new BigDecimal(100)));
                shopPay.setOrderId(order.getOrderId());
                shopPay.setUserId(order.getUserId());
                shopPay.setStatus(1);
                shopPay.setType(1);
                shopPayDao.save(shopPay);
            } else {
                //做异常订单处理
                XzqnShopPay shopPay = new XzqnShopPay();
                shopPay.setMoney(BigDecimal.valueOf(Long.valueOf(totalFee)).divide(new BigDecimal(100)));
                assert order != null;
                shopPay.setOrderId(order.getOrderId());
                shopPay.setUserId(order.getUserId());
                shopPay.setStatus(-1);
                shopPay.setType(1);
                shopPayDao.save(shopPay);
                log.error("ERROR做异常订单处理");
            }
        } else {
            //xzqn服务施工的支付
            XzqnServiceOrder order = serviceOrderDao.findByOrderNo(outTradeNo.substring(0, outTradeNo.length() - 9));
            if (resultCode.equals("SUCCESS") && mchId.equals(WXPayConfig.mchId) && order != null) {
                log.error("SUCCESS交易成功响应" + data);

                //已支付
                serviceOrderDao.updateIsPay(true, outTradeNo);
                XzqnShopPay shopPayOrServiceOrderPay = new XzqnShopPay();//通用数据库 type区分  1shop 2service
                BigDecimal money = BigDecimal.valueOf(Long.valueOf(totalFee)).divide(new BigDecimal(100));
                shopPayOrServiceOrderPay.setMoney(money);
                shopPayOrServiceOrderPay.setOrderId(order.getOrderId());
                shopPayOrServiceOrderPay.setUserId(order.getUserId());
                shopPayOrServiceOrderPay.setStatus(1);
                shopPayOrServiceOrderPay.setType(2);
                orderDao.updateOrderStatus(11, order.getOrderId());
                updateLog(11, 1, 2, order.getOrderId());
                //防止重复数据type=2 orderId相同不存入数据库
                if (!shopPayDao.existsByTypeAndOrderId(2, order.getOrderId())) {
                    shopPayDao.save(shopPayOrServiceOrderPay);
                    XzqnServicePay xzqnServicePay = new XzqnServicePay();
                    xzqnServicePay.setMoney(money);
                    xzqnServicePay.setOrderId(order.getOrderId());
                    xzqnServicePay.setSerializeNo(System.currentTimeMillis() + "" + RandomUtils.nextInt(10000, 99999));
                    Integer userId = userDao.getUserIdByReceiveUserId(order.getReceiveUserId());
                    xzqnServicePay.setUserId(userId);
                    servicePayDao.save(xzqnServicePay);
                }
            } else {
                //做异常订单处理
                XzqnShopPay shopPay = new XzqnShopPay();
                shopPay.setMoney(BigDecimal.valueOf(Long.valueOf(totalFee)).divide(new BigDecimal(100)));
                assert order != null;
                shopPay.setOrderId(order.getOrderId());
                shopPay.setUserId(order.getUserId());
                shopPay.setStatus(-1);
                shopPay.setType(2);
                shopPayDao.save(shopPay);
                log.error("ERROR做异常订单处理");
            }
        }

    }

    private void updateLog(int status, int isOrder, int userType, int orderId) {
        Date time = new Date();
        if (isOrder != 1) {
            logDao.updateByPublishIdAndStatus(orderId, status, time, userType);
        }
        if (isOrder == 1) {
            logDao.updateByOrderIdAndStatus(orderId, status, time, userType);
        }
    }
}
