package com.example.websocketdemo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

/**
 * @author zwd
 * @date 2018/8/27 17:00
 * @Email stephen.zwd@gmail.com
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
//    }
//
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//    }
//
//    public void configureClientOutboundChannel(ChannelRegistration registration) {
//    }
//
//    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
//        return true;
//    }
//
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//    }
//
//    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
//    }

    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/my-websocket").withSockJS();
    }
}
