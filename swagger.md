---
layout: post
title: springboot2.0 整合swagger2
category: springboot
tags: [springboot]
keywords: Spring Cloud,open source
---

### springboot2.0 整合swagger2
#### 依赖引入

``` xml
    <parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.6.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
    </parent>
    <!--swagger2-->
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
        <version>2.8.0</version>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger-ui</artifactId>
        <version>2.8.0</version>
    </dependency>    
```

#### 配置类

``` java
@Configuration
@EnableSwagger2
public class Swagger2 {
    /**
     * 通过 createRestApi函数来构建一个DocketBean
     * 函数名,可以随意命名,喜欢什么命名就什么命名
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())//调用apiInfo方法,创建一个ApiInfo实例,里面是展示在文档页面信息内容
                .select()
                //控制暴露出去的路径下的实例
                //如果某个接口不想暴露,可以使用以下注解
                //@ApiIgnore 这样,该接口就不会暴露在 swagger2 的页面下
                .apis(RequestHandlerSelectors.basePackage("cc.huluwa.electronic.contract.sign.group.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    //构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot Swagger2 构建RESTful API")
                //条款地址
                .termsOfServiceUrl("http://hwangfantasy.github.io/")
                .contact("zwd")
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }
}
```

`@EnableSwagger2` 此注解有时引入失败，请删除`m2/repository/io/springfox/springfox-swagger2`中对应的版本包，我这里是2.8.0

#### 控制层使用
``` java
    @ApiOperation(value = "测试post请求",notes="注意事项",httpMethod = "POST")
    @ApiImplicitParam(dataType = "Department4Info",name = "department4Info",value = "添加部门",required = true)
    @RequestMapping(value = "/adddepartment")
    public String add_department(@RequestBody Department4Info department4Info) {

         RespInfo respInfo = ZwdUtil.ParamIsNull(department4Info,"companyid,userid");
         if (respInfo.getStatus()== InfoCode.ERROR) {
             return JSON.toJSONString(respInfo);
         }
         respInfo = department4Service.adddepartment(department4Info);
        return JSON.toJSONString(respInfo);
    }
```

`swagger是自动检测扫描包下面的所有方法，如果该方法没有使用@ApiIgnore，则表示此方法默认生成swagger`

* @ApiOperation中的`httpMethod`可以决定生成的是何类型的请求，默认是全部类型都生成
* @ApiImplicitParam 是对传递的参数进行描述和填充，可以多重使用
* @ApiIgnore 使用此注解表示该方法不对外暴露
* @JsonIgnore 是用来忽略在参数生成时，屏蔽掉我们不想生成的参数使用。用在实体类的参数上


#### @ApiImplicitParam：
作用在方法上，表示单独的请求参数 
参数： 
1. name ：参数名。 
2. value ： 参数的具体意义，作用。 
3. required ： 参数是否必填。 
4. dataType ：参数的数据类型。 
5. paramType ：查询参数类型，这里有几种形式：


|类型	| 作用 |
|----|-----|
|path |	以地址的形式提交数据|
|query |	直接跟参数完成自动映射赋值|
| body|	以流的形式提交 仅支持POST|
|header|	参数在request headers 里边提交|
|form|	以form表单的形式提交 仅支持POST|
在这里我被坑过一次：当我发POST请求的时候，当时接受的整个参数，不论我用body还是query，后台都会报Body Missing错误。这个参数和SpringMvc中的@RequestBody冲突，索性我就去掉了paramType，对接口测试并没有影响。

#### @ApiImplicitParams：
用于方法，包含多个 @ApiImplicitParam： 
例：
``` java
@ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "book's name", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "book's date", required = false, dataType = "string", paramType = "query")})
            
```
#### @ApiModel：
用于类，表示对类进行说明，用于参数用实体类接收；

#### @ApiModelProperty：
用于方法，字段 ，表示对model属性的说明或者数据操作更改 
例：

``` java
  @ApiModel(value = "User", description = "用户")
    public class User implements Serializable{

    private static final long serialVersionUID = 1546481732633762837L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotEmpty(message = "{id.empty}", groups = {Default.class,New.class,Update.class})
    protected String id;

    /**
     * code/登录帐号
     */
    @ApiModelProperty(value = "code/登录帐号")
    @NotEmpty(message = "{itcode.empty}", groups = {Default.class,New.class,Update.class})
    protected String itcode;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    @NotEmpty(message = "{name.empty}", groups = {Default.class,New.class,Update.class})
    protected String name;
```

#### @ApiOperation：
用于方法，表示一个http请求的操作 。
``` java
 @ApiOperation(value = "获取图书信息", notes = "获取图书信息", response = Book.class, responseContainer = "Item", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "book's name", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "book's date", required = false, dataType = "string", paramType = "query")})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Book getBook(@PathVariable Long id, String date) {
        return books.get(id);
    }
```

#### @ApiResponse：
用于方法，描述操作的可能响应。

#### @ApiResponses：
用于方法，一个允许多个ApiResponse对象列表的包装器。 
例：
``` java
@ApiResponses(value = { 
            @ApiResponse(code = 500, message = "2001:因输入数据问题导致的报错"),
            @ApiResponse(code = 500, message = "403:没有权限"),
            @ApiResponse(code = 500, message = "2500:通用报错（包括数据、逻辑、外键关联等，不区分错误类型）")})
```
#### @ApiParam：
用于方法，参数，字段说明，表示对参数的添加元数据（说明或是否必填等）

#### @Authorization：
声明要在资源或操作上使用的授权方案。

#### @AuthorizationScope：
介绍一个OAuth2授权范围。

#### @ResponseHeader：
响应头设置，使用方法。