package com.example.zwd.rabbitmqexample.controller;

import com.example.zwd.rabbitmqexample.entity.MessageEntity;
import com.example.zwd.rabbitmqexample.util.HelloSender;
import com.example.zwd.rabbitmqexample.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zwd
 * @date 2018/11/19 15:50
 * @Email stephen.zwd@gmail.com
 */
@RestController
public class TestController {
//    /**
//     * 消息队列 - 消息提供者 注入
//     */
//    @Autowired
//    private MessageProvider messageProvider;
//    /**
//     * 测试发送消息队列方法
//     *
//     * @param messageEntity 发送消息实体内容
//     * @return
//     */
//    @RequestMapping(value = "/index")
//    public String index(MessageEntity messageEntity) {
//        // 将实体实例写入消息队列
//        messageProvider.sendMessage(messageEntity);
//        return "Success";
//    }

    @Autowired
    private HelloSender helloSender;

    @RequestMapping(value = "/index")
    public String sayhello (MessageEntity messageEntity) {
        // 将实体实例写入消息队列
        helloSender.send(messageEntity);
        return "Success";
    }
}
