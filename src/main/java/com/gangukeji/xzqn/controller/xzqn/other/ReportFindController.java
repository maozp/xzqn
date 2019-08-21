package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.ReceiveReportDao;
import com.gangukeji.xzqn.entity.XzqnReceiveReport;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: hx
 * @Date: 2019/6/18 14:37
 * @Description:
 */
@RestController
@RequestMapping
public class ReportFindController {
    @Resource
    ReceiveReportDao reportDao;

    @Log
    @RequestMapping({"service/report/findById", "service/report/findByOrderId", "report/findByOrderId"})
    public Result findById(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int orderId = jsonObject.get("orderId").getAsInt();
        XzqnReceiveReport report = reportDao.findTopByOrderIdOrderByIdDesc(orderId);
        return ResultUtils.success(200, "查询成功", report);

    }
}
