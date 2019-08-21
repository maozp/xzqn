package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.ServiceCommentDao;
import com.gangukeji.xzqn.entity.XzqnServiceComment;
import com.gangukeji.xzqn.utils.ListToString;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/18 14:37
 * @Description:
 */
@RestController
@RequestMapping("service/comment")
public class CommentController {
    @Resource
    ServiceCommentDao commentDao;
    @Log
    @RequestMapping({"findById", "findByOrderId"})
    public Result findById(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int orderId = jsonObject.get("orderId").getAsInt();
        XzqnServiceComment comment = commentDao.findAllByOrderId(orderId, new PageRequest(0, 1, Sort.Direction.DESC, "id")).get(0);
        return ResultUtils.success(200, "查询成功", comment);
    }
    @PostMapping("add")
    @Log
    public Result addComment(@RequestBody String data) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
//        final int STATUS = jsonObject.get("status").getAsInt();
        XzqnServiceComment comment = new XzqnServiceComment();

        int orderId = jsonObject.get("orderId").getAsInt();
        int userId = jsonObject.get("userId").getAsInt();
        int grade = jsonObject.get("grade").getAsInt();
        String des = jsonObject.get("des").getAsString();
        //set orderId des  img userId grade
        comment.setOrderId(orderId);
        comment.setGrade(grade);
        comment.setContent(des);
        comment.setUserId(userId);
        try {
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            JsonArray jsonArray = jsonObject.getAsJsonArray("img");
            List<String> imgList = new Gson().fromJson(jsonArray, listType);
            comment.setImg(ListToString.goString(imgList, "@"));
        } catch (Exception e) {
            System.out.println(e.getMessage() + "图片未传捕获异常");
        }
        XzqnServiceComment save = commentDao.save(comment);
//        orderDao.updateOrderStatus(STATUS + 1, orderId);
//        updateLog(STATUS + 1, 1, 1, orderId);
        return ResultUtils.success(200, "评论回单add ok", save);
    }
}
