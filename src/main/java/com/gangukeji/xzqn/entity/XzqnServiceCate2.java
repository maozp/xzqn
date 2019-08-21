package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gangukeji.xzqn.utils.StatusUtils2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hx
 * @Description 服务分类
 */

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnServiceCate2 {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cateId;
    private Integer isParent;
    private String name;
    private String pic1;
    private String pic2;
    private String pic3;
    private Integer del;
    private Integer order;
    @JsonIgnore
    private String mark;
    private BigDecimal fee;
    @Transient
    private boolean isCheck = false;
    @Transient
    private List<XzqnServiceCate2> sonList;
    @Transient
    private List<XzqnServiceProp2> prop2List;

    @JsonGetter("sonList")
    public List<XzqnServiceCate2> getSonList() {
        List<XzqnServiceCate2> sonList = StatusUtils2.cate2List.stream().filter(e -> e.getIsParent().equals(cateId)).collect(Collectors.toList());
        sonList=sonList.stream().sorted(Comparator.comparing(XzqnServiceCate2::getOrder).reversed()).collect(Collectors.toList());
        return sonList;
    }

    @JsonGetter("prop2List")
    public List<XzqnServiceProp2> getProp2List() {
        List<XzqnServiceProp2> propList = StatusUtils2.prop2List.stream().filter(e -> e.getCateId().equals(cateId) && e.getParentId() == 0).collect(Collectors.toList());
//        propList=propList.stream().sorted(Comparator.comparing(e->e.getProp2s().size())).collect(Collectors.toList());
//        Collections.reverse(propList);
        return propList;
    }

    @JsonIgnore
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @JsonIgnore
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;
}

