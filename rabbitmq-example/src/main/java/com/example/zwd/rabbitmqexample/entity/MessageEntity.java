package com.example.zwd.rabbitmqexample.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zwd
 * @date 2018/11/19 15:22
 * @Email stephen.zwd@gmail.com
 */
@Data
public class MessageEntity implements Serializable {
    /**
     * 消息内容
     */
    private String content;
}
