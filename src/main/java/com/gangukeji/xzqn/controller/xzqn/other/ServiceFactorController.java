package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.UserFactorDao;
import com.gangukeji.xzqn.entity.XzqnUserFactor;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/14 14:13
 * @Description:
 */
@RestController
@RequestMapping("service/factor")
public class ServiceFactorController {
    @Resource
    UserFactorDao factorDao;
    @Log
    @RequestMapping({"findAll"})
    public Result findById() {
        List<XzqnUserFactor> resp = factorDao.findAll();
        ArrayList<XzqnUserFactor> factorAll = new ArrayList<>(resp);
        resp.removeIf(e -> e.getParentId() != 0);
        resp.forEach(e -> {
            if (e.getParentId() == 0) {
                factorAll.forEach(e1 -> {
                    if (e.getFactorId().equals(e1.getParentId())) {
                        e.addFactor(e1);
                    }
                });

            }
        });
        return ResultUtils.success(200, "success", resp);
    }
}
