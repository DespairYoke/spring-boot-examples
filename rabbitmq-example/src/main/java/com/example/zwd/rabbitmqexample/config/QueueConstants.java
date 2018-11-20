package com.example.zwd.rabbitmqexample.config;

/**
 * @author zwd
 * @date 2018/11/19 14:46
 * @Email stephen.zwd@gmail.com
 */
public interface QueueConstants {
    /**
     * 消息交换
     */
    String MESSAGE_EXCHANGE = "message.direct.exchange";
    /**
     * 消息队列名称
     */
    String MESSAGE_QUEUE_NAME = "message.queue";

    /**
     * 消息路由键
     */
    String MESSAGE_ROUTE_KEY = "message.send";


}
