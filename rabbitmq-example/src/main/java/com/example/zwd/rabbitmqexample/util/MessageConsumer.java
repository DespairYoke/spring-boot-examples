package com.example.zwd.rabbitmqexample.util;

import com.alibaba.fastjson.JSON;
import com.example.zwd.rabbitmqexample.config.QueueConstants;
import com.example.zwd.rabbitmqexample.entity.MessageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author zwd
 * @date 2018/11/19 15:42
 * @Email stephen.zwd@gmail.com
 */
//@Component
//@RabbitListener(queues = QueueConstants.MESSAGE_QUEUE_NAME)
public class MessageConsumer {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @RabbitHandler
    public void handler(@Payload MessageEntity messageEntity) {
        logger.info("消费内容：{}", JSON.toJSONString(messageEntity));
    }

}
