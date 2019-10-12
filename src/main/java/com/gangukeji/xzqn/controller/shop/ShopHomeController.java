package com.gangukeji.xzqn.controller.shop;

import com.gangukeji.xzqn.dao.AppBannerDao;
import com.gangukeji.xzqn.dao.ShopCateDao;
import com.gangukeji.xzqn.dao.ShopProductInfoDao;
import com.gangukeji.xzqn.entity.XzqnAppBanner;
import com.gangukeji.xzqn.entity.shop.XzqnShopCate;
import com.gangukeji.xzqn.entity.shop.XzqnShopProductInfo;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: hx
 * @Date: 2019/6/6 11:58
 * @Description: 商城首页数据
 */
@RestController
@RequestMapping("shop/home")
public class ShopHomeController {
    @Resource
    ShopProductInfoDao productDao;
    @Resource
    ShopCateDao cateDao;
    @Autowired
    AppBannerDao appBannerDao;

    /**
     * 初始化新品数据
     * @return
     */
    @PostMapping({"newProduct"})
    private Result newProduct() {
        List<XzqnShopProductInfo> lists = null;
        try {
            lists = initNewProducts();
        } catch (Exception e) {
            return ResultUtils.error(-1, "商品不够");
        }
        return ResultUtils.success(200,"新品列表获取成功",lists);
    }

    /**
     * 初始化5个新品
     * @return
     */
    private List<XzqnShopProductInfo> initNewProducts() {
        Page<XzqnShopProductInfo> pages = productDao.findAll(new PageRequest(0, 5, Sort.Direction.DESC,"productId"));
        return pages.getContent();
    }

    /**
     * 商品分类
     * @return
     */
    @PostMapping({"cate"})
    private Result cate() {
        List<XzqnShopCate> cateList;
        try {
            cateList = cateDao.findAllByParentId(0);
        } catch (Exception e) {
            return ResultUtils.error(-1, "分类不够");
        }
        //返回9个品类
        return ResultUtils.success(200,"首页品类获取成功",cateList.subList(0,9));
    }

    /**
     * 轮播图设置,资源放在apache/xzqn下面
     * @return
     */
//    @PostMapping({"recycle"})
//    private Result recycle() {
//        HashMap<String, List> resp = new HashMap<>();
//        HashMap<String, Object> item1 = new HashMap<>();
//        item1.put("url", "https://www.xiaozheng8.com/banner1");
//        item1.put("banner", "http://www.xiaozheng8.com/xzqn/shop/recycle1.png");
//        HashMap<String, Object> item2 = new HashMap<>();
//        item2.put("url", "https://www.xiaozheng8.com/banner2");
//        item2.put("banner", "http://www.xiaozheng8.com/xzqn/shop/recycle2.png");
//        HashMap<String, Object> item3 = new HashMap<>();
//        item3.put("url", "https://www.xiaozheng8.com/banner3");
//        item3.put("banner", "http://www.xiaozheng8.com/xzqn/shop/recycle3.png");
//        ArrayList<Map> bannerList = new ArrayList<>();
//        bannerList.add(item1);
//        bannerList.add(item2);
//        bannerList.add(item3);
//        resp.put("bannerList", bannerList);
//        return ResultUtils.success(200,"轮播图获取成功",resp);
//    }

    //小程序不想改重新上线，只能这么写，不然轮播图不能显示图片
    @PostMapping({"recycle"})
     private Result recycle() {
        XzqnAppBanner xzqnAppBanner=appBannerDao.findById(2).get();
        HashMap<String, String> map=new HashMap<>();
        HashMap<String, List> resp = new HashMap<>();
        ArrayList<Map> bannerList = new ArrayList<>();
        try {
            HashMap<String, Object> item1 = new HashMap<>();
            item1.put("banner",xzqnAppBanner.getImg().split("@")[0]);
            HashMap<String, Object> item2 = new HashMap<>();
            item2.put("banner",xzqnAppBanner.getImg().split("@")[1]);
            HashMap<String, Object> item3 = new HashMap<>();
            item3.put("banner",xzqnAppBanner.getImg().split("@")[2]);
            bannerList.add(item1);
            bannerList.add(item2);
            bannerList.add(item3);
            resp.put("bannerList",bannerList);
        }catch (Exception e){
            return ResultUtils.error(-1,"请上传三张轮播图片");
        }
        return ResultUtils.success(200,"查看banner成功",resp);
    }
}
