package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.dao.NewsCommentDao;
import com.gangukeji.xzqn.dao.NewsDao;
import com.gangukeji.xzqn.entity.XzqnNews;
import com.gangukeji.xzqn.entity.XzqnNewsComment;
import com.gangukeji.xzqn.entity.XzqnServiceVideos;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

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
    NewsCommentDao newsCommentDao;

    private String msg;
    Date now = new Date();

    Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    //查看资讯列表
    @PostMapping("/newsFindAll")
    public Result findAllNews(@RequestParam("page") int page) throws  Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "newsDate");
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<XzqnNews> news = newsDao.findAll(pageable);
        return ResultUtils.success(200,"查看资讯列表成功",news);
    }
    //查询资讯详情
    @PostMapping("/newsFind")
    public Result findNews(@RequestParam(value="id") Integer id){
        return ResultUtils.success(200,"查询资讯成功",newsDao.findById(id));
    }
    //查询收藏的资讯
    @PostMapping("/newsCollectFind")
    public Result findCollectNews(){
        return  ResultUtils.success(200,"查询收藏的资讯列表成功",newsDao.findByCollectAndTime());
    }
    //收藏资讯接口
    @PostMapping("/collectNews")
    public Result collectNews(@RequestParam(value="id") Integer newsId) throws Exception{
        XzqnNews news=newsDao.findById(newsId).get();
        if(news.getIsCollect()==0){
            news.setIsCollect(1);
            news.setCollectTime(now);
            msg="收藏成功";
        }else {
            news.setIsCollect(0);
            news.setCollectTime(null);
            msg="取消收藏成功";
        }
        newsDao.save(news);
        return ResultUtils.success(200,msg,"");
    }

    //点赞资讯接口
    @PostMapping("/likeNew")
    public Result likeNews(@RequestParam(value="id") Integer NewId) throws Exception{
        XzqnNews News=newsDao.findById(NewId).get();
        if(News.getIsLike()==0){
            News.setIsLike(1);
            News.setLikeTime(now);
            News.setNewsLikeNums(News.getNewsLikeNums()+1);
            msg="点赞成功";
        }else {
            News.setIsLike(0);
            News.setLikeTime(null);
            News.setNewsLikeNums(News.getNewsLikeNums()-1);
            msg="取消点赞成功";
        }
        newsDao.save(News);
        return ResultUtils.success(200,msg,"");
    }
    //资讯留言接口
    @PostMapping("/NewComment")
    public Result newsComment(@RequestBody String data) throws Exception{
        Gson builderTime = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        XzqnNewsComment news = builderTime.fromJson(data, XzqnNewsComment.class);
        Integer userId = news.getUserId();
        newsCommentDao.save(news);
        return ResultUtils.success(200,msg,"");
    }

}
