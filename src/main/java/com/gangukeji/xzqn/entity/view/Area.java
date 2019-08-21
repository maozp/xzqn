package com.gangukeji.xzqn.entity.view;

import lombok.Data;

import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/6/27 11:03
 * @Description: 地区序列化模板
 */
@Data
public class Area {
    /**
     * name : 北京
     * child : [{"name":"东城区"},{"name":"西城区"},{"name":"崇文区"},{"name":"宣武区"},{"name":"朝阳区"},{"name":"丰台区"},{"name":"石景山区"},{"name":"海淀区"},{"name":"门头沟区"},{"name":"房山区"},{"name":"通州区"},{"name":"顺义区"},{"name":"昌平区"},{"name":"大兴区"},{"name":"平谷区"},{"name":"怀柔区"},{"name":"密云县"},{"name":"延庆县"}]
     */
    private String name;
    private Boolean click=false;
    private List<ChildBean> child;

    @Data
    public static class ChildBean {
        /**
         * name : 东城区
         */
        private String name;
        private Boolean click=false;
    }
}
