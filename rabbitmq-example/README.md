---
layout: post
tiltle: rabbitmq发布与订阅
no-post-nav: true
category: springboot
tags: [springboot]
---

### rabbitmq发布与订阅

>默认情况下如果一个 Message 被消费者所正确接收则会被从 Queue 中移除
>如果一个 Queue 没被任何消费者订阅，那么这个 Queue 中的消息会被 Cache（缓存），当有消费者订阅时则会立即发送，当 Message 
>被消费者正确接收时，就会被从 Queue 中移除

#### 消息发送确认
##### 发送的消息怎么样才算失败或成功？如何确认？

#### ConfirmCallback

`通过实现 ConfirmCallback 接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange 中`

``` java
@Component
public class RabbitTemplateConfig implements RabbitTemplate.ConfirmCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);            //指定 ConfirmCallback
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("消息唯一标识："+correlationData);
        System.out.println("确认结果："+ack);
        System.out.println("失败原因："+cause);
    }
```

* 还需要在配置文件添加配置
``` yml
spring:
  rabbitmq:
    publisher-confirms: true 
```
#### ReturnCallback

* 通过实现 ReturnCallback 接口，启动消息失败返回，比如路由不到队列时触发回调

``` java
@Component
public class RabbitTemplateConfig implements RabbitTemplate.ReturnCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnCallback(this);             //指定 ReturnCallback
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("消息主体 message : "+message);
        System.out.println("消息主体 message : "+replyCode);
        System.out.println("描述："+replyText);
        System.out.println("消息使用的交换器 exchange : "+exchange);
        System.out.println("消息使用的路由键 routing : "+routingKey);
    }
}
```
* 还需要在配置文件添加配置
``` yml
spring:
  rabbitmq:
    publisher-returns: true 
```

### 消息接收确认

###　消息消费者如何通知 Rabbit 消息消费成功？

*　消息通过 ACK 确认是否被正确接收，每个 Message 都要被确认（acknowledged），可以手动去 ACK 或自动 ACK
*　自动确认会在消息发送给消费者后立即确认，但存在丢失消息的可能，如果消费端消费逻辑抛出异常，也就是消费端没有处理成功这条消息，那么就相当于丢失了消息
*　如果消息已经被处理，但后续代码抛出异常，使用 Spring 进行管理的话消费端业务逻辑会进行回滚，这也同样造成了实际意义的消息丢失
*　如果手动确认则当消费者调用 ack、nack、reject 几种方法进行确认，手动确认可以在业务失败后进行一些操作，如果消息未被 ACK 则会发送到下一个消费者
*　如果某个服务忘记 ACK 了，则 RabbitMQ 不会再发送数据给它，因为 RabbitMQ 认为该服务的处理能力有限
*　ACK 机制还可以起到限流作用，比如在接收到某条消息时休眠几秒钟
消息确认模式有：
**　AcknowledgeMode.NONE：自动确认
**　AcknowledgeMode.AUTO：根据情况确认
**　AcknowledgeMode.MANUAL：手动确认

``` yml
spring:
  rabbitmq:
    listener:
      simple:
        acknowledge-mode: manual
```

``` java
@RabbitHandler
public void processMessage2(String message,Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long tag) {
    System.out.println(message);
    try {
        channel.basicAck(tag,false);            // 确认消息
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```　
需要注意的 basicAck 方法需要传递两个参数
* deliveryTag（唯一标识 ID）：当一个消费者向 RabbitMQ 注册后，会建立起一个 Channel ，RabbitMQ 会用 basic.deliver 方法向消费者推送消息，这个方法携带了一个 delivery tag， 它代表了 RabbitMQ 向该 Channel 投递的这条消息的唯一标识 ID，是一个单调递增的正整数，delivery tag 的范围仅限于 Channel
* multiple：为了减少网络流量，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
