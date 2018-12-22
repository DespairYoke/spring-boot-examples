### spring security介绍
Spring Security是一个功能强大且可高度自定义的身份验证和访问控制框架。它是保护基于Spring的应用程序的事实上的标准。

Spring Security是一个专注于为Java应用程序提供身份验证和授权的框架。与所有Spring项目一样，Spring Security的真正强大之处在于它可以轻松扩展以满足自定义要求。
### 特征
* 对身份验证和授权的全面和可扩展的支持
* 防止会话固定，点击劫持，跨站点请求伪造等攻击
* Servlet API集成
* 可选与Spring Web MVC集成
* Much more…


### 自定义User实例
------------
`思路流程` 用户来访问接口时，根据用户携带的Authorization去查询此用户的角色，再根据设置好的角色所具有的权限进行判断，如果访问的接口是该角色下的接口，则进行接口放行。
![](https://upload-images.jianshu.io/upload_images/15204062-1243d257739d8faf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 项目结构
![](https://upload-images.jianshu.io/upload_images/15204062-4d858c1b8301e225.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
###  1、maven依赖
----------
```xml
<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.8.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-actuator</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
		</dependency>

		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt</artifactId>
			<version>0.7.0</version>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
		</dependency>
	</dependencies>
```
#### 2、继承`WebSecurityConfigurerAdapter`配置角色权限
-------------
```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());//passwoldEncoder是对密码的加密处理，如果user中密码没有加密，则可以不加此方法。注意加密请使用security自带的加密方式。
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//禁用了 csrf 功能
                .authorizeRequests()//限定签名成功的请求
                .antMatchers("/decision/**","/govern/**","/employee/*").hasAnyRole("EMPLOYEE","ADMIN")//对decision和govern 下的接口 需要 USER 或者 ADMIN 权限
                .antMatchers("/employee/login").permitAll()///employee/login 不限定
                .antMatchers("/admin/**").hasRole("ADMIN")//对admin下的接口 需要ADMIN权限
                .antMatchers("/oauth/**").permitAll()//不拦截 oauth 开放的资源
                .anyRequest().permitAll()//其他没有限定的请求，允许访问
                .and().anonymous()//对于没有配置权限的其他请求允许匿名访问
                .and().formLogin()//使用 spring security 默认登录页面
                .and().httpBasic();//启用http 基础验证

    }

}
```
此处设置了两个角色：`admin`,`employee`;`admin`可以访问`"/decision/**","/govern/**","/employee/*","/admin/**"`，`employee`可以访问：`"/decision/**","/govern/**","/employee/*"`；从配置上看`admin`权限大于`employee`。


#### 3、创建用户继承`UserDetailsService`
------------
```java
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        //生成环境是查询数据库获取username的角色用于后续权限判断（如：张三 admin)
        //这里不做数据库操作，给定假数据，有兴趣的人可以使内存模式。
        if (username.equals("employee")) {
            Employee employee = new Employee();
            employee.setUsername("employee");
            employee.setPassword("123456");
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_EMPLOYEE");
            grantedAuthorities.add(grantedAuthority);
            //创建一个用户，用于判断权限，请注意此用户名和方法参数中的username一致；BCryptPasswordEncoder是用来演示加密使用。
            return new User(employee.getUsername(), new BCryptPasswordEncoder().encode(employee.getPassword()), grantedAuthorities);
        }
        if (username.equals("admin")) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("123456");
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
            grantedAuthorities.add(grantedAuthority);
            return new User(admin.getUsername(), new BCryptPasswordEncoder().encode(admin.getPassword()), grantedAuthorities);
        }
        else {
            return null;
        }


    }
}
```
此处为了方便，不连接数据库，使用假数据进行模拟，生成环境使用数据替代即可。

#### 4、`employee`、`admin`实体创建
```java
public class Admin {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

public class Employee {
    private String id;
    private String username;
    private String password;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
```
#### 5、测试controller层的代码编写
```java
@RestController
@RequestMapping("/admin")
public class AdminController {


    @GetMapping("/greeting")
    public String greeting() {
        return "Hello,World!";
    }

    @GetMapping("/login")
    public String login() {

        return "login sucess";
    }
}

@RestController
@RequestMapping("/employee")
public class EmployeeController {


    @GetMapping("/greeting")
    public String greeting() {
        return "Hello,World!";
    }

    @GetMapping("/login")
    public String login() {

        return "login sucess";
    }
}
```
#### 最终效果

* `员工访问员工接口`

![](https://upload-images.jianshu.io/upload_images/15204062-c4ece24a7e1b9c43.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

* `员工访问管理员接口`

![](https://upload-images.jianshu.io/upload_images/15204062-1899fb24adf2ac91.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

* `管理员访问管理员接口`
![](https://upload-images.jianshu.io/upload_images/15204062-85c6944c1902e293.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

* `管理员访问员工接口`

![](https://upload-images.jianshu.io/upload_images/15204062-4d92ec3ac458d3f8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 基于内存的实例
--------------
基于内存，表示用户来源于内存；则放弃User使用`auth.inMemoryAuthentication()`
在上述代码中进行更改即可
```java
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());//passwoldEncoder是对密码的加密处理，如果user中密码没有加密，则可以不加此方法。注意加密请使用security自带的加密方式。
        auth.inMemoryAuthentication()
                .withUser("admin").password("123456").roles("ADMIN")
                .and()
                .withUser("employee").password("123456").roles("EMPLOYEE");
    }
```
效果还是和上述相同，一般用来编写demo使用内存模式，生成环境不常使用
### 基于数据库模式
-------------
`原理：`让spring security注入datasouce，再编写sql语句，让spring security内部执行sql进行权限判断，这里不做demo演示。

