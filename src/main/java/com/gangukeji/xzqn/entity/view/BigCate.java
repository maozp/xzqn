package com.gangukeji.xzqn.entity.view;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/5/15 19:03
 * @Description: 商城分类大类
 */
@Data@Builder
public class BigCate {
    int id;
    String bigCate;
    boolean isCheck=false;
    List<SmallCate> smallCates;
}
