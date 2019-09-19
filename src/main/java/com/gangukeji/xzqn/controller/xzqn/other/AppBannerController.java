package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.dao.AppBannerDao;
import com.gangukeji.xzqn.entity.XzqnAppBanner;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("app")
public class AppBannerController {

    @Autowired
    AppBannerDao appBannerDao;

    @PostMapping("/banner")
    public Result banner(@RequestBody String data){
        //解析多张图片，把多张图片地址数组中间加@成为字符串存入skillCertificate
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("img").getAsJsonArray();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> imgs= new Gson().fromJson(jsonArray, type);
        StringBuilder stringBuilder = new StringBuilder();
        imgs.forEach(img -> stringBuilder.append(img).append("@"));
        XzqnAppBanner xzqnAppBanner=new XzqnAppBanner();
        xzqnAppBanner.setImg(stringBuilder.toString());
        appBannerDao.save(xzqnAppBanner);
        return ResultUtils.success(200,"查看banner成功",xzqnAppBanner);
    }

    @PostMapping("/updateBanner")
    public Result updateBanner(@RequestBody String data){
        //解析多张图片，把多张图片地址数组中间加@成为字符串存入skillCertificate
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("img").getAsJsonArray();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> imgs= new Gson().fromJson(jsonArray, type);
        StringBuilder stringBuilder = new StringBuilder();
        imgs.forEach(img -> stringBuilder.append(img).append("@"));
        XzqnAppBanner xzqnAppBanner=appBannerDao.findById(1).get();
        xzqnAppBanner.setImg(stringBuilder.toString());
        appBannerDao.save(xzqnAppBanner);
        HashMap<String,Object> map=new HashMap<>();
        map.put("img",xzqnAppBanner.getImg().split("@"));
        return ResultUtils.success(200,"修改banner成功",map);
    }

    @PostMapping("/findBanner")
    public Result findBanner(){
        XzqnAppBanner xzqnAppBanner=appBannerDao.findById(1).get();
        HashMap<String,Object> map=new HashMap<>();
        map.put("img",xzqnAppBanner.getImg().split("@"));
        return ResultUtils.success(200,"查看banner成功",map);
    }


}
