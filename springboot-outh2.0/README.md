### outh2.0介绍
现实生活中，我们经常需要授权才能访问第三方信息。
>  借助阮一峰的例子和讲解 http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html

如有一个"云冲印"的网站，可以将用户储存在Google的照片，冲印出来。用户为了使用该服务，必须让"云冲印"读取自己储存在Google上的照片。

![云冲印](http://upload-images.jianshu.io/upload_images/15204062-0b79246bd646248b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

问题是只有得到用户的授权，Google才会同意"云冲印"读取这些照片。那么，"云冲印"怎样获得用户的授权呢？

传统方法是，用户将自己的Google用户名和密码，告诉"云冲印"，后者就可以读取用户的照片了。这样的做法有以下几个严重的缺点。

> （1）"云冲印"为了后续的服务，会保存用户的密码，这样很不安全。
> 
> （2）Google不得不部署密码登录，而我们知道，单纯的密码登录并不安全。
> 
> （3）"云冲印"拥有了获取用户储存在Google所有资料的权力，用户没法限制"云冲印"获得授权的范围和有效期。
> 
> （4）用户只有修改密码，才能收回赋予"云冲印"的权力。但是这样做，会使得其他所有获得用户授权的第三方应用程序全部失效。
> 
> （5）只要有一个第三方应用程序被破解，就会导致用户密码泄漏，以及所有被密码保护的数据泄漏。

OAuth就是为了解决上面这些问题而诞生的。
![image.png](https://upload-images.jianshu.io/upload_images/15204062-ff2250c81eacded7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
更多关于outh2.0的介绍，可查看http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html ；我觉得没有比他介绍更好的了。

-----------------


### 流程介绍
如上述例子所说，如果我们不想把google的用户名和密码给到云冲印，那当我们想访问google上的照片时，云冲印会`携带一个自己申请好的client_id`跳到google认证服务器授权界面，这时，我们先要登录google账号并我们同意授权，google会给云冲印一个`code`，然后云冲印拿到这个`code`再次去认证服务器认证（通俗的说，就是google你小子看好了，这是人家同意的`code`你确认一下）,确认无误时，google返回一个`token`,然后云冲印携带这个`token`访问google上的图片。这样云冲印就拿不到我们的用户名和密码，只是一个有时限的token，当token过期，我们需要重新授权后，才可以访问。

项目地址: https://github.com/DespairYoke/spring-boot-examples/tree/master/springboot-outh2.0

### 项目结构
![image.png](https://upload-images.jianshu.io/upload_images/15204062-974f8520e4a3a36c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
## 基于内存演示授权码模式
### 这里假设localhost的问google
**1、先登录我们google（localhost）设置好的google账号密码(localhost中的)**

![image.png](https://upload-images.jianshu.io/upload_images/15204062-ef429920d5cfccb6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

--------------------
**2、同意授权**
![image.png](https://upload-images.jianshu.io/upload_images/15204062-e39ad207dbac4f69.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

云冲印
---------------------
**3、这里应该是云冲印的回调地址，我用github的代替（现实中显然是谁请求授权，回调给谁。如QQ登录，登录成功后跳转的是请求授权的页面）**
![image.png](https://upload-images.jianshu.io/upload_images/15204062-1ce80314efed051d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
是云冲印(回调地址)拿到我们给他的`code`，这时，我做为一个用户已经做完了我的事情（登录，授权），剩下的事就是你客户端要处理的了。
---------------------
**4、客户端拿到`code`去访问localhost（google）进行code认证**
![](https://upload-images.jianshu.io/upload_images/15204062-4c43bb0bbb93ace2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这时认证成功返回一个`access_token`
-----------------

**5、客户端拿到`access_token进行资源访问（google图片）访问`**
![image.png](https://upload-images.jianshu.io/upload_images/15204062-d9d806d86d876003.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![image.png](https://upload-images.jianshu.io/upload_images/15204062-bc09b8dbcc595c07.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


-------
#### 1、认证服务器搭建（AuthorizationServer）
根据流程可知，云冲印携带自己提前申请好的`client_id`去访问google的服务器，这个服务器就是认证服务器，它会根据携带的`client_id`进行判断是否存在，如果存在则跳转到授权页面。下面我们模拟这个操作。
```java
@Configuration
@EnableAuthorizationServer
public class CustomAuthenticationServerConfig extends AuthorizationServerConfigurerAdapter{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //用来配置令牌端点(Token Endpoint)的安全约束.
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    }

    //用来配置客户端详情服务
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("yunchongyin") //模拟认证服务器存在我们提前申请好的client_id
                .secret(passwordEncoder.encode("123456")) 
                .accessTokenValiditySeconds(7200)
                .authorizedGrantTypes("refresh_token", "password","authorization_code")
                .redirectUris("https://github.com/despairyoke?tab=repositories")
                .scopes("/api/example/hello");
    }

    //来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
```
从代码可以看出我们先要在认证服务器中创建一个用户（client_id）,供后续`携带来的client_id做对比`。
#### 参数介绍
* accessTokenValiditySeconds：有效期时间
* authorizedGrantTypes：可接受的认证类型
*  redirectUris：确认授权后的回调地址
* scopes：表示申请的权限范围，可选项

-------------------

#### AuthenticationManager
```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    //配置内存模式的用户
    @Bean
    @Override
    protected UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("employee").password(new BCryptPasswordEncoder().encode("123456")).authorities("USER").build());
        manager.createUser(User.withUsername("employee1").password(new BCryptPasswordEncoder().encode("123456")).authorities("USER").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .formLogin().and()
                .httpBasic().and()
                .csrf().disable();
    }
}

```
`AuthenticationManager`,`BCryptPasswordEncoder`直接使用自带的类即可，不做深究。
------------------------
 http://localhost:8080/oauth/authorize?response_type=code&client_id=yunchongyin&redirect_uri=https://github.com/despairyoke?tab=repositories&scope=/api/example/hello
> 即携带client_id访问认证服务器
> 参数介绍：
> * response_type：表示授权类型，必选项，此处的值固定为"code"
> * client_id：表示客户端的ID，必选项
> * redirect_uri：表示重定向URI，可选项
> * scope：表示申请的权限范围，可选项
> * state：表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值。

### 2、资源服务器搭建

```java
@Configuration
@EnableResourceServer
public class CustomResourceServerConfig extends ResourceServerConfigurerAdapter {

    public void configure(HttpSecurity http) throws Exception {
        ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated();//允许所有请求
    }

}
```
这里直接放行所有请求，不做介绍，详情请参照[springboot 集成spring security实现权限管理](https://www.jianshu.com/p/6a7dcef02bd5)。
 > `由于资源服务器安全问题，此处请求必须post`

## 密码模式
代码不需要更改
![image.png](https://upload-images.jianshu.io/upload_images/15204062-20a24ff28d2d1527.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
## 流程介绍
>  客户端直接携带密码和账号访问，除非客户端非常可靠，不然不建议使用

![image.png](https://upload-images.jianshu.io/upload_images/15204062-5f3b737561582d00.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)







