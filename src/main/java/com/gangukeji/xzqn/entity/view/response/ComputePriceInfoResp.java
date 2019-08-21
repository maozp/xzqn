package com.gangukeji.xzqn.entity.view.response;

import com.gangukeji.xzqn.entity.XzqnServiceFeeItem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: hx
 * @Date: 2019/5/14 9:36
 * @Description:
 */
@Data
@Builder
@Deprecated
public class ComputePriceInfoResp {
    private BigDecimal platformFee;
    private BigDecimal serviceFee;
    private List<XzqnServiceFeeItem> items;
}
