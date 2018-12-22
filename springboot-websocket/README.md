### WebSocket和http的区别？
* 1. http协议是用在应用层的协议，他是基于tcp协议的，http协议建立链接也必须要有三次握手才能发送信息。

>　　http链接分为短链接，长链接，短链接是每次请求都要三次握手才能发送自己的信息。即每一个request对应一个response。长链接是在一定的期限内保持链接。保持TCP连接不断开。客户端与服务器通信，必须要有客户端发起然后服务器返回结果。客户端是主动的，服务器是被动的。

* 2. WebSocket 

>　　WebSocket他是为了解决客户端发起多个http请求到服务器资源浏览器必须要经过长时间的轮训问题而生的，他实现了多路复用，他是全双工通信。在webSocket协议下客服端和浏览器可以同时发送信息。

建立了WenSocket之后服务器不必在浏览器发送request请求之后才能发送信息到浏览器。这时的服务器已有主动权想什么时候发就可以发送信息到服务器。而且信息当中不必在带有head的部分信息了与http的长链接通信来说，这种方式，不仅能降低服务器的压力。而且信息当中也减少了部分多余的信息。

--------

#### 实例
----------
* 项目结构

![](https://upload-images.jianshu.io/upload_images/15204062-6b47f95465aea807.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


--------------

##### maven依赖

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.6.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- websocket dependency -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
    </dependencies>
```

----------------

##### 核心代码
```java
@ServerEndpoint(value = "/ws/asset")
@Component
public class WebSocketServer {

    @PostConstruct
    public void init() {
        System.out.println("websocket 加载");
    }
    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        SessionSet.add(session);
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入，当前连接数为：{}", cnt);
        SendMessage(session, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SessionSet.remove(session);
        int cnt = OnlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息：{}",message);
        SendMessage(session, "收到消息，消息内容："+message);

    }

    /**
     * 出现错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     * @param session
     * @param message
     */
    public static void SendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)",message,session.getId()));
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     * @param message
     * @throws IOException
     */
    public static void BroadCastInfo(String message) throws IOException {
        for (Session session : SessionSet) {
            if(session.isOpen()){
                SendMessage(session, message);
            }
        }
    }

    /**
     * 指定Session发送消息
     * @param sessionId
     * @param message
     * @throws IOException
     */
    public static void SendMessage(String message,String sessionId) throws IOException {
        Session session = null;
        for (Session s : SessionSet) {
            if(s.getId().equals(sessionId)){
                session = s;
                break;
            }
        }
        if(session!=null){
            SendMessage(session, message);
        }
        else{
            log.warn("没有找到你指定ID的会话：{}",sessionId);
        }
    }

}
```
--------------------

##### ServerEndpointExporter对象创建

```java
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter () {
        return new ServerEndpointExporter();
    }
}

```
-------

##### 测试Controller

```java
@RestController
@RequestMapping("/api/ws")
public class WebSocketController {


    /**
     * 群发消息内容
     * @param message
     * @return
     */
    @RequestMapping(value="/sendAll", method= RequestMethod.GET)
    public String sendAllMessage(@RequestParam(required=true) String message){
        try {
            WebSocketServer.BroadCastInfo(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }
    
    /**
     * 指定会话ID发消息
     * @param message 消息内容
     * @param id 连接会话ID
     * @return
     */
    @RequestMapping(value="/sendOne", method=RequestMethod.GET)
    public String sendOneMessage(@RequestParam(required=true) String message,@RequestParam(required=true) String id){
        try {
            WebSocketServer.SendMessage(message,id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
```

-------

##### index.html

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>websocket测试</title>
    <style type="text/css">
        h3,h4{
            text-align:center;
        }
    </style>
</head>
<body>

<h3>WebSocket测试，在<span style="color:red">控制台</span>查看测试信息输出！</h3>
<h4>
    [url=/api/ws/sendOne?message=单发消息内容&id=none]单发消息链接[/url]
    [url=/api/ws/sendAll?message=群发消息内容]群发消息链接[/url]
</h4>


<script type="text/javascript">
    var socket;
    if (typeof (WebSocket) == "undefined") {
        console.log("遗憾：您的浏览器不支持WebSocket");
    } else {
        console.log("恭喜：您的浏览器支持WebSocket");

        //实现化WebSocket对象
        //指定要连接的服务器地址与端口建立连接
        //注意ws、wss使用不同的端口。我使用自签名的证书测试，
        //无法使用wss，浏览器打开WebSocket时报错
        //ws对应http、wss对应https。
        socket = new WebSocket("ws://localhost:8080/ws/asset");
        //连接打开事件
        socket.onopen = function() {
            console.log("Socket 已打开");
            socket.send("消息发送测试(From Client)");
![QQ20181219-141341.png](https://upload-images.jianshu.io/upload_images/15204062-39b01834b9749043.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
        };
        //收到消息事件
        socket.onmessage = function(msg) {
            console.log(msg.data);
        };
        socket.
        //连接关闭事件
        socket.onclose = function() {
            console.log("Socket已关闭");
        };
        //发生了错误事件
        socket.onerror = function() {
            alert("Socket发生了错误");
        }

        //窗口关闭时，关闭连接
        window.unload=function() {
            socket.close();
        };
    }
</script>

</body>
</html>
```

启动项目，打开index.html文件
##### 最终效果
* 后台
![](https://upload-images.jianshu.io/upload_images/15204062-93eb1c00b6809856.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

* 浏览器
![](https://upload-images.jianshu.io/upload_images/15204062-5ed31187841cae4e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



`携带sessionid访问controller接口`

![](https://upload-images.jianshu.io/upload_images/15204062-b0652fa4a8d13364.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


查看浏览器控制台

![](https://upload-images.jianshu.io/upload_images/15204062-c9a285cd5fc80746.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


说明后台推送的消息，接受成功。
