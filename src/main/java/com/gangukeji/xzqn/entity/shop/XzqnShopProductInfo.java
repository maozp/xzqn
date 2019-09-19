package com.gangukeji.xzqn.entity.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
/**
 * 商城产品信息表
 */
public class XzqnShopProductInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;
    private Integer userId;
    private String productNo;
    private String productName;
    private Integer cateId;
    private BigDecimal price;
    private BigDecimal priceOrigin;
    private Integer publishStatus;
    private Integer checkStatus;
    private String property;
    private Integer storeCount;
    private Integer payCount;
    private String mark;

    private String img;

    private String des;

    private String pic;
    @Transient
    private String[] picList;
    @Transient

    private List imgAndDes;
    @Transient
    private String[] desList;
    @Transient
    private String[] imgList;

    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date createTime;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Date updateTime;


    public String[] getPicList() {
        return this.picList = pic.split("@");
    }

    public String[] getDesList() {
        return this.desList = des.split("@");
    }

    public String[] getImgList() {
        return this.imgList = img.split("@");
    }
    public List getImgAndDes() {
        ArrayList<Map> result = new ArrayList<>();

        String[] imgList = img.split("@");
        String[] desList = des.split("@");
        for (int i = 0; i < imgList.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("img", imgList[i]);
            map.put("des", desList[i]);
            result.add(map);
        }
        return result;
    }
}
