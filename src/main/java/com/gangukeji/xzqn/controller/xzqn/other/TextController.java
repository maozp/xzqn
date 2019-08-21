package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.dao.HelpDao;
import com.gangukeji.xzqn.dao.ShopPayDao;
import com.gangukeji.xzqn.dao.TextDao;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: hx
 * @Date: 2019/6/29 10:41
 * @Description:
 */
@RestController
@RequestMapping
public class TextController {
    @Autowired
    TextDao textDao;
    @Autowired
    HelpDao helpDao;
    @Autowired
    ShopPayDao shopPayDao;

    @RequestMapping("sendAgreement")
    public String sendAgreement() {
        return textDao.findById(1).get().getText();
    }

    @RequestMapping("receiveAgreement")
    public String receiveAgreement() {
        return textDao.findById(2).get().getText();
    }

    @RequestMapping("exists")
    public Object exists(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        return shopPayDao.existsByTypeAndOrderId(jsonObject.get("type").getAsInt(), jsonObject.get("orderId").getAsInt());
    }
    @RequestMapping("help")
    public Object help() {
        return helpDao.findAll();
    }
}
