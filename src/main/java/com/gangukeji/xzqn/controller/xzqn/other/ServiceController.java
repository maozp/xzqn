package com.gangukeji.xzqn.controller.xzqn.other;

/**
 * @Author: hx
 * @Date: 2019/5/14 9:26
 * @Description: 服务类型获取, 计算服务价格
 */

import com.gangukeji.xzqn.dao.ServiceCateDao;
import com.gangukeji.xzqn.dao.ServiceFeeItemDao;
import com.gangukeji.xzqn.entity.view.BigCate;
import com.gangukeji.xzqn.entity.view.SmallCate;
import com.gangukeji.xzqn.entity.view.response.ComputePriceInfoResp;
import com.gangukeji.xzqn.entity.XzqnServiceCate;
import com.gangukeji.xzqn.utils.Result;
import com.gangukeji.xzqn.utils.ResultUtils;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class ServiceController {
    @Resource
    ServiceCateDao serviceCateDao;
    @Resource
    ServiceFeeItemDao itemDao;

//    /**
//     * 获取服务类型小类
//     */
//    @RequestMapping("serviceCate/findSmallCate")
//    public Result findSmallCate() {
//        List<XzqnServiceCate> all = serviceCateDao.findAll();
//        List<XzqnServiceCate> smallList = all.stream().filter(a -> a.getIsParent() != 0).collect(Collectors.toList());
//        List<SmallCateDto> resps = new ArrayList<>();
//        smallList.forEach(s -> {
//            resps.add(new SmallCateDto(s.getId(), s.getName(), false));
//        });
//        return ResultUtils.success(200, "小类返回成功", resps);
//    }

    /**
     * 获取服务类型
     */
    @RequestMapping("serviceCate/findAll")
    public Result findServiceCate() {
        List<BigCate> result = new ArrayList<>();
        List<XzqnServiceCate> cates = serviceCateDao.findAll();
        ArrayList<XzqnServiceCate> cateParentList = new ArrayList<>();
        ArrayList<XzqnServiceCate> cateSonList = new ArrayList<>();
        for (XzqnServiceCate cate : cates) {
            cate.setCreateTime(null);
            cate.setUpdateTime(null);
            //找出所有父节点
            if (cate.getIsParent() == 0) {
                cateParentList.add(cate);
            }
            //找出所有子节点
            else {
                cateSonList.add(cate);
            }
        }
        for (XzqnServiceCate cateP : cateParentList) {
            List<SmallCate> smallCates = new ArrayList<>();
            for (XzqnServiceCate cate : cateSonList) {
                if (cate.getIsParent() == cateP.getId()) {
                    SmallCate smallCate = SmallCate.builder().build();
                    smallCate.setId(cate.getId());
                    smallCate.setName(cate.getName());
                    smallCates.add(smallCate);
                }
            }
            result.add(BigCate.builder()
                    .id(cateP.getId())
                    .bigCate(cateP.getName())
                    .smallCates(smallCates)
                    .build());
        }
        return ResultUtils.success(200, "服务类型获取成功", result);
    }

    /**
     * 计算服务价格
     */
    @PostMapping("computePrice/compute")
    public Result computePrice(@RequestBody String data) {
        Integer cateId = null;
        try {
            cateId = new JsonParser().parse(data).getAsJsonObject().get("cateId").getAsInt();
        } catch (Exception e) {
            return ResultUtils.error(-1, "请传cateId");
        }
        XzqnServiceCate cate = serviceCateDao.findById(cateId).get();
        BigDecimal fee = cate.getFee();
        ComputePriceInfoResp resp = ComputePriceInfoResp.builder()
                .serviceFee(fee)
                .items(itemDao.findAll())
                .platformFee(new BigDecimal(1)).build();
        return ResultUtils.success(200, "价格计算成功", resp);
    }
}

