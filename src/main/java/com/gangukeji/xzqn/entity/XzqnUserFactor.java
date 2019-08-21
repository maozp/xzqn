package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/15 16:44
 * @Description: 用户类型决定计费系数
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnUserFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer factorId;
    private Integer parentId;
    private BigDecimal fee;
    private BigDecimal factor;
    private String name;
    @Transient
    private List<XzqnUserFactor> factors=new ArrayList<>();
    @Transient
    private boolean isCheck=false;

    public void addFactor(XzqnUserFactor factor) {
        factors.add(factor);
    }
}
