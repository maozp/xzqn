package com.gangukeji.xzqn.controller.xzqn.other;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gangukeji.xzqn.dao.*;
import com.gangukeji.xzqn.entity.XzqnAuthContent;
import com.gangukeji.xzqn.entity.XzqnAuthPerson;
import com.gangukeji.xzqn.entity.XzqnAuthSkill;
import com.gangukeji.xzqn.entity.XzqnUserSend;
import com.gangukeji.xzqn.serialize.AuthContentSerialize;
import com.gangukeji.xzqn.serialize.AuthPersonSerialize;
import com.gangukeji.xzqn.serialize.AuthSkillSerialize;
import com.gangukeji.xzqn.utils.ListToPageUtil;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.gangukeji.xzqn.entity.view.AuthAll;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * @Author: hx
 * @Date: 2019/7/18 16:14
 * @Description: 用于后台管理系统认证模块
 */
@RestController
@CrossOrigin("*")
public class PCAuthController {
    @Autowired
    AuthSkillDao authSkillDao;
    @Autowired
    AuthPersonDao authPersonDao;
    @Autowired
    AuthContentDao authContentDao;
    @Autowired
    ReceiveUserDao receiveUserDao;
    @Autowired
    UserDao userDao;
    @Autowired
    SendUserDao sendUserDao;
    @Autowired
    WebSocket webSocket;



    @PostMapping("auth/skill/findAll")
    public String skillFindAll(@PageableDefault Pageable pageable) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(XzqnAuthSkill.class, new AuthSkillSerialize());
        mapper.registerModule(module);
        Result result = ResultUtils.success(200, "技能认证列表查询", authSkillDao.findAll(pageable));
        return mapper.writeValueAsString(result);
    }

    @PostMapping("auth/person/findAll")
    public String personFindAll(@PageableDefault Pageable pageable) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(XzqnAuthPerson.class, new AuthPersonSerialize());
        mapper.registerModule(module);
        Result result = ResultUtils.success(200, "个人认证列表查询", authPersonDao.findAll(pageable));
        return mapper.writeValueAsString(result);
    }

    @PostMapping("auth/content/findAll")
    public String contentFindAll(@PageableDefault Pageable pageable) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(XzqnAuthContent.class, new AuthContentSerialize());
        mapper.registerModule(module);
        Result result = ResultUtils.success(200, "内容认证列表查询", authContentDao.findAll(pageable));
        return mapper.writeValueAsString(result);
    }

    @PostMapping("auth/content/update")
    public Result contentStatusUpdate(@RequestBody Map<String, Object> map) {
        int count = 0;
        //status=2审核通过
        int userId = (int) map.get("userId");
        int isCheck = (int) map.get("isCheck");
        if (isCheck == 9) {
            String refuseReason = (String) map.get("refuseReason");
            count = authContentDao.updateStatusAndRefuseReason(isCheck, refuseReason, userId);
        } else {
            count = authContentDao.updateStatus(isCheck, userId);
        }
        map.put("更新数量", count);
        return ResultUtils.success(200, "内容认证更新成功", map);
    }

    @PostMapping("auth/skill/update")
    public Result skillStatusUpdate(@RequestBody Map<String, Object> map) {
        int count = 0;
        //status=2审核通过
        int userId = (int) map.get("userId");
        int isCheck = (int) map.get("isCheck");
        if (isCheck == 9) {
            String refuseReason = (String) map.get("refuseReason");
            count = authSkillDao.updateStatusAndRefuseReason(isCheck, refuseReason, userId);
        } else {
            count = authSkillDao.updateStatus(isCheck, userId);
        }
        map.put("更新数量", count);
        return ResultUtils.success(200, "技能认证更新成功", map);
    }

    @PostMapping("auth/person/update")
    public Result personStatusUpdate(@RequestBody Map<String, Object> map) {
        int count = 0;
        //status=2审核通过
        int userId = (int) map.get("userId");
        int isCheck = (int) map.get("isCheck");
        if (isCheck == 9) {
            String refuseReason = (String) map.get("refuseReason");
            count = authPersonDao.updateStatusAndRefuseReason(isCheck, refuseReason, userId);
        } else {
            count = authPersonDao.updateStatus(isCheck, userId);
        }
        map.put("更新数量", count);
        return ResultUtils.success(200, "个人认证更新成功", map);
    }

    @PostMapping("auth/receive/findAll")
    public Map findAll(@PageableDefault Pageable pageRequest) throws Exception {
        Set<Integer> userIdSet = new HashSet<>();
        ArrayList<AuthAll> authAlls = new ArrayList<>();
        userIdSet.addAll(authPersonDao.getAuthUserId());
        userIdSet.addAll(authContentDao.getAuthUserId());
        userIdSet.addAll(authSkillDao.getAuthUserId());
        int userIdOut = 0;
        for (Integer userId : userIdSet) {
            if(userId==null){

            }else{
                userIdOut = userId;
                AuthAll authAll = new AuthAll();
                Object[] args = (Object[]) receiveUserDao.findByNameAndHeadImgByUserId(userId);
                if (args != null) {
                    authAll.setName((String) args[0]);
                    authAll.setHead_img((String) args[1]);
                }
                authAll.setStatusContent(authContentDao.getAuthStatus(userId).orElse(0));
                authAll.setStatusPerson(authPersonDao.getAuthStatus(userId).orElse(0));
                authAll.setStatusSkill(authSkillDao.getAuthStatus(userId).orElse(0));
                authAll.setAuthContent(authContentDao.findTopByUserIdOrderByIdDesc(userId));
                authAll.setAuthSkill(authSkillDao.findTopByUserIdOrderByIdDesc(userId));
                authAll.setAuthPerson(authPersonDao.findTopByUserIdOrderByIdDesc(userId));
                authAll.setUserId(userIdOut);
                authAlls.add(authAll);
            }

        }
        List resp = ListToPageUtil.go2(pageRequest.getPageNumber(), pageRequest.getPageSize(), authAlls);
        Result result = ResultUtils.success(200, "查询全部认证成功总记录数:" + resp.size(), resp);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(result, new TypeReference<Map<String, Object>>() {
        });
        map.put("totalElement", authAlls.size());
        return map;
    }

    @PostMapping("/index/xzqn/authAndSend")
    public Result findAuthAndSend(@PageableDefault Pageable pageRequest) throws Exception{
        Set<Integer> userIdSet = new HashSet<>();
        ArrayList<AuthAll> authAlls = new ArrayList<>();
        userIdSet.addAll(authPersonDao.getAuthUserId());
        userIdSet.addAll(authContentDao.getAuthUserId());
        userIdSet.addAll(authSkillDao.getAuthUserId());
        int userIdOut = 0;
        for (Integer userId : userIdSet) {
            if(userId==null){

            }else{
                userIdOut = userId;
                AuthAll authAll = new AuthAll();
                Object[] args = (Object[]) receiveUserDao.findByNameAndHeadImgByUserId(userId);
                if (args != null) {
                    authAll.setName((String) args[0]);
                    authAll.setHead_img((String) args[1]);
                }
                authAll.setStatusContent(authContentDao.getAuthStatus(userId).orElse(0));
                authAll.setStatusPerson(authPersonDao.getAuthStatus(userId).orElse(0));
                authAll.setStatusSkill(authSkillDao.getAuthStatus(userId).orElse(0));
                authAll.setAuthContent(authContentDao.findTopByUserIdOrderByIdDesc(userId));
                authAll.setAuthSkill(authSkillDao.findTopByUserIdOrderByIdDesc(userId));
                authAll.setAuthPerson(authPersonDao.findTopByUserIdOrderByIdDesc(userId));
                authAll.setUserId(userIdOut);
                authAlls.add(authAll);
            }

        }
        Integer sendNums=sendUserDao.findBySendNums();

        HashMap<String,Object> map1=new HashMap();
        map1.put("authNums", authAlls.size());
        map1.put("sendNums", sendNums);
        return ResultUtils.success(200, "查询师傅与发单方认证数:",map1);
    }

    //分页，按创建时间倒序方式查询发单方
    @PostMapping("auth/send/findAll")
    //(@PathVariable("page") int page)
    //(@PageableDefault Pageable pageable)
    public Object findAllSend(@RequestBody String data) throws  Exception {
        //分页+排序查询演示：
        //Pageable pageable = new PageRequest(page, size);//2.0版本后,该方法已过时
        Integer page = new JsonParser().parse(data).getAsJsonObject().get("page").getAsInt();
        //Sort sort = new Sort(Sort.Direction.DESC, "updateTime","createTime");
        Pageable pageable = PageRequest.of(page, 5);
        Page<XzqnUserSend> sendUsers = sendUserDao.findByPcSendNums(pageable);
        return sendUsers;
    }

    @PostMapping("auth/send/update")
    public Object sendUpdate(@RequestBody Map map) {
        int count = 0;
        //status=2审核通过
        int sendUserId = (int) map.get("sendUserId");
        int isCheck = (int) map.get("isCheck");
        if (isCheck == -1) {
            String refuseReason = (String) map.get("refuseReason");
            count = sendUserDao.updateStatusAndRefuseReason(isCheck, refuseReason, sendUserId);
        } else {
            count = sendUserDao.updateStatus(isCheck, sendUserId);
        }
        map.put("更新数量", count);

        return ResultUtils.success(200, "发单用户认证更新成功", map);
    }

}
