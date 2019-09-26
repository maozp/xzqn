package com.gangukeji.xzqn.utils;

import java.io.FileOutputStream;

/**
 * @author mao
 * @date 2019/9/22
 * @Description 创建html
 */
public class HtmlUtils {

    /**
     *
     * @param content 内容
     * @param dirPath 生成html的存放路径
     * @param fileName html名字
     * @return
     */
    public static String makeHtml(String content,String dirPath,String fileName){
        String result=null;
        try {

            String name=fileName+".html";
            name=dirPath+"/static/"+name;//生成的html文件
            FileOutputStream fileOutputStream=new FileOutputStream(name);//建立文件输出流

            byte tag_bytes[]=content.getBytes();
            fileOutputStream.write(tag_bytes);
            fileOutputStream.close();
            result ="success";
        }catch (Exception e){
            result=e.toString();
        }
        return result;
    }
}
