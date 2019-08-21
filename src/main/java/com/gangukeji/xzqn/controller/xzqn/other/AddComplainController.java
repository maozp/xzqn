package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.ServiceComplainDao;
import com.gangukeji.xzqn.entity.XzqnServiceComplain;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Author: hx
 * @Date: 2019/6/22 15:37
 * @Description:
 */
@RequestMapping
@RestController
public class AddComplainController {
    Gson gson = new Gson();
    @Autowired
    ServiceComplainDao complainDao;
    /**
     * 发单用户举报接单用户
     */
    @PostMapping("serviceComplain/add")
    @Log("举报--添加")
    public Result addComplain(@RequestBody String data) {
        XzqnServiceComplain complain = gson.fromJson(data, XzqnServiceComplain.class);
        Integer complainId = complainDao.save(complain).getId();
        HashMap<Object, Object> map = new HashMap<>();
        map.put("complainId", complainId);
        return ResultUtils.success(200, "举报成功", map);
    }
}
