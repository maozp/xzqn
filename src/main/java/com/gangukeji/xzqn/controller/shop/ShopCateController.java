package com.gangukeji.xzqn.controller.shop;

import com.gangukeji.xzqn.dao.ShopCateDao;
import com.gangukeji.xzqn.entity.view.BigCate;
import com.gangukeji.xzqn.entity.view.SmallCate;
import com.gangukeji.xzqn.entity.shop.XzqnShopCate;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.Utils;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/5 18:18
 * @Description: 商城分类的添加
 */
@RestController
@RequestMapping("shop/cate")
public class ShopCateController {
    @Resource
    private ShopCateDao cateDao;

    /**
     * 增
     */
    @PostMapping({"add"})
    private Result addCate(@RequestBody XzqnShopCate object) {
        XzqnShopCate save = cateDao.save(object);
        return ResultUtils.success(200, "添加object成功", save);
    }

    /**
     * 删
     */
    @PostMapping({"delete"})
    private Result deleteCate(@RequestBody String data) {
        Integer cateId = new JsonParser().parse(data).getAsJsonObject().get("cateId").getAsInt();
        try {
            cateDao.deleteById(cateId);
        } catch (Exception e) {
            return ResultUtils.error(-1, "not exist");
        }
        return ResultUtils.success(200, "object删除成功", cateId);
    }

    /**
     * 改
     */
    @PostMapping({"update"})
    private Result updateCate(@RequestBody XzqnShopCate object) {
        XzqnShopCate save = new XzqnShopCate();
        Utils.copyPropertiesIgnoreNull(object, save);
        save = cateDao.save(object);
        return ResultUtils.success(200, "添加object成功", save);
    }

    /**
     * 查 id
     */
    @PostMapping({"findById"})
    private Result findByIdCate(@RequestBody String data) {
        Integer cateId = new JsonParser().parse(data).getAsJsonObject().get("cateId").getAsInt();
        XzqnShopCate object;
        try {
            object = cateDao.findById(cateId).get();
        } catch (Exception e) {
            return ResultUtils.error(-1, "not exist");
        }
        return ResultUtils.success(200, "objectId查找成功", object);
    }

    /**
     * 查 all
     */
    @PostMapping({"findAll"})
    private Result findAllCate() {
        List<BigCate> result = new ArrayList<>();
        List<XzqnShopCate> cates = cateDao.findAll();
        ArrayList<XzqnShopCate> cateParentList = new ArrayList<>();
        ArrayList<XzqnShopCate> cateSonList = new ArrayList<>();
        for (XzqnShopCate cate : cates) {
            cate.setCreateTime(null);
            cate.setUpdateTime(null);
            //找出所有父节点
            if (cate.getParentId() == 0) {
                cateParentList.add(cate);
            }
            //找出所有子节点
            else {
                cateSonList.add(cate);
            }
        }
        for (XzqnShopCate cateP : cateParentList) {
            List<SmallCate> smallCates = new ArrayList<>();
            for (XzqnShopCate cate : cateSonList) {
                if (cate.getParentId() == cateP.getCateId()) {
                    SmallCate smallCate = SmallCate.builder().build();
                    smallCate.setId(cate.getCateId());
                    smallCate.setName(cate.getCateName());
                    smallCates.add(smallCate);
                }
            }
            result.add(BigCate.builder()
                    .id(cateP.getCateId())
                    .bigCate(cateP.getCateName())
                    .smallCates(smallCates)
                    .build());
        }
//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        Type type = new TypeToken<ArrayList<Integer>>() {
//        }.getType();
//        Object o = new Gson().fromJson( "[1,2,3,4,5,6]", type);

        return ResultUtils.success(200, "object查找所有成功", result);
    }
}
