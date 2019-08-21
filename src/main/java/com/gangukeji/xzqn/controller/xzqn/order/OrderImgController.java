package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.dao.OrderImgDao;
import com.gangukeji.xzqn.entity.XzqnOrderImg;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: hx
 * @Date: 2019/7/30 11:04
 * @Description:
 */
@RestController
public class OrderImgController {
    @Autowired
    OrderImgDao orderImgDao;
    @RequestMapping("order/img/findByPublishId")
    public Result findByPublishId(@RequestBody Map<String,Integer> map) {
        Integer publishId = map.get("publishId");
        XzqnOrderImg orderImg =orderImgDao.findByPublishId(publishId);
        if (orderImg == null) {
            return ResultUtils.error(-1,"图片不存在");
        }
        return ResultUtils.success(200,"查询图片成功",orderImg.getImgs().split("@"));
    }
    @RequestMapping("order/img/findByOrderId")
    public Result findByOrderId(@RequestBody Map<String,Integer> map) {
        Integer orderId = map.get("orderId");
        XzqnOrderImg orderImg =orderImgDao.findByOrderId(orderId);
        if (orderImg == null) {
            return ResultUtils.error(-1,"图片不存在");
        }
        return ResultUtils.success(200,"查询图片成功",orderImg.getImgs().split("@"));
    }
}
