package com.gangukeji.xzqn.service;


import java.util.Map;

public interface WXservice {
    //统一下单
    Map dounifiedOrder(Integer order_id,String total_fee) throws Exception;

    //回调
    String payBack(String notifyData);
}

