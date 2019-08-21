package com.gangukeji.xzqn.service;


import java.util.Map;

public interface WXservice {
    //统一下单
    Map dounifiedOrder(String user_id,String total_fee,String oid) throws Exception;

    //回调
    String payBack(String notifyData);
}

