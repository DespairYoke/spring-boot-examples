---
layout: post
title: springboot配置文件详解
category: springboot
tags: [springboot]
---
### 自定义属性与加载

``` xml
zwd.name=zwd
zwd.password=123456
```
然后通过`@Value("${属性名}")`注解来加载对应的配置属性，具体如下：
``` java
@Component
public class UserProperties {

    @Value("${zwd.name}")
    private String username;
    @Value("${zwd.password}")
    private String password;
    
    //省略getter setter
}
```

### 参数间的引用

在`在application.properties`中的各参数间也可以直接引用来使用，具体如下：
``` YAML
zwd.name=zwd
zwd.password=123456
zwd.content= name:${zwd.name},password:${zwd.password}
```

### 使用随机数
--------

``` YAML
# 随机字符串
zwd.blog.value=${random.value}
# 随机int
zwd.blog.number=${random.int}
# 随机long
zwd.blog.bignumber=${random.long}
# 10以内的随机数
zwd.blog.test1=${random.int(10)}
# 10-20的随机数
zwd..blog.test2=${random.int[10,20]}

```
具体使用如下：
``` java
    @Value("${zwd.blog.value}")
    private String isString;
    @Value("${zwd.blog.number}")
    private Integer number;
```
## 配置文件的选择
项目从开发到测试到生产，这些流程如果都使用同一个数据库的话，显然是不合理的；所以我们应该有多个配置文件供选择。
`application-dev.properties`
``` js
server.port=8083
```
`application-test.properties`
``` js
server.port=8082
```
`application-prod.properties`
``` js
server.port=8081
```
这里我创建了三个配置文件做为演示，配置文件的内容如上,只是做了简单的启动端口替换。文件的选择是通过`applicaiton.properties`来控制，我具体代码如下：
``` yml
spring.profiles.active=test
```
想要切换为dev时，只需修改`test`为`dev`即可
