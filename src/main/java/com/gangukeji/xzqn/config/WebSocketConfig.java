package com.gangukeji.xzqn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 *
 * @author mao
 * 2019-8-7 21:26
 */
@CrossOrigin("*")
@Component //这是一个组件类，写上这个注解，SpringBoot会在启动时自动加载
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
