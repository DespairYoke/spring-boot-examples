package com.example.zwd.rabbitmqexample.util;

import com.alibaba.fastjson.JSON;
import com.example.zwd.rabbitmqexample.config.QueueConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zwd
 * @date 2018/11/19 16:02
 * @Email stephen.zwd@gmail.com
 */
@Service
public class HelloSender implements RabbitTemplate.ReturnCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;
    static Logger logger = LoggerFactory.getLogger(HelloSender.class);
    public void send(Object object) {
        logger.info("写入消息队列内容：{}", JSON.toJSONString(object));
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(this);
        this.rabbitTemplate.setConfirmCallback((correlationData,ack,cause)-> {
            if (!ack) {
                logger.info("HelloSender 消息发送失败"+ cause + correlationData.toString());
            }else {
                logger.info("HelloSender 消息发送成功");
            }
        });
        rabbitTemplate.convertAndSend(QueueConstants.MESSAGE_EXCHANGE,QueueConstants.MESSAGE_ROUTE_KEY,object);
    }
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println("zzz");
        logger.info("sender return success" + message.toString()+"==="+i+"==="+s1+"==="+s2);
    }
}
