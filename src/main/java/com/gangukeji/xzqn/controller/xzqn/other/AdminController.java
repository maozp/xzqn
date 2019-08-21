package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.dao.AdminDao;
import com.gangukeji.xzqn.entity.XzqnAdmin;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: hx
 * @Date: 2019/7/18 11:47
 * @Description:
 */
@RequestMapping("admin")
@RestController
@CrossOrigin("*")
public class AdminController {
    @Autowired
    AdminDao adminDao;

    @PostMapping("login")
    public Result login(@RequestBody XzqnAdmin data) {
        XzqnAdmin admin = adminDao.findByUsername(data.getUsername());
        if (admin.getPassword().equals(data.getPassword())) {
            return ResultUtils.success(200, "登录成功", admin);
        } else {
            return ResultUtils.error(-1,"登录失败");
        }
    }
}
