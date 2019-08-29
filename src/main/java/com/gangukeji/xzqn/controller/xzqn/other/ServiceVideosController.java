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
import java.util.Date;
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
    private String msg;
    Date now = new Date();

    /**
     * 后台接口
     * @param data
     * @return
     * @throws Exception
     */
    //提交视频上传相关内容表单
    @PostMapping("/submit")
    public Result formSubmit(@RequestBody String data) throws Exception{

        XzqnServiceVideos videos = builderTime.fromJson(data, XzqnServiceVideos.class);
        videos.setIsCollect(0);
        videos.setIsLike(0);
        videos.setLikeNums(0);
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

    /**
     * app端接口
     * @return
     */

    //查看收藏的视频列表并按照收藏时间先后排序
    @PostMapping("/videosCollectFind")
    public Result findCollectVideos(){
        return  ResultUtils.success(200,"查询收藏的视频成功",serviceVideosDao.findByCollectAndTime());
    }

    //查询所有视频
    @PostMapping("/videosFindAll")
    public Result findAllVideos(){
        return ResultUtils.success(200,"查看视频列表成功",serviceVideosDao.findAllOrderByVideosDate());
    }

    //查询单个视频
    @PostMapping("/videosFind")
    public Result findVideos(@RequestParam(value="id") Integer id){
        return ResultUtils.success(200,"查询视频成功",serviceVideosDao.findById(id));
    }


    //收藏视频接口
    @PostMapping("/collectVideo")
    public Result collectVideos(@RequestParam(value="id") Integer videoId) throws Exception{
        XzqnServiceVideos videos=serviceVideosDao.findById(videoId).get();
        if(videos.getIsCollect()==0){
            videos.setIsCollect(1);
            videos.setCollectTime(now);
            msg="收藏成功";
        }else {
            videos.setIsCollect(0);
            videos.setCollectTime(null);
            msg="取消收藏成功";
        }
        serviceVideosDao.save(videos);
        return ResultUtils.success(200,msg,"");
    }
    //点赞视频接口
    @PostMapping("/likeVideo")
    public Result likeVideos(@RequestParam(value="id") Integer videoId) throws Exception{
        XzqnServiceVideos videos=serviceVideosDao.findById(videoId).get();
        if(videos.getIsLike()==0){
            videos.setIsLike(1);
            videos.setLikeTime(now);
            videos.setLikeNums(videos.getLikeNums()+1);
            msg="点赞成功";
        }else {
            videos.setIsLike(0);
            videos.setLikeTime(null);
            videos.setLikeNums(videos.getLikeNums()-1);
            msg="取消点赞成功";
        }
        serviceVideosDao.save(videos);
        return ResultUtils.success(200,msg,"");
    }

}
