package com.gangukeji.xzqn.entity.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: hx
 * @Date: 2019/5/16 21:56
 * @Description: 用于订单详情的日志显示
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogView {
    private String name;
    private String des;
    private Integer status;
    private Date time;
    private Boolean isOk;
    private String logTop;
}
