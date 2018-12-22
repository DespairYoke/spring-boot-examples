### jwt和传统session的区别？
#### 传统的session认证
    
> 1、用户向服务器发送用户名和密码。
>
> 2、服务器验证通过后，在当前对话（session）里面保存相关数据，比如用户角色、登录时间等等。
>
> 3、服务器向用户返回一个 session_id，写入用户的 Cookie。
>
> 4、用户随后的每一次请求，都会通过 Cookie，将 session_id 传回服务器。
>
> 5、服务器收到 session_id，找到前期保存的数据，由此得知用户的身份。

##### 基于session认证所显露的问题。

这种模式的问题在于，扩展性（scaling）不好。单机当然没有问题，如果是服务器集群，或者是跨域的服务导向架构，就要求 session 数据共享，每台服务器都能够读取 session。

举例来说，A 网站和 B 网站是同一家公司的关联服务。现在要求，用户只要在其中一个网站登录，再访问另一个网站就会自动登录，请问怎么实现？

一种解决方案是 session 数据持久化，写入数据库或别的持久层。各种服务收到请求后，都向持久层请求数据。这种方案的优点是架构清晰，缺点是工程量比较大。另外，持久层万一挂了，就会单点失败。

另一种方案是服务器索性不保存 session 数据了，所有数据都保存在客户端，每次请求都发回服务器。JWT 就是这种方案的一个代表。


#### 基于token的鉴权机制
基于token的鉴权机制类似于http协议也是无状态的，它不需要在服务端去保留用户的认证信息或者会话信息。这就意味着基于token认证机制的应用不需要去考虑用户在哪一台服务器登录了，这就为应用的扩展提供了便利。

流程上是这样的：

* 用户使用用户名密码来请求服务器
* 服务器进行验证用户的信息
* 服务器通过验证发送给用户一个token
* 客户端存储token，并在每次请求时附送上这个token值
* 服务端验证token值，并返回数据

`这个token必须要在每次请求时传递给服务端，它应该保存在请求头里， 另外，服务端要支持CORS(跨来源资源共享)策略，一般我们在服务端这么做就可以了Access-Control-Allow-Origin: *。`

#### JWT长什么样？
JWT是由三段信息构成的，将这三段信息文本用.链接一起就构成了Jwt字符串。就像这样:
`eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ`

* 第一部分我们称它为头部（header)
* 第二部分我们称其为载荷（payload, 类似于飞机上承载的物品)
* 第三部分是签证（signature).

#### header
--------
jwt的头部承载两部分信息：
```java
   {
    'typ': 'JWT',
    'alg': 'HS256'
   }
```
> `声明类型`，这里是jwt
> `声明加密的算法` 通常直接使用 HMAC SHA256



然后将头部进行base64加密（该加密是可以对称解密的),构成了第一部分.

eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
#### playload
--------

载荷就是存放有效信息的地方。这个名字像是特指飞机上承载的货品，这些有效信息包含三个部分

* 标准中注册的声明
* 公共的声明
* 私有的声明
* 标准中注册的声明 (建议但不强制使用) ：

> iss: jwt签发者
> sub: 主题
> aud: 接收jwt的一方
> exp: jwt的过期时间，这个过期时间必须要大于签发时间
> nbf: 生效时间
> iat: 签发时间
> jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。

* 公共的声明 ：
公共的声明可以添加任何的信息，一般添加用户的相关信息或其他业务需要的必要信息.但不建议添加敏感信息，因为该部分在客户端可解密.

* 私有的声明 ：
私有声明是提供者和消费者所共同定义的声明，一般不建议存放敏感信息，因为base64是对称解密的，意味着该部分信息可以归类为明文信息。

定义一个payload:
```java
{
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}
```

然后将其进行base64加密，得到Jwt的第二部分。

eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9
signature
#### Signature
---------

* header (base64后的)
* payload (base64后的)
* secret
这个部分需要base64加密后的header和base64加密后的payload使用.连接组成的字符串，然后通过header中声明的加密方式进行加盐secret组合加密，然后就构成了jwt的第三部分。
将这三部分用.连接成一个完整的字符串,构成了最终的jwt

```xml
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ
```
`注意：`secret是保存在服务器端的，jwt的签发生成也是在服务器端的，secret就是用来进行jwt的签发和jwt的验证，所以，它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。

#### jjwt如何应用
--------

我们如果把token理解问一个携带信息的加密字符，那大致可以分为3个步骤
* 向token中加入信息
* 把信息加密
* 解密获取信息

 ##### 项目结构
 ![](https://upload-images.jianshu.io/upload_images/15204062-49582671b99fd969.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 
 ##### jwt核心代码
```java
public class JwtHelper {

    /**
     * token 过期时间, 单位: 秒. 这个值表示 30 天
     */
    private static final long TOKEN_EXPIRED_TIME = 30 * 24 * 60 * 60;

    /**
     * jwt 加密解密密钥
     */
    private static final String JWT_SECRET = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";

    public static final String jwtId = "tokenId";
    /**
     * 创建JWT
     */
    public static String createJWT(Map<String, Object> claims, Long time) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        Date now = new Date(System.currentTimeMillis());

        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(jwtId)                  //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           //iat: jwt的签发时间
                .signWith(signatureAlgorithm, secretKey);//设置签名使用的签名算法和签名使用的秘钥
        if (time >= 0) {
            long expMillis = nowMillis + time;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);     //设置过期时间
        }
        return builder.compact();
    }

    /**
     * 验证jwt
     */
    public static Claims verifyJwt(String token) {
        //签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key = generalKey();
        Claims claims;
        try {
            claims = Jwts.parser()  //得到DefaultJwtParser
                    .setSigningKey(key)         //设置签名的秘钥
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }//设置需要解析的jwt
        return claims;

    }


    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        String stringKey = JWT_SECRET;
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 根据userId和openid生成token
     */
    public static String generateToken(String openId, Integer userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("openId", openId);
        return createJWT(map, TOKEN_EXPIRED_TIME);
    }

}
```

##### Controller层
-------
```java
@RestController
public class LoginController {

    @RequestMapping("/user/login")
    public String login() {
        
        String jwtToken = JwtHelper.generateToken("123",456);

        return jwtToken;
    }

    @RequestMapping("user/hello")
    public String user(){

        return   "hello";
    }
}
```

##### 过滤器的使用
-------
```java
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request =(HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String token = request.getHeader("authorization"); //获取请求传来的token
        Claims claims = JwtHelper.verifyJwt(token); //验证token
        if (claims == null) {  
            response.getWriter().write("token is invalid");
        }else {
            filterChain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }
}
```

##### 过滤器的加载
-------
```java
@Configuration
public class BeanRegisterConfig {

    @Bean
    public FilterRegistrationBean createFilterBean() {
        //过滤器注册类
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new MyFilter());
        registration.addUrlPatterns("/user/hello"); //需要过滤的接口
        return registration;
    }
}
```
`启动项目：` 访问`localhost:8080/user/hello`
如下图：

![](https://upload-images.jianshu.io/upload_images/15204062-7bc40818f1110e4b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


`携带token访问/user/hello`
![](https://upload-images.jianshu.io/upload_images/15204062-8f586aa472f9c1d5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)




