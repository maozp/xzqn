package com.gangukeji.xzqn.controller.shop;

import com.gangukeji.xzqn.dao.ShopProductCommentDao;
import com.gangukeji.xzqn.entity.shop.XzqnShopProductComment;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.Utils;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/5 18:18
 * @Description: 商品购买后评论的添加
 */
@Deprecated
@RestController
@RequestMapping("shop/comment")
public class ShopCommentController {
    @Resource
    private ShopProductCommentDao commentDao;

    /**
     * 增
     */
    @PostMapping({"add"})
    private Result addObject(@RequestBody XzqnShopProductComment object) {
        XzqnShopProductComment save = commentDao.save(object);
        return ResultUtils.success(200, "添加object成功", save);
    }

    /**
     * 删
     */
    @PostMapping({"delete"})
    private Result deleteObject(@RequestBody String data) {
        Integer objectId = new JsonParser().parse(data).getAsJsonObject().get("objectId").getAsInt();
        try {
            commentDao.deleteById(objectId);
        } catch (Exception e) {
            return ResultUtils.error(-1, "not exist");
        }
        return ResultUtils.success(200, "object删除成功", objectId);
    }

    /**
     * 改
     */
    @PostMapping({"update"})
    private Result updateObject(@RequestBody XzqnShopProductComment object) {
        XzqnShopProductComment save = new XzqnShopProductComment();
        Utils.copyPropertiesIgnoreNull(object, save);
        save = commentDao.save(object);
        return ResultUtils.success(200, "添加object成功", save);
    }

    /**
     * 查 id
     */
    @PostMapping({"findById"})
    private Result findByIdObject(@RequestBody String data) {
        Integer objectId = new JsonParser().parse(data).getAsJsonObject().get("objectId").getAsInt();
        XzqnShopProductComment object;
        try {
            object = commentDao.findById(objectId).get();
        } catch (Exception e) {
            return ResultUtils.error(-1, "not exist");
        }
        return ResultUtils.success(200, "objectId查找成功", object);
    }

    /**
     * 查 all
     */
    @PostMapping({"findAll"})
    private Result findAllObject() {
        List<XzqnShopProductComment> objectList = commentDao.findAll();
        return ResultUtils.success(200, "object查找所有成功", objectList);
    }
}
