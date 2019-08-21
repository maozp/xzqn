package com.gangukeji.xzqn.controller.xzqn.other;

import com.gangukeji.xzqn.config.Log;
import com.gangukeji.xzqn.dao.ReceiveUserDao;
import com.gangukeji.xzqn.dao.SendUserDao;
import com.gangukeji.xzqn.dao.ServiceVideosDao;
import com.gangukeji.xzqn.entity.*;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.utils.WaterMarkUtils;
import com.google.gson.Gson;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.Graphics;
import java.util.HashMap;

/**
 * @Author: hx
 * @Date: 2019/5/24 15:14
 * @Description:
 */
@SuppressWarnings("ALL")
@Controller
@CrossOrigin("*")
public class FileController {

    @Resource
    SendUserDao sendDao;
    @Resource
    ReceiveUserDao receiveDao;
    Gson gson = new Gson();
    String filePath = System.getProperty("user.dir");
    @Value("${server.url}")
    private String url;
    @Resource
    ServiceVideosDao serviceVideosDao;

    @ResponseBody
    @Log
    @GetMapping(value = "/Dnf8BGBQLW.txt", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public byte[] dnf() throws Exception {
//        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
//        String filename = jsonObject.get("filename").getAsString();
        filePath = System.getProperty("user.dir");
        String filename = "Dnf8BGBQLW.txt";
        File dest = new File(filePath + File.separator + "files" + File.separator + filename);
        FileInputStream inputStream = new FileInputStream(dest);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;

    }

//    @ResponseBody
//    @Log
//    @RequestMapping(value = "file/{filename}", consumes = MediaType.ALL_VALUE, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
////    @RequestMapping(value = "file/{filename}")
//    public ResponseEntity download(@PathVariable("filename") String filename) throws Exception {
//        filePath = System.getProperty("user.dir");
//        File dest = new File(filePath + File.separator + "files" + File.separator + filename);
//        Path path = Paths.get(dest.getAbsolutePath());
//        ByteArrayResource resource = null;
//        try {
//            resource = new ByteArrayResource(Files.readAllBytes(path));
//        } catch (Exception e) {
//            System.out.println(e.getMessage() + "2eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee文件未找到"+e.getMessage());
//            return ResponseEntity.badRequest().body("文件未找到");
//        }
//
//        if (filename.toLowerCase().endsWith(".png")) {
//            return ResponseEntity.ok()
//                    .contentLength(dest.length()).contentType(MediaType.IMAGE_PNG).body(resource);
//        }
//        if (filename.toLowerCase().endsWith(".jpeg")) {
//            return ResponseEntity.ok()
//                    .contentLength(dest.length()).contentType(MediaType.IMAGE_JPEG).body(resource);
//        }
//        return ResponseEntity.ok()
//                .contentLength(dest.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
//    }
//
//    @PostMapping("upload")
//    @ResponseBody
//    @Log
//    public Object upload(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResultUtils.error(-1, "上传失败 空的文件");
//        }
//        String fileOName = file.getOriginalFilename();
//        int first = fileOName.lastIndexOf(".") + 1;
//        String endWith = fileOName.substring(first);
//        filePath = System.getProperty("user.dir");
//        String filename = System.currentTimeMillis() + String.valueOf(RandomUtils.nextInt(100000, 999999));
//        File dest = new File(filePath + File.separator + "files" + File.separator + filename);
//        if (fileOName.toLowerCase().endsWith("png")) {
//            filename += ".png";
//        } else if (fileOName.toLowerCase().endsWith("jpg")) {
//            filename += ".JPEG";
//        } else {
//            filename += "." + endWith;
//            try {
//                file.transferTo(new File(dest.getAbsolutePath() + "." + endWith));
//            } catch (Exception e) {
//                System.out.println(e.getMessage() + "2eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//                return ResultUtils.error(-1, "存储文件失败    服务器内部错误");
//            }
////            return ResultUtils.success(200, "上传成功", url + filename);
//            return url + filename;
//        }
//        try {
//            Thumbnails.of(file.getInputStream()).scale(1f)
//                    .outputQuality(0.5f)
//                    .toFile(dest);
////            return ResultUtils.success(200, "上传jpg png图片成功", url + filename);
//            return url + filename;
//        } catch (Exception e) {
//            System.out.println(e.getMessage() + "2eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//            return ResultUtils.error(-1, "压缩文件失败   服务器内部错误");
//        }
//    }
    @PostMapping("upload")
    @ResponseBody
    @Log
    public Object uploadV2(@RequestParam("file") MultipartFile file,@RequestParam(value="lat",required=false,defaultValue = "0") float lat,@RequestParam(value="lng",required=false,defaultValue = "0") float lng) {
        if (file.isEmpty()) {
            return "";
        }
        filePath = System.getProperty("user.dir");
        String filename = System.currentTimeMillis() + String.valueOf(RandomUtils.nextInt(100000, 999999));
        File dest = new File(filePath + File.separator + "static" + File.separator + filename);

        try {
            Thumbnails.of(file.getInputStream()).scale(0.6f)
                    .outputFormat("png")
                    .outputQuality(0.6f)
                    .toFile(dest);
//            return ResultUtils.success(200, "上传jpg png图片成功", url + filename);
            /* 添加水印*/
            if(lat!=0&&lng!=0){
                Font font = new Font("微软雅黑", Font.PLAIN, 24);                     //水印字体
                String srcImgPath=dest+".png"; //源图片地址
                String tarImgPath=dest+".png"; //待存储的地址
                String nlat = Float.toString(lat);
                String nlng = Float.toString(lng);
                String content="("+nlat+","+nlng+")";
                String[] waterMarkContent={							//水印内容
                        content
                };
                Color color=new Color(255, 255, 255);                               //水印图片色彩以及透明度
                new WaterMarkUtils().addWaterMark(srcImgPath, tarImgPath, waterMarkContent,color ,font);
            }
            return url + filename+".png";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
//    @PostMapping("uploadList")
//    @ResponseBody
//    public String uploadList(@RequestParam("file") List<MultipartFile> files) {
//        if (file.isEmpty()) {
//            return "上传失败，请选择文件";
//        }
//        String fileOName = file.getOriginalFilename();
//        int first = fileOName.lastIndexOf(".") + 1;
//        String endWith = fileOName.substring(first);
//        log.error("endWith=======txt=============" + endWith);
//        filePath = System.getProperty("user.dir");
//        String filename = System.currentTimeMillis() + String.valueOf(RandomUtils.nextInt(100000, 999999)) + ".";
//        if (fileOName.toLowerCase().endsWith("png")) {
//            filename += "png";
//        } else if (fileOName.toLowerCase().endsWith("jpg")) {
//            filename += "jpg";
//        } else {
//            filename += endWith;
//        }
//        File dest = new File(filePath + File.separator + "files" + File.separator + filename);
//        log.error(dest.getAbsolutePath());
//        try {
//            file.transferTo(dest);
//            log.info("上传成功");
//            log.error("filename====================" + filename);
//
//            return url + filename;
//        } catch (IOException e) {
//            log.error(e.toString(), e);
//            return "上传失败";
//        }
//    }

    //接收视频
    @PostMapping("upload/videos")
    @ResponseBody
    @Log
    public Object uploadV3(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return "";
        }
        filePath = System.getProperty("user.dir");
        String filename = System.currentTimeMillis() + String.valueOf(RandomUtils.nextInt(100000, 999999));
        File dest = new File(filePath + File.separator + "static" + File.separator + filename);
        //扩展名
        String suffix=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        try {
            FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(dest+suffix));
            return url + filename+suffix;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
