package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.*;
import com.gangukeji.xzqn.utils.EscapeUtils;
import com.gangukeji.xzqn.utils.HtmlUtils;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.RandomUtils;
import org.apache.tomcat.util.security.Escape;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 *  资讯接口
 * @author mao
 * @date 2019/8/26 9:40
 */
@RestController
@RequestMapping
@CrossOrigin("*")
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
    @Value("${server.url}")
    private String url;

    private String msg;
    private String rmsg;
    Date now = new Date();

    Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    //后台添加资讯信息
    @PostMapping("/newsSubmit")
    public Result newsSubmit(@RequestBody String data) throws  Exception {
        XzqnNews news = gson.fromJson(data, XzqnNews.class);

        String newsContent=new JsonParser().parse(data).getAsJsonObject().get("newsContent").getAsString();
        newsContent= EscapeUtils.unescape(newsContent);
        news.setNewsContent(newsContent);
        newsContent="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<title>"+news.getNewsTitle() +"</title>\n" +
                "</head>"+newsContent+"</body>\n" +
                "</html>";
        String filePath = System.getProperty("user.dir");
        String filename = System.currentTimeMillis() + String.valueOf(RandomUtils.nextInt(100000, 999999));
        File dest = new File(filePath + File.separator + "static" + File.separator + filename);
        System.out.println(dest.toString());

        String result= HtmlUtils.makeHtml(newsContent,filePath,filename);
        news.setNewsUrl(url+filename+".html");
        news.setNewsLikeNums(0);
        news.setNewsReadNums(0);
        news.setNewsCommentNums(0);
        news.setNewsDate(now);

        news=newsDao.save(news);
        //news.getTitleFirstImg().split("@");
//        HashMap<Object,Object> map=new HashMap<>();
//        map.put("id",news.getId());
//        map.put("newsTitle",news.getNewsTitle());
//        map.put("newsContent",news.getNewsContent());
//        map.put("newsReadNums",news.getNewsReadNums());
//        map.put("newsCommentNums",news.getNewsCommentNums());
//        map.put("newsLikeNums",news.getNewsLikeNums());
//        map.put("titleFirstImg",news.getTitleFirstImg());
//        map.put("newsType",news.getNewsType());
//        map.put("newsUrl",news.getNewsUrl());
//        map.put("ImgList",news.getTitleFirstImg().split("@"));
        return ResultUtils.success(200,"添加资讯成功",news);
    }

    //后台修改资讯信息
    @PostMapping("/newsUpdate")
    public Result updateSubmit(@RequestBody String data) throws  Exception {
        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer newsId=jsonObject.get("id").getAsInt();
        XzqnNews news=newsDao.findById(newsId).get();

        news =gson.fromJson(data, XzqnNews.class);
        String newsContent=news.getNewsContent();
        newsContent="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<title>"+news.getNewsTitle() +"</title>\n" +
                "</head>"+newsContent+"</body>\n" +
                "</html>";
        String filePath = System.getProperty("user.dir");
        String filename = System.currentTimeMillis() + String.valueOf(RandomUtils.nextInt(100000, 999999));
        File dest = new File(filePath + File.separator + "static" + File.separator + filename);
        System.out.println(dest.toString());

        String result= HtmlUtils.makeHtml(newsContent,filePath,filename);
        news.setNewsUrl(url+filename+".html");
        newsDao.save(news);
        return ResultUtils.success(200,"修改资讯成功",news);
    }
    //后台删除资讯信息
    @PostMapping("/newsDelete")
    public Result newsDelete(@RequestBody String data) throws  Exception {
        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer newsId=jsonObject.get("id").getAsInt();
        XzqnNews news=newsDao.findById(newsId).get();
        newsDao.delete(news);
        return ResultUtils.success(200,"删除资讯成功",news);
    }

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
        XzqnNews news=newsDao.findById(id).get();
        news.setNewsReadNums(news.getNewsReadNums()+1);
        news.setNewsCommentNums(newsCommentDao.findCountComment(id));
        news.setNewsLikeNums(newsLikeDao.findCountLike(id));
        newsDao.save(news);
        return ResultUtils.success(200,"查询资讯成功",news);
    }

    //分享资讯详情
    @PostMapping("/newsShore")
    public Result findShore(@RequestBody String data){
        Integer id = new JsonParser().parse(data).getAsJsonObject().get("id").getAsInt();
        String url=newsDao.findByIdUrl(id);
        return ResultUtils.success(200,"分享成功",url);
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

    //判断是否收藏
    @PostMapping("/isNewsCollect")
    public Result newsV2(@RequestBody String data) throws Exception{
        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer userId=jsonObject.get("userId").getAsInt();
        Integer newsId=jsonObject.get("collectNewsId").getAsInt();
        XzqnNewsLog newsLog=newsLogDao.findByNewslog(userId,newsId);
        HashMap<String,String> map=new HashMap<>();
        if(newsLogDao.findByNewslog(userId,newsId)==null){
            msg="未收藏";
            map.put("isCollect","0");
        }else {
            msg="已收藏";
            map.put("isCollect","1");
        }
        return ResultUtils.success(200,msg,map);
    }

    //判断是否点赞
    @PostMapping("/isNewsLike")
    public Result likeV2(@RequestBody String data) throws Exception{
        JsonObject jsonObject=new JsonParser().parse(data).getAsJsonObject();
        Integer userId=jsonObject.get("userId").getAsInt();
        Integer newsId=jsonObject.get("newsId").getAsInt();
        XzqnNewsLike newsLog=newsLikeDao.findByNewsLike(userId,newsId);
        HashMap<String,String> map=new HashMap<>();
        if(newsLikeDao.findByNewsLike(userId,newsId)==null){
            msg="未收藏";
            map.put("isLilk","0");
        }else {
            msg="已收藏";
            map.put("isLilk","1");
        }
        return ResultUtils.success(200,msg,map);
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
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<XzqnNewsComment> news = newsCommentDao.findByComment(newsId,pageable);
        return ResultUtils.success(200,"查看资讯留言列表成功",news);
    }

}
