package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.dao.SmsDao;
import com.gangukeji.xzqn.entity.XzqnSms;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.SmsUtilAli;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @Author: hx
 * @Date: 2019/7/23 16:41
 * @Description:
 */
@RestController
@Slf4j
public class SmsController {
    @Autowired
    private SmsDao smsDao;

    Date now=new Date();
    @PostMapping("/sendCode")
    public Result sendCode(@RequestBody Map<String, String> map) throws Exception {
        String phoneNum = map.get("phone");
        String sendCode = SmsUtilAli.send(phoneNum);
        XzqnSms xzqnSms=smsDao.findByPhone(phoneNum);
        if(xzqnSms==null){
            xzqnSms = smsDao.save(new XzqnSms( phoneNum, sendCode));
            xzqnSms.setPhone(phoneNum);
            xzqnSms.setCode(sendCode);
            xzqnSms.setCreateTime(now);
            smsDao.save(xzqnSms);
        }else{
            xzqnSms.setCode(sendCode);
            xzqnSms.setCreateTime(now);
            smsDao.save(xzqnSms);
        }

        return ResultUtils.success(200, "发送成功", "");
    }

    @PostMapping("/findCode")
    public Object findCode(@RequestBody Map<String, String> map) {
        String phoneNum = map.get("phone");
        XzqnSms xzqnSms = smsDao.findTop1ByPhoneOrderByIdDesc(phoneNum);
        if (xzqnSms == null) {
            xzqnSms = new XzqnSms();
            xzqnSms.setPhone(phoneNum);
            return ResultUtils.success(200, "查询成功验证码失效", xzqnSms);
        }
        return ResultUtils.success(200, "查询成功", xzqnSms);
    }

//    @PostMapping("/findCodeTest")
//    public Object findCodeFirst(@RequestBody Map<String, String> map) {
//        String phoneNum = map.get("phone");
//        return ResultUtils.success(200, "查询成功", smsDao.deleteByPhone(phoneNum));
//    }
}
