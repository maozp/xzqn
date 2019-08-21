package com.gangukeji.xzqn.controller.xzqn.other;


import com.gangukeji.xzqn.dao.ServiceVideosDao;
import com.gangukeji.xzqn.entity.*;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.From;
import java.util.HashMap;

/**
 * @author mao
 * @扣扣：1223480339
 * @date 2019/8/12 13:47
 */
@RestController
@RequestMapping("/videos")
//跨域的注解
@CrossOrigin("*")
public class ServiceVideosController {

    //不能使用Gson gson=new Gson() 放在服务器会报错，时间格式问题
    Gson builderTime = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    @Autowired
    ServiceVideosDao serviceVideosDao;
    @Value("${server.url}")
    private String url;

    //提交视频上传相关内容表单
    @PostMapping("/submit")
    public Result formSubmit(@RequestBody String data) throws Exception{

        XzqnServiceVideos videos = builderTime.fromJson(data, XzqnServiceVideos.class);
        Integer videoId = serviceVideosDao.save(videos).getId();
        HashMap<Object, Object> map = new HashMap<>();
        map.put("id", videoId);
        //与上面 42-44代码 功能一样
//      videos.setVideoname(videos.getVideoname());
//      videos.setVideodes(videos.getVideodes());
//      videos.setVideotype(videos.getVideotype());
//      videos.setVideodate(videos.getVideodate());
//      videos.setVideoaddr(videos.getVideoaddr());
//        serviceVideosDao.save(videos);
        return ResultUtils.success(200,"视频表单提交成功",videos);
    }

    //查询所有视频
    @PostMapping("/findAll/{page}")
    public Result videosFind(@PathVariable("page") int page)throws Exception{
       // Pageable pageable = new PageRequest(page, size);//2.0版本后,该方法已过时
        Sort sort = new Sort(Sort.Direction.DESC, "videodate");
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<XzqnServiceVideos> sendUsers = serviceVideosDao.findAll(pageable);
        return ResultUtils.success(200, "查询视频列表信息成功", sendUsers);
    }

    //修改视频信息
    @PostMapping("/update")
    public Result videosUpdate(@RequestBody String data,@RequestParam(value="id") Integer videoId) throws Exception{

        XzqnServiceVideos videos=serviceVideosDao.findById(videoId).get();
        videos = builderTime.fromJson(data, XzqnServiceVideos.class);
        serviceVideosDao.save(videos);
        return ResultUtils.success(200,"修改视频信息成功",videos);
    }

    //删除视频
    @PostMapping("/delete")
    public Result videosDelete(@RequestParam(value="id") Integer videoId) throws Exception{
        XzqnServiceVideos videos=serviceVideosDao.findById(videoId).get();
        serviceVideosDao.delete(videos);
        return ResultUtils.success(200,"删除视频成功",videos);
    }

}
