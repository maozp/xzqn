package com.gangukeji.xzqn.controller.xzqn.other;


import com.gangukeji.xzqn.dao.ServiceVideoLikeDao;
import com.gangukeji.xzqn.dao.ServiceVideoLogDao;
import com.gangukeji.xzqn.dao.ServiceVideosDao;
import com.gangukeji.xzqn.entity.*;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.From;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private String rmsg;
    Date now = new Date();

    @Resource
    ServiceVideoLogDao videoLogDao;

    @Resource
    ServiceVideoLikeDao videoLikeDao;

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
        videos.setLikeNums(0);
        videos.setVideoReadNums(0);
        Integer videoId = serviceVideosDao.save(videos).getId();
        HashMap<Object, Object> map = new HashMap<>();
        map.put("videoId", videoId);
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
    @PostMapping("/findAll")
    public Result videosFind(@RequestBody String data)throws Exception{
       // Pageable pageable = new PageRequest(page, size);//2.0版本后,该方法已过时
        Integer page = new JsonParser().parse(data).getAsJsonObject().get("page").getAsInt();
        Sort sort = new Sort(Sort.Direction.DESC, "videoDate");
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<XzqnServiceVideos> sendUsers = serviceVideosDao.findAll(pageable);
        return ResultUtils.success(200, "查询视频列表信息成功", sendUsers);
    }

    //修改视频信息
    @PostMapping("/update")
    public Result videosUpdate(@RequestBody String data) throws Exception{

        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer videoId=jsonObject.get("videoId").getAsInt();
        XzqnServiceVideos videos=serviceVideosDao.findById(videoId).get();
        videos = builderTime.fromJson(data, XzqnServiceVideos.class);
        serviceVideosDao.save(videos);
        return ResultUtils.success(200,"修改视频信息成功",videos);
    }

    //删除视频
    @PostMapping("/delete")
    public Result videosDelete(@RequestBody String data) throws Exception{
        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer videoId=jsonObject.get("videoId").getAsInt();
        XzqnServiceVideos videos=serviceVideosDao.findById(videoId).get();
        serviceVideosDao.delete(videos);
        return ResultUtils.success(200,"删除视频成功",videos);
    }

    /**
     * app端接口
     * @return
     */

//    //查看收藏的视频列表并按照收藏时间先后排序
//    @PostMapping("/videosCollectFind")
//    public Result findCollectVideos(){
//        return  ResultUtils.success(200,"查询收藏的视频成功",serviceVideosDao.findByCollectAndTime());
//    }

    //查询所有视频
    @PostMapping("/videosFindAll")
    public Result findAllVideos(@RequestBody String data) throws  Exception {
        Integer page = new JsonParser().parse(data).getAsJsonObject().get("page").getAsInt();
        Sort sort = new Sort(Sort.Direction.DESC, "videoDate");
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<XzqnServiceVideos> sendUsers = serviceVideosDao.findAll(pageable);
        return ResultUtils.success(200, "查询视频列表信息成功", sendUsers);
    }
    //根据人气查询所有视频
    @PostMapping("/videosReadFindAll")
    public Result findAllVideosRead(@RequestBody String data) throws  Exception {
        Integer page = new JsonParser().parse(data).getAsJsonObject().get("page").getAsInt();
        Sort sort = new Sort(Sort.Direction.DESC, "videoReadNums");
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<XzqnServiceVideos> sendUsers = serviceVideosDao.findAll(pageable);
        return ResultUtils.success(200, "查询视频列表信息成功", sendUsers);
    }
    //查询单个视频
    @PostMapping("/videosFind")
    public Result findVideos(@RequestBody String data){
        Integer id = new JsonParser().parse(data).getAsJsonObject().get("id").getAsInt();
        return ResultUtils.success(200,"查询视频成功",serviceVideosDao.findById(id));
    }


//    //收藏视频接口
//    @PostMapping("/collectVideo")
//    public Result collectVideos(@RequestBody String data) throws Exception{
//        Integer id = new JsonParser().parse(data).getAsJsonObject().get("id").getAsInt();
//        XzqnServiceVideos videos=serviceVideosDao.findById(id).get();
//        if(videos.getIsCollect()==0){
//            videos.setIsCollect(1);
//            videos.setCollectTime(now);
//            msg="收藏成功";
//        }else {
//            videos.setIsCollect(0);
//            videos.setCollectTime(null);
//            msg="取消收藏成功";
//        }
//        serviceVideosDao.save(videos);
//        return ResultUtils.success(200,msg,"");
//    }

    //查看收藏的视频列表并按照收藏时间先后排序
    @PostMapping("/videosCollectFindV2")
    public Result findCollectVideosV2(@RequestBody String data){
        Integer userId = new JsonParser().parse(data).getAsJsonObject().get("userId").getAsInt();
        Integer page = new JsonParser().parse(data).getAsJsonObject().get("page").getAsInt();
        Sort sort = new Sort(Sort.Direction.DESC, "collectTime");
        Pageable pageable = PageRequest.of(page, 5, sort);
        List<Integer> users = videoLogDao.findByCollectAndTime(userId,pageable);
        List<XzqnServiceVideos> videos=serviceVideosDao.findAllById(users);
        return  ResultUtils.success(200,"查询收藏的视频成功",videos);
    }

    //收藏视频接口
    @PostMapping("/collectVideoV2")
    public Result collectVideoV2(@RequestBody String data) throws Exception{
        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer userId=jsonObject.get("userId").getAsInt();
        Integer videoId=jsonObject.get("videoId").getAsInt();
        XzqnServiceVideoLog videoLog=videoLogDao.findByVideolog(userId,videoId);
        if(videoLogDao.findByVideolog(userId,videoId)==null){
            videoLog = builderTime.fromJson(data, XzqnServiceVideoLog.class);
            videoLog.setCollectTime(now);
            videoLogDao.save(videoLog);
            msg="收藏成功";
            rmsg="1";
        }else {

            msg="取消收藏成功";
            videoLogDao.delete(videoLog);
            rmsg="0";
        }
        return ResultUtils.success(200,msg,rmsg);
    }

    //点赞视频接口
    @PostMapping("/likeVideoV2")
    public Result likeVideos(@RequestBody String data) throws Exception{
        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer userId=jsonObject.get("userId").getAsInt();
        Integer videoId=jsonObject.get("videoId").getAsInt();
        XzqnServiceVideoLike videoLike=videoLikeDao.findByVideoLike(userId,videoId);
        XzqnServiceVideos xzqnServiceVideos=serviceVideosDao.findById(videoId).get();
        if(videoLikeDao.findByVideoLike(userId,videoId)==null){
            videoLike= builderTime.fromJson(data, XzqnServiceVideoLike.class);
            videoLike.setLikeTime(now);
            videoLikeDao.save(videoLike);

            xzqnServiceVideos.setLikeNums(xzqnServiceVideos.getLikeNums()+1);
            msg="点赞成功";
            rmsg="1";
        }else {

            msg="取消点赞成功";
            videoLikeDao.delete(videoLike);

            xzqnServiceVideos.setLikeNums(xzqnServiceVideos.getLikeNums()-1);
            rmsg="0";
        }
        serviceVideosDao.save(xzqnServiceVideos);
        return ResultUtils.success(200,msg,rmsg);
    }
    
//    //点赞视频接口
//    @PostMapping("/likeVideo")
//    public Result likeVideos(@RequestBody String data) throws Exception{
//        Integer id = new JsonParser().parse(data).getAsJsonObject().get("id").getAsInt();
//        XzqnServiceVideos videos=serviceVideosDao.findById(id).get();
//        if(videos.getIsLike()==0){
//            videos.setIsLike(1);
//            videos.setLikeTime(now);
//            videos.setLikeNums(videos.getLikeNums()+1);
//            msg="点赞成功";
//        }else {
//            videos.setIsLike(0);
//            videos.setLikeTime(null);
//            videos.setLikeNums(videos.getLikeNums()-1);
//            msg="取消点赞成功";
//        }
//        serviceVideosDao.save(videos);
//        return ResultUtils.success(200,msg,"");
//    }

}
