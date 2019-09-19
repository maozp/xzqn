package com.gangukeji.xzqn.controller.xzqn.order;

import com.gangukeji.xzqn.dao.ServicePublishDao;
import com.gangukeji.xzqn.entity.XzqnServicePublish;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: hx
 * @Date: 2019/7/23 10:19
 * @Description: 用于后台管理系统审核订单与查询
 * publish更新isCheck字段
 * pageDefault 查询
 */
@RestController
@CrossOrigin("*")
public class PublishController {
    @Autowired
    ServicePublishDao publishDao;

    @PostMapping("publish/sort/find")
    public Result findAll(@RequestParam("page") int page) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<XzqnServicePublish> all = publishDao.findAllByIsCheck1(pageable);
        return ResultUtils.success(200, "publish查找成功", all);
    }

    @PostMapping("publish/auth/update")
    public Result updateIsCheck(@RequestBody Map<String, Object> map) {
        int count = 0;
        Integer isCheck = (Integer) map.get("isCheck");
        Integer publishId = (Integer) map.get("publishId");
        if (isCheck == -1) {
            String refuseReason = (String) map.get("refuseReason");
            count = publishDao.updateIsCheckAndRefuseReasonByPublishId(isCheck, refuseReason, publishId);
        } else {
            count = publishDao.updateIsCheckByPublishId(isCheck, publishId);
        }
        return ResultUtils.success(200, "更新成功", count);
    }

    /**
     * 行锁
     * @param map
     * @return
     */
//    @PostMapping("publish/row/lock")
//    public Object rowLock(@RequestBody Map<String, Object> map) {
////        Integer publishId = (Integer) map.get("publishId");
////        XzqnServicePublish lockPublish = publishDao.findByIdLock(publishId);
////        lockPublish.setIsCheck(10);
////        return publishDao.save(lockPublish);
//        int count = 0;
//        Integer isCheck = (Integer) map.get("isCheck");
//        Integer publishId = (Integer) map.get("publishId");
//        if (isCheck == -1) {
//            String refuseReason = (String) map.get("refuseReason");
//            count = publishDao.updateIsCheckAndRefuseReasonByPublishId(isCheck, refuseReason, publishId);
//        } else {
//            count = publishDao.updateIsCheckByPublishId(isCheck, publishId);
//        }
//        return ResultUtils.success(200, "更新成功", count);
//    }
}
