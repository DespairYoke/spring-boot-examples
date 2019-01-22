### springboot2.0 集成spring-data-jpa

对于熟悉hibernate的人，都知道使用orm的好处，这里我用spring-data-jpa的原因是因为我发现在写测试demo的时候都需要进行建库建表等一系列流程操作，这些操作使本来很简单的一个demo变的复杂化，所以我引入spring-data-jpa的orm机制，并使用create-drop，这样就脱离了对数据库的操作。


#### maven依赖
```xml
       <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
```
实体类

#### 配置文件
application.properties
```
spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.jpa.properties.hibernate.hbm2ddl.auto=create-drop
```
`spring.jpa.properties.hibernate.hbm2ddl.auto`是hibernate的配置属性，其主要作用是：自动创建、更新、验证数据库表结构。该参数的几种配置如下：

`create`：每次加载hibernate时都会删除上一次的生成的表，然后根据你的model类再重新来生成新表，哪怕两次没有任何改变也要这样执行，这就是导致数据库表数据丢失的一个重要原因。
`create-drop`：每次加载hibernate时根据model类生成表，但是sessionFactory一关闭,表就自动删除。
`update`：最常用的属性，第一次加载hibernate时根据model类会自动建立起表的结构（前提是先建立好数据库），以后加载hibernate时根据model类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行。要注意的是当部署到服务器后，表结构是不会被马上建立起来的，是要等应用第一次运行起来后才会。
validate：每次加载hibernate时，验证创建数据库表结构，只会和数据库中的表进行比较，不会创建新表，但是会插入新值。

#### 实体类
```java
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;
   //省略构造
  //省略getset
}
```
此处要注意，必须保留一个空的构造函数，不让数据库会生成失败。

#### 查询接口
```java
public interface UserRepository extends JpaRepository<User,Long> {


    User findByName(String name);

    User findByNameAndAge(String name,Integer age);

    @Query("from User u where u.name =:name")
    User findUser(@Param("name") String name);

}
```

#### 测试示例
```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {
    @Autowired
    private UserRepository userRepository;


    @Test
    public void test() {

        // 创建10条记录
        userRepository.save(new User("AAA", 10));
        userRepository.save(new User("BBB", 20));
        userRepository.save(new User("CCC", 30));
        userRepository.save(new User("DDD", 40));
        userRepository.save(new User("EEE", 50));
        userRepository.save(new User("FFF", 60));
        userRepository.save(new User("GGG", 70));
        userRepository.save(new User("HHH", 80));
        userRepository.save(new User("III", 90));
        userRepository.save(new User("JJJ", 100));

        User aaa = userRepository.findByName("AAA");
        System.out.println(aaa.toString());

        User bbb = userRepository.findByNameAndAge("BBB", 20);
        System.out.println(bbb);

        User ccc = userRepository.findUser("CCC");
        System.out.println(ccc);
    }
}

```