package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.dao.MessageAdvice;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MessageAdviceController {

    @Autowired
    MessageAdvice messageAdvice;

    @PostMapping("/MessageAdvice")
    public Result messageAdvice(){
        return ResultUtils.success(200, "消息查询成功", messageAdvice.findAll());
    }
}
