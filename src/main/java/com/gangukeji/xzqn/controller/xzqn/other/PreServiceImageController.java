package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.dao.PreServiceImageDao;
import com.gangukeji.xzqn.entity.XzqnPreServiceImage;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: hx
 * @Date: 2019/7/18 17:41
 * @Description:
 */
@RestController
public class PreServiceImageController {
    @Autowired
    PreServiceImageDao preServiceImageDao;
    @PostMapping("service/order/findImg")
    public Result findByOrderId(@RequestBody Map<String, Integer> map) {
        Integer orderId = map.get("orderId");
        XzqnPreServiceImage preServiceImage = preServiceImageDao.findByOrderId(orderId);
        return ResultUtils.success(200,"查询成功",preServiceImage);
    }
}
