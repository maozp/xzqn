package com.gangukeji.xzqn.entity.view;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: hx
 * @Date: 2019/5/15 20:10
 * @Description: 商城分类小类
 */
@Builder@Data
public class SmallCate {
    int id;
    String name;
    boolean isCheck=false;
}
