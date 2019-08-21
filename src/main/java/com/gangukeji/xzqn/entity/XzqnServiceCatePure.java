package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author hx
 * @Description 用于返回所有小类
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class XzqnServiceCatePure {
    private Integer id;
    private Integer isParent;
    private String name;
    private BigDecimal fee;
    private boolean click=false;
    private Integer del;
}

