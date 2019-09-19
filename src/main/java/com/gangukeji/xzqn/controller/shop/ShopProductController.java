package com.gangukeji.xzqn.controller.shop;

import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnAuthSkill;
import com.gangukeji.xzqn.entity.XzqnNews;
import com.gangukeji.xzqn.entity.shop.*;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.Utils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/5 16:56
 * @Description:
 */
@RestController
@RequestMapping("shop/product")
@CrossOrigin("*")
public class ShopProductController {
    @Resource
    ShopProductInfoDao productDao;
    @Resource
    ShopOrderMasterDao masterDao;
    @Resource
    ShopOrderDetailDao detailDao;
    @Resource
    ShopCateDao cateDao;

    // 商品 添加|更新
    @PostMapping({"add", "update"})
    private Result productAdd(@RequestBody XzqnShopProductInfo product) {
        XzqnShopProductInfo save;
        if(product.getProductId()==null){
            save=productDao.save(product);
        }else{
            save = productDao.findById(product.getProductId()).orElse(product);
            Utils.copyPropertiesIgnoreNull(product, save);
            save = productDao.save(save);
        }
        return ResultUtils.success(200, "商品添加|更新成功", save);
    }

    //XzqnShopCate save = cateDao.save(object);
    // 商品 添加|更新
//    @PostMapping({"addV2"})
//    private Result productAdd2(@RequestBody String data) {
//
//        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
//        JsonArray jsonArray = jsonObject.get("des").getAsJsonArray();
//        Type type = new TypeToken<List<String>>() {
//        }.getType();
//        List<String> desc= new Gson().fromJson(jsonArray, type);
//        StringBuilder stringBuilder = new StringBuilder();
//        desc.forEach(des -> stringBuilder.append(des).append("@"));
//
//        JsonArray jsonArray2 = jsonObject.get("pic").getAsJsonArray();
//        List<String> pics= new Gson().fromJson(jsonArray2, type);
//        pics.forEach(pic -> stringBuilder.append(pic).append("@"));
//
//        JsonArray jsonArray3 = jsonObject.get("img").getAsJsonArray();
//        List<String> imgs= new Gson().fromJson(jsonArray3, type);
//        imgs.forEach(img -> stringBuilder.append(img).append("@"));
//
//
//        XzqnShopProductInfo shopProductInfo = new XzqnShopProductInfo();
//
//        return ResultUtils.success(200, "商品添加", save);
//    }

    // 商品 删除
    @PostMapping({"delete"})
    private Result productDelete(@RequestBody String data) {
        Integer productId = new JsonParser().parse(data).getAsJsonObject().get("productId").getAsInt();
        try {
            productDao.deleteById(productId);
        } catch (Exception e) {
            return ResultUtils.error(-1, "删除失败,产品id不存在");
        }
        return ResultUtils.success(200, "商品删除成功", productId);
    }


    //     商品 查全部
    @PostMapping({"findAll"})
    private Result productAll(@RequestBody String data) {
        Integer page = new JsonParser().parse(data).getAsJsonObject().get("page").getAsInt();
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<XzqnShopProductInfo> productList = productDao.findAll(pageable);
        return ResultUtils.success(200, "查找全部商品成功", productList);
    }

    //    cateid查找类名
    @PostMapping({"findAllV2"})
    private Result productAllV2(@RequestBody String data) {
        Integer page = new JsonParser().parse(data).getAsJsonObject().get("page").getAsInt();
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page, 5, sort);
        List<Object> productList = productDao.findAllandCateName();
        return ResultUtils.success(200, "查找全部商品成功", productList);
    }


    //商品 cateId查
    @PostMapping({"findByCateId"})
    private Result findByCateId(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int cateId = 0;
        PageRequest pageRequest = new PageRequest(0, 100);
        try {
            pageRequest = new PageRequest(jsonObject.get("page").getAsInt(), jsonObject.get("size").getAsInt());
        } catch (Exception e) {
            System.out.println("未分页");
        }
        try {
            cateId = jsonObject.get("cateId").getAsInt();
        } catch (Exception e) {
            return ResultUtils.error(-1, "请输入cateId");
        }
        Page<XzqnShopProductInfo> productList = productDao.findAllByCateId(cateId, pageRequest);
        return ResultUtils.success(200, "查找成功", productList.getContent());
    }

    //商品 cateIds查
    @PostMapping({"findByCateIds"})
    private Result findByCateIds(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        List<Integer> cateIdList;
        PageRequest pageRequest = new PageRequest(0, 100);
        try {
            pageRequest = new PageRequest(jsonObject.get("page").getAsInt(), jsonObject.get("size").getAsInt());
        } catch (Exception e) {
            System.out.println("未分页");
        }
        try {
            JsonArray jsonArray = jsonObject.get("cateIds").getAsJsonArray();
            Type type = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            cateIdList = new Gson().fromJson(jsonArray, type);
        } catch (Exception e) {
            return ResultUtils.error(-1, "请输入cateIdList数组");
        }
//        cateIdList.forEach(cateId -> productList.addAll(productDao.findAllByCateId(cateId)));
        Page<XzqnShopProductInfo> productList = productDao.findAllByCateIdIn(cateIdList, pageRequest);
        return ResultUtils.success(200, "查找成功", productList.getContent());
    }

    //商品 cateParentId查
    @PostMapping({"findByCateParentId", "findByParentCateId"})
    private Result findByCateParentId(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int cateParentId = 0;
        PageRequest pageRequest = new PageRequest(0, 100);
        try {
            pageRequest = new PageRequest(jsonObject.get("page").getAsInt(), jsonObject.get("size").getAsInt());
        } catch (Exception e) {
            System.out.println("未分页");
        }
        try {
            cateParentId = jsonObject.get("cateParentId").getAsInt();
        } catch (Exception e) {
            return ResultUtils.error(-1, "请输入cateParentId");
        }
        List<XzqnShopCate> cateList = cateDao.findAllByParentId(cateParentId);
        List<Integer> cateIdList = new ArrayList<>();
        cateList.forEach(p -> cateIdList.add(p.getCateId()));
//        cateIdList.forEach(i -> productList.addAll(productDao.findAllByCateId(i)));
        Page<XzqnShopProductInfo> productList = productDao.findAllByCateIdIn(cateIdList, pageRequest);
        return ResultUtils.success(200, "查找成功", productList.getContent());
    }

    // 商品 查id
    @PostMapping({"findById"})
    private Result productFindById(@RequestBody String data) {
        Integer productId = new JsonParser().parse(data).getAsJsonObject().get("productId").getAsInt();
        XzqnShopProductInfo product = null;
        try {
            product = productDao.findById(productId).get();
        } catch (Exception e) {
            return ResultUtils.error(-1, "查找失败商品id不存在" + productId);
        }
        return ResultUtils.success(200, "查找商品详情成功", product);
    }

    //商品 userId 去查
    @PostMapping({"findByUserId"})
    private Result productFindByUserId(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        PageRequest pageRequest = new PageRequest(0, 100);
        try {
            pageRequest = new PageRequest(jsonObject.get("page").getAsInt(), jsonObject.get("size").getAsInt());
        } catch (Exception e) {
            System.out.println("未分页");
        }
        Integer userId = 0;
        try {
            userId = jsonObject.get("userId").getAsInt();
        } catch (Exception e) {
            return ResultUtils.error(-1, "请传userId");
        }
        Page<XzqnShopProductInfo> productList = productDao.findAllByUserId(userId, pageRequest);
        return ResultUtils.success(200, "查找全部商品成功", productList.getContent());
    }

    //商品 立即购买
    @PostMapping({"buy"})
    private Result productBuy(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        Integer productId = jsonObject.get("productId").getAsInt();
        Integer count = jsonObject.get("productCount").getAsInt();
        //产生订单
        XzqnShopOrderMaster master = new Gson().fromJson(data, XzqnShopOrderMaster.class);
        master.setOrderStatus(1);
        master.setOrderNo("XZQN" + System.currentTimeMillis());
        master.setPaymentMethod(1);
        master.setExpressMoney(new BigDecimal(0));
        master.setInvoiceTitle(master.getUserName());
        master.setExpressName("顺丰");
        master.setExpressNo("EXPRESS" + System.currentTimeMillis());
        master.setReduceMoney(new BigDecimal(0));
        master.setPaymentMoney(new BigDecimal(0));
        //设置总金额
        XzqnShopProductInfo product = productDao.findById(productId).get();
        master.setOrderMoney(product.getPrice().multiply(new BigDecimal(count)));
        master = masterDao.save(master);
        Integer orderId = master.getOrderId();
        //产生订单详情
        List<XzqnShopOrderDetail> details = new ArrayList<>();
        XzqnShopOrderDetail detail = new XzqnShopOrderDetail();
        BeanUtils.copyProperties(product, detail);
        detail.setProductPrice(product.getPrice());
//        detail.setProductPrice(product.getPrice().multiply(new BigDecimal(count)));
        detail.setProductCount(count);
        detail.setOrderId(orderId);
        detail.setReduceMoney(new BigDecimal(0));
        details.add(detail);
        details = detailDao.saveAll(details);
        master.setDetails(details);
        return ResultUtils.success(200, "立即购买商品成功", master);
    }
}
