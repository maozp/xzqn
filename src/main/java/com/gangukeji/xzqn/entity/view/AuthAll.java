package com.gangukeji.xzqn.entity.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gangukeji.xzqn.entity.XzqnAuthContent;
import com.gangukeji.xzqn.entity.XzqnAuthPerson;
import com.gangukeji.xzqn.entity.XzqnAuthSkill;
import com.gangukeji.xzqn.serialize.AuthContentSerialize;
import com.gangukeji.xzqn.serialize.AuthPersonSerialize;
import com.gangukeji.xzqn.serialize.AuthSkillSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: hx
 * @Date: 2019/7/20 16:54
 * @Description: 单条记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthAll {
    private int userId;
    private int statusPerson;
    private int statusSkill;
    private int statusContent;
    private String name;
    private String head_img;
    @JsonSerialize(using = AuthContentSerialize.class)
    private XzqnAuthContent authContent;
    @JsonSerialize(using = AuthSkillSerialize.class)
    private XzqnAuthSkill authSkill;
    @JsonSerialize(using = AuthPersonSerialize.class)
    private XzqnAuthPerson authPerson;
}
