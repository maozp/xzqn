package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.gangukeji.xzqn.utils.StatusUtils2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: hx
 * @Date: 2019/6/15 11:08
 * @Description: 服务属性
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnServiceProp2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer propId;
    private Integer cateId;
    private String name;
    private BigDecimal fee;
    private Integer multi;
    private Integer parentId;
    private Integer del;
    /**
     * 通过get方法自动去获取儿子属性
     */
    @Transient
    private List<XzqnServiceProp2> prop2s;
    @JsonGetter("prop2s")
    public List<XzqnServiceProp2> getProp2s() {
        List<XzqnServiceProp2> sonList = StatusUtils2.prop2List.stream().filter(e -> e.getParentId().equals(propId)).collect(Collectors.toList());
        return sonList;
    }

    @Transient
    private boolean isCheck = false;

}
