package com.gangukeji.xzqn.entity.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: hx
 * @Date: 2019/5/30 14:35
 * @Description: v1版本地区实体 目前用v2版
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Deprecated
public class AreaDto {
    Integer id;
    String area;
    boolean isClick;
}
