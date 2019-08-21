package com.gangukeji.xzqn.controller.shop;

import com.gangukeji.xzqn.dao.ShopOrderCartDao;
import com.gangukeji.xzqn.dao.ShopOrderDetailDao;
import com.gangukeji.xzqn.dao.ShopOrderMasterDao;
import com.gangukeji.xzqn.dao.ShopProductInfoDao;
import com.gangukeji.xzqn.entity.shop.XzqnShopOrderCart;
import com.gangukeji.xzqn.entity.shop.XzqnShopOrderDetail;
import com.gangukeji.xzqn.entity.shop.XzqnShopOrderMaster;
import com.gangukeji.xzqn.entity.shop.XzqnShopProductInfo;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/5 16:56
 * @Description: 商城购物车的增删改查
 */
@RestController
@RequestMapping("shop/cart")
public class ShopCartController {
    @Resource
    ShopOrderCartDao cartDao;
    @Resource
    ShopProductInfoDao productDao;
    @Resource
    ShopOrderMasterDao masterDao;
    @Resource
    ShopOrderDetailDao detailDao;

    //cart add|update 购物车 添加 更新  ok
    @PostMapping({"add", "update"})
    private Result cartAdd(@RequestBody XzqnShopOrderCart post) {
        //根据userId  productId寻找购物车  没有new  有就更改 设置 金额 数量<=0 要从购物车移除
        try {
            Integer productId = post.getProductId();
            Integer count = post.getProductCount();
            Integer userId = post.getUserId();
            post.setCartId(null);
            post = cartDao.findByUserIdAndProductId(userId, productId).orElse(post);
            if (count <= 0) {
                cartDao.deleteById(post.getCartId());
                post.setProductCount(0);
                return ResultUtils.success(200, "清空商品成功", post);
            }
            XzqnShopProductInfo product = productDao.findById(productId).get();
            post.setProductName(product.getProductName());
            post.setPic(product.getPic());
            post.setPrice(product.getPrice());
            post.setProductCount(count);
            cartDao.save(post);
        } catch (Exception e) {
            return ResultUtils.error(-1, "加减商品失败" + e.getMessage());
        }
        return ResultUtils.success(200, "加减商品成功", post);
    }

    //delete 删除 ok
    @PostMapping({"delete"})
    private Result cartDelete(@RequestBody String data) {
        //根据userId  productId寻找购物车  没有new  有就更改 设置 金额 数量<=0 要从购物车移除
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        try {
            JsonArray jsonArray = jsonObject.get("cartIdList").getAsJsonArray();
            Type type = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            List<Integer> cartIdList = new Gson().fromJson(jsonArray, type);
            cartDao.deleteByCartIdIn(cartIdList);
            return ResultUtils.success(200, "清空商品成功", cartIdList);
        } catch (Exception e) {
            return ResultUtils.error(-1, "清空商品失败");
        }

    }

    //购物车 + 1
    @PostMapping({"addOne"})
    private Result addOne(@RequestBody XzqnShopOrderCart post) {
        //根据userId  productId寻找购物车  没有new  有就更改 设置 金额 数量<=0 要从购物车移除
        try {
            Integer productId = post.getProductId();
            Integer userId = post.getUserId();
            XzqnShopProductInfo product = productDao.findById(productId).get();
            post.setCartId(null);
            XzqnShopOrderCart cart = new XzqnShopOrderCart();
            try {
                //购物车存在此商品
                cart = cartDao.findByUserIdAndProductId(userId, productId).get();
                cart.setProductName(product.getProductName());
                cart.setPic(product.getPic());
                cart.setProductCount(cart.getProductCount() + 1);
                cart.setPrice(product.getPrice());
                XzqnShopOrderCart save = cartDao.save(cart);
                return ResultUtils.success(200, "加1商品成功", save);
            } catch (Exception e) {
                //购物车不存在此商品
                cart.setUserId(userId);
                cart.setProductId(productId);
                cart.setProductName(product.getProductName());
                cart.setPic(product.getPic());
                cart.setProductCount(1);
                cart.setPrice(product.getPrice());
                XzqnShopOrderCart save = cartDao.save(cart);
                return ResultUtils.success(200, "加1商品成功", save);
            }
        } catch (Exception e) {
            return ResultUtils.error(-1, "加减商品失败" + e.getMessage());
        }
    }

    //cart findByUserId 查用户的购物车  ok
    @PostMapping("findByUserId")
    private Result cartFind(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        try {
            Integer userId = jsonObject.get("userId").getAsInt();
            List<XzqnShopOrderCart> cartList = cartDao.findByUserId(userId);
            return ResultUtils.success(200, "查找成功", cartList);
        } catch (Exception e) {
            return ResultUtils.error(-1, "请传userId");
        }
    }

    //    购物车 购买多个
    @PostMapping({"buy"})
    private Result productBuys(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("cartIdList").getAsJsonArray();
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        List<Integer> cartIdList = new Gson().fromJson(jsonArray, type);
        //获取购物车列表
        List<XzqnShopOrderCart> cartList = cartDao.findAllById(cartIdList);
        if (cartList.size() == 0) {
            return ResultUtils.error(-1, "找不到cartId对应的商品");
        }
        //前端输入
        XzqnShopOrderMaster master = new Gson().fromJson(data, XzqnShopOrderMaster.class);
        //删除购物车
        cartDao.deleteByCartIdIn(cartIdList);
        //产生订单
        master.setOrderStatus(1);
        master.setUserId(cartList.get(0).getUserId());
        master.setOrderNo("XZQN" + System.currentTimeMillis());
        master.setPaymentMethod(1);
        master.setInvoiceTitle(master.getUserName());
        master.setExpressMoney(new BigDecimal(0));
        master.setExpressName("顺丰");
        master.setExpressNo("EXPRESS" + System.currentTimeMillis());
        master.setReduceMoney(new BigDecimal(0));
        master.setPaymentMoney(new BigDecimal(0));
        master.setProductCount(cartList.size());
        //设置总金额 需要  购物车里商品价格*数量
        BigDecimal money = new BigDecimal(0);
        for (XzqnShopOrderCart cart : cartList) {
            money = money.add(cart.getPrice().multiply(new BigDecimal(cart.getProductCount())));
        }
        master.setOrderMoney(money);
        master = masterDao.save(master);
        Integer orderId = master.getOrderId();
        //产生订单详情
        List<XzqnShopOrderDetail> details = new ArrayList<>();
        for (XzqnShopOrderCart item : cartList) {
            XzqnShopOrderDetail detail = new XzqnShopOrderDetail();
            BeanUtils.copyProperties(item, detail);
            detail.setOrderId(orderId);
            detail.setProductPrice(item.getPrice());
            detail.setReduceMoney(new BigDecimal(0));
            details.add(detail);
        }
        details = detailDao.saveAll(details);
        master.setDetails(details);
        return ResultUtils.success(200, "购买商品成功", master);
    }
}
