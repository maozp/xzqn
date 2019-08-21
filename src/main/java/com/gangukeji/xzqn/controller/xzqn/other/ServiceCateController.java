package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.ServiceCateDao2;
import com.gangukeji.xzqn.dao.ServicePropDao2;
import com.gangukeji.xzqn.entity.XzqnServiceCate2;
import com.gangukeji.xzqn.entity.XzqnServiceCatePure;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.StatusUtils2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/14 14:13
 * @Description:
 */
@RestController
@RequestMapping
public class ServiceCateController {
    @Autowired
    ServiceCateDao2 cateDao2;
    @Autowired
    ServicePropDao2 propDao2;

    @RequestMapping({"service/cate/findAll"})
    @Log
    public Result findById() {
        return ResultUtils.success(200, "success", StatusUtils2.cate2List.get(0));
    }
    @RequestMapping({"flush"})
    @Log
    public Result flush() {
        StatusUtils2.cate2List.clear();
        StatusUtils2.prop2List.clear();
        StatusUtils2.cate2List.addAll(cateDao2.findAll());
        StatusUtils2.prop2List.addAll(propDao2.findAll());
        return ResultUtils.success(200, "success", "ok");
    }

    @RequestMapping("serviceCate/findSmallCate")
    @Log
    public Result findSmallCate() {
        List<XzqnServiceCate2> cateList = cateDao2.findAll();
        List<XzqnServiceCatePure> cateListPure = new ArrayList<>();
        cateList.forEach(e -> {
            cateListPure.add(new XzqnServiceCatePure(e.getCateId(), e.getIsParent(), e.getName(), e.getFee(), false, e.getDel()));
        });
        cateListPure.removeIf(e -> e.getDel() != 3);

        return ResultUtils.success(200, "cateListLv3.size:" + cateListPure.size(), cateListPure);
    }
}
