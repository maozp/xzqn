package com.gangukeji.xzqn.serialize;

/**
 * @Author: hx
 * @Date: 2019/7/3 13:42
 * @Description:
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 返回对象保留2位小数处理
 */
public class CustomerBigDecimalSerialize extends JsonSerializer<BigDecimal> {

    private DecimalFormat df = new DecimalFormat("0.00");
    @Override
    public void serialize(BigDecimal arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
        if(arg0 != null) {
            arg1.writeString(df.format(arg0));
        }
    }

}
