package com.example.zwd.rabbitmqexample.util;

import com.alibaba.fastjson.JSON;
import com.example.zwd.rabbitmqexample.config.QueueConstants;
import com.example.zwd.rabbitmqexample.entity.MessageEntity;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ReturnCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author zwd
 * @date 2018/11/19 16:14
 * @Email stephen.zwd@gmail.com
 */
@Service
@RabbitListener(queues = QueueConstants.MESSAGE_QUEUE_NAME)
public class HelloReceiver {

    Logger logger = LoggerFactory.getLogger(HelloReceiver.class);

    @RabbitHandler
    public void handler(@Payload MessageEntity messageEntity, Channel channel, Message message) {
        logger.info("消费内容：{}", JSON.toJSONString(messageEntity));
        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            e.printStackTrace();
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            logger.info("receiver fail");
        }
    }

}
