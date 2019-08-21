package com.gangukeji.xzqn.controller.xzqn.other;


import com.gangukeji.xzqn.entity.AuthSignLog;
import com.gangukeji.xzqn.service.AuthSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sign")
//@Api(tags = {"用户签到打卡API"}
public class AuthSignController {
    private final AuthSignService authSignService;



    @Autowired
    public AuthSignController(AuthSignService authSignService) {
        this.authSignService = authSignService;
    }

    //@ApiOperation(value = "用户签到")
    @PostMapping("/createSign")
    public List<AuthSignLog> createSign(@RequestParam Integer authId) {
        //首先要初始化数据
        authSignService.init(authId);
        authSignService.createSign(authId);
        return authSignService.data(authId);
    }


    //@ApiOperation(value = "打卡数据")
    @GetMapping("/data")
    public List<AuthSignLog> data(@RequestParam Integer authId) {
        authSignService.init(authId);
        return authSignService.data(authId);
    }

    //@ApiOperation(value = "判断今天是否已经打过卡")
    @GetMapping("/isOrSign")
    public Boolean isOrSign(@RequestParam Integer authId) {
        return authSignService.isOrSign(authId);
    }
    
}
