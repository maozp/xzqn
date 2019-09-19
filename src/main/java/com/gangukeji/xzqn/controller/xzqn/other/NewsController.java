package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.*;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 *  资讯接口
 * @author mao
 * @date 2019/8/26 9:40
 */
@RestController
@RequestMapping
public class NewsController {

    @Resource
    NewsDao newsDao;
    @Resource
    NewsLikeDao newsLikeDao;
    @Resource
    NewsLogDao newsLogDao;
    @Resource
    NewsCommentDao newsCommentDao;
    @Resource
    UserDao userDao;

    private String msg;
    private String rmsg;
    Date now = new Date();

    Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    //查看资讯列表
    @PostMapping("/newsFindAll")
    public Result findAllNews(@RequestBody String data) throws  Exception {
        Integer page = new JsonParser().parse(data).getAsJsonObject().get("page").getAsInt();
        Sort sort = new Sort(Sort.Direction.DESC, "newsDate");
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<XzqnNews> news = newsDao.findAll(pageable);
        return ResultUtils.success(200,"查看资讯列表成功",news);
    }
    //查询资讯详情
    @PostMapping("/newsFind")
    public Result findNews(@RequestBody String data){
        Integer id = new JsonParser().parse(data).getAsJsonObject().get("id").getAsInt();
        return ResultUtils.success(200,"查询资讯成功",newsDao.findById(id));
    }

    //分享资讯详情
    @PostMapping("/newsShore")
    public Result findShore(@RequestBody String data){
        Integer id = new JsonParser().parse(data).getAsJsonObject().get("id").getAsInt();
        return ResultUtils.success(200,"分享成功","https://www.xiaozheng8.com/"+id+".html");
    }


//    //查询收藏的资讯
//    @PostMapping("/newsCollectFind")
//    public Result findCollectNews(){
//        return  ResultUtils.success(200,"查询收藏的资讯列表成功",newsDao.findByCollectAndTime());
//    }
//
//    //收藏资讯接口
//    @PostMapping("/collectNews")
//    public Result collectNews(@RequestBody String data) throws Exception{
//        Integer newsId = new JsonParser().parse(data).getAsJsonObject().get("id").getAsInt();
//        XzqnNews news=newsDao.findById(newsId).get();
//        if(news.getIsCollect()==0){
//            news.setIsCollect(1);
//            news.setCollectTime(now);
//            msg="收藏成功";
//            rmsg="1";
//        }else {
//            news.setIsCollect(0);
//            news.setCollectTime(null);
//            msg="取消收藏成功";
//            rmsg="0";
//        }
//        newsDao.save(news);
//        return ResultUtils.success(200,msg,rmsg);
//    }


    //查询收藏的资讯
    @PostMapping("/newsCollectFindV2")
    public Result findCollectNewsV2(@RequestBody String data){

        Integer userId = new JsonParser().parse(data).getAsJsonObject().get("userId").getAsInt();
        Integer page = new JsonParser().parse(data).getAsJsonObject().get("page").getAsInt();
        Sort sort = new Sort(Sort.Direction.DESC, "collectTime");
        Pageable pageable = PageRequest.of(page, 5, sort);
        List<Integer> users = newsLogDao.findByCollectAndTime(userId,pageable);
        List<XzqnNews> videos= newsDao.findAllById(users);
        return  ResultUtils.success(200,"查询收藏的视频成功",videos);

    }

    //收藏资讯接口
    @PostMapping("/collectNewsV2")
    public Result collectNewsV2(@RequestBody String data) throws Exception{
        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer userId=jsonObject.get("userId").getAsInt();
        Integer newsId=jsonObject.get("collectNewsId").getAsInt();
        XzqnNewsLog newsLog=newsLogDao.findByNewslog(userId,newsId);
        if(newsLogDao.findByNewslog(userId,newsId)==null){
            newsLog = gson.fromJson(data, XzqnNewsLog.class);
            newsLog.setCollectTime(now);
            newsLogDao.save(newsLog);
            msg="收藏成功";
            rmsg="1";
        }else {

            msg="取消收藏成功";
            newsLogDao.delete(newsLog);
            rmsg="0";
        }
        return ResultUtils.success(200,msg,rmsg);
    }

    //点赞资讯接口
    @PostMapping("/likeNewsV2")
    public Result likeVideos(@RequestBody String data) throws Exception{
        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer userId=jsonObject.get("userId").getAsInt();
        Integer newsId=jsonObject.get("newsId").getAsInt();
        XzqnNewsLike newsLike=newsLikeDao.findByNewsLike(userId,newsId);
        XzqnNews xzqnNews=newsDao.findById(newsId).get();
        if(newsLikeDao.findByNewsLike(userId,newsId)==null){
            newsLike= gson.fromJson(data, XzqnNewsLike.class);
            newsLike.setLikeTime(now);
            newsLikeDao.save(newsLike);

            xzqnNews.setNewsLikeNums(xzqnNews.getNewsLikeNums()+1);
            msg="点赞成功";
            rmsg="1";
        }else {

            msg="取消点赞成功";
            newsLikeDao.delete(newsLike);

            xzqnNews.setNewsLikeNums(xzqnNews.getNewsLikeNums()-1);
            rmsg="0";
        }
        newsDao.save(xzqnNews);
        return ResultUtils.success(200,msg,rmsg);
    }




//    //点赞资讯接口
//    @PostMapping("/likeNews")
//    public Result likeNews(@RequestBody String data) throws Exception{
//        Integer NewId = new JsonParser().parse(data).getAsJsonObject().get("id").getAsInt();
//        XzqnNews News=newsDao.findById(NewId).get();
//        if(News.getIsLike()==0){
//            News.setIsLike(1);
//            News.setLikeTime(now);
//            News.setNewsLikeNums(News.getNewsLikeNums()+1);
//            msg="点赞成功";
//            rmsg="1";
//        }else {
//            News.setIsLike(0);
//            News.setLikeTime(null);
//            News.setNewsLikeNums(News.getNewsLikeNums()-1);
//            msg="取消点赞成功";
//            rmsg="0";
//        }
//        newsDao.save(News);
//        return ResultUtils.success(200,msg,rmsg);
//    }

    //资讯留言接口
    @PostMapping("/newsComment")
    public Result newsComment(@RequestBody String data) throws Exception{
        Gson builderTime = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        XzqnNewsComment news = builderTime.fromJson(data, XzqnNewsComment.class);
        Integer userId = news.getUserId();
        Integer newsId = news.getNewsId();
        String comment=news.getComment();
        XzqnUser xzqnUser=userDao.findById(userId).get();
        news.setUserName(xzqnUser.getName());
        news.setUserHeadImg(xzqnUser.getUserHeadImg());
        news.setCommentTime(now);
        news.setComment(comment);
        news.setNewsId(newsId);
        msg="留言成功";
        newsCommentDao.save(news);
        return ResultUtils.success(200,msg,news);
    }

    //展示资讯留言接口
    @PostMapping("/newsCommentList")
    public Result commentFind(@RequestBody String data){
        Integer newsId = new JsonParser().parse(data).getAsJsonObject().get("newsId").getAsInt();
        Integer page = new JsonParser().parse(data).getAsJsonObject().get("page").getAsInt();
        Sort sort = new Sort(Sort.Direction.DESC, "commentTime");
        Pageable pageable = PageRequest.of(page, 10, sort);
        List<XzqnNewsComment> news = newsCommentDao.findByComment(newsId,pageable);
        return ResultUtils.success(200,"查看资讯留言列表成功",news);
    }

}
