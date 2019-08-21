package com.gangukeji.xzqn.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gangukeji.xzqn.entity.XzqnAuthPerson;
import com.gangukeji.xzqn.entity.XzqnAuthSkill;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @Author: hx
 * @Date: 2019/7/19 11:23
 * @Description:
 */
public class AuthPersonSerialize extends JsonSerializer<XzqnAuthPerson> {
    @Override
    public void serialize(XzqnAuthPerson value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gen.writeStartObject();
        gen.writeNumberField("userId", value.getUserId());
        gen.writeStringField("createTime", dateFormat.format(value.getCreateTime()));
        Integer isCheck = value.getIsCheck();
        String isCheckStr="未知状态";
        String clickDes="未知状态";
        if (isCheck == 9) {
            isCheckStr = "未通过";
        }
        if (isCheck == 0) {
            isCheckStr = "未认证";
        }
        if (isCheck == 1) {
            isCheckStr = "待审核";
            clickDes = "去审核";
        }
        if (isCheck == 2) {
            isCheckStr = "已审核";
            clickDes = "详情";
        }
        gen.writeStringField("clickDes", clickDes);
        gen.writeStringField("isCheck", isCheckStr);
        gen.writeStringField("idCardName", value.getIdCardName());
        gen.writeStringField("idCardImgP", value.getIdCardImgP());
        gen.writeStringField("idCardImgB", value.getIdCardImgB());
        gen.writeStringField("idCardNum", value.getIdCardNum());
        gen.writeStringField("personalImg", value.getPersonalImg());
        gen.writeStringField("phone", value.getPhone());
        gen.writeStringField("refuseReason", value.getRefuseReason());
        gen.writeEndObject();
    }
}
