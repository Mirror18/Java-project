# 第一个spring Boot 程序

要了解Spring Boot，我们先来编写第一个Spring Boot应用程序，看看与前面我们编写的Spring应用程序有何异同。

我们新建一个`springboot-hello`的工程，创建标准的Maven目录结构如下：

```ascii
springboot-hello
├── pom.xml
├── src
│   └── main
│       ├── java
│       └── resources
│           ├── application.yml
│           ├── logback-spring.xml
│           ├── static
│           └── templates
└── target
```

其中，在`src/main/resources`目录下，注意到几个文件：

### application.yml

这是Spring Boot默认的配置文件，它采用[YAML](https://yaml.org/)格式而不是`.properties`格式，文件名必须是`application.yml`而不是其他名称。

YAML格式比`key=value`格式的`.properties`文件更易读。比较一下两者的写法：

使用`.properties`格式：

```
# application.properties

spring.application.name=${APP_NAME:unnamed}

spring.datasource.url=jdbc:hsqldb:file:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.hsqldb.jdbc.JDBCDriver

spring.datasource.hikari.auto-commit=false
spring.datasource.hikari.connection-timeout=3000
spring.datasource.hikari.validation-timeout=3000
spring.datasource.hikari.max-lifetime=60000
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=1
```

使用YAML格式：

```
# application.yml

spring:
  application:
    name: ${APP_NAME:unnamed}
  datasource:
    url: jdbc:hsqldb:file:testdb
    username: sa
    password:
    driver-class-name: org.hsqldb.jdbc.JDBCDriver
    hikari:
      auto-commit: false
      connection-timeout: 3000
      validation-timeout: 3000
      max-lifetime: 60000
      maximum-pool-size: 20
      minimum-idle: 1
```

可见，YAML是一种层级格式，它和`.properties`很容易互相转换，它的优点是去掉了大量重复的前缀，并且更加易读。

也可以使用application.properties作为配置文件，但不如YAML格式简单。

### 使用环境变量

在配置文件中，我们经常使用如下的格式对某个key进行配置：

```
app:
  db:
    host: ${DB_HOST:localhost}
    user: ${DB_USER:root}
    password: ${DB_PASSWORD:password}
```

这种`${DB_HOST:localhost}`意思是，首先从环境变量查找`DB_HOST`，如果环境变量定义了，那么使用环境变量的值，否则，使用默认值`localhost`。

这使得我们在开发和部署时更加方便，因为开发时无需设定任何环境变量，直接使用默认值即本地数据库，而实际线上运行的时候，只需要传入环境变量即可：

```
$ DB_HOST=10.0.1.123 DB_USER=prod DB_PASSWORD=xxxx java -jar xxx.jar
```

### logback-spring.xml

这是Spring Boot的logback配置文件名称（也可以使用`logback.xml`），一个标准的写法如下：

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml" />

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
            <charset>utf8</charset>
        </encoder>
    </appender>

    <appender name="APP_LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <encoder>
            <pattern>${FILE_LOG_PATTERN}</pattern>
            <charset>utf8</charset>
        </encoder>
          <file>app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
            <maxIndex>1</maxIndex>
            <fileNamePattern>app.log.%i</fileNamePattern>
        </rollingPolicy>
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>1MB</MaxFileSize>
        </triggeringPolicy>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="APP_LOG" />
    </root>
</configuration>
```

它主要通过`<include resource="..." />`引入了Spring Boot的一个缺省配置，这样我们就可以引用类似`${CONSOLE_LOG_PATTERN}`这样的变量。上述配置定义了一个控制台输出和文件输出，可根据需要修改。

`static`是静态文件目录，`templates`是模板文件目录，注意它们不再存放在`src/main/webapp`下，而是直接放到`src/main/resources`这个classpath目录，因为在Spring Boot中已经不需要专门的webapp目录了。

以上就是Spring Boot的标准目录结构，它完全是一个基于Java应用的普通Maven项目。

我们再来看源码目录结构：

```ascii
src/main/java
└── com
    └── itranswarp
        └── learnjava
            ├── Application.java
            ├── entity
            │   └── User.java
            ├── service
            │   └── UserService.java
            └── web
                └── UserController.java
```

在存放源码的`src/main/java`目录中，Spring Boot对Java包的层级结构有一个要求。注意到我们的根package是`com.itranswarp.learnjava`，下面还有`entity`、`service`、`web`等子package。Spring Boot要求`main()`方法所在的启动类必须放到根package下，命名不做要求，这里我们以`Application.java`命名，它的内容如下：

```
@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
```

启动Spring Boot应用程序只需要一行代码加上一个注解`@SpringBootApplication`，该注解实际上又包含了：

* @SpringBootConfiguration
  * @Configuration
* @EnableAutoConfiguration
  * @AutoConfigurationPackage
* @ComponentScan

这样一个注解就相当于启动了自动配置和自动扫描。

我们再观察`pom.xml`，它的内容如下：

```
<project ...>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.0.0</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.itranswarp.learnjava</groupId>
    <artifactId>springboot-hello</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <java.version>17</java.version>
        <pebble.version>3.2.0</pebble.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>

        <!-- 集成Pebble View -->
        <dependency>
            <groupId>io.pebbletemplates</groupId>
            <artifactId>pebble-spring-boot-starter</artifactId>
            <version>${pebble.version}</version>
        </dependency>

        <!-- JDBC驱动 -->
        <dependency>
            <groupId>org.hsqldb</groupId>
            <artifactId>hsqldb</artifactId>
        </dependency>
    </dependencies>
</project>
```

使用Spring Boot时，强烈推荐从`spring-boot-starter-parent`继承，因为这样就可以引入Spring Boot的预置配置。

紧接着，我们引入了依赖`spring-boot-starter-web`和`spring-boot-starter-jdbc`，它们分别引入了Spring MVC相关依赖和Spring JDBC相关依赖，无需指定版本号，因为引入的`<parent>`内已经指定了，只有我们自己引入的某些第三方jar包需要指定版本号。这里我们引入`pebble-spring-boot-starter`作为View，以及`hsqldb`作为嵌入式数据库。`hsqldb`已在`spring-boot-starter-jdbc`中预置了版本号`3.0.0`，因此此处无需指定版本号。

根据`pebble-spring-boot-starter`的[文档](https://pebbletemplates.io/wiki/guide/spring-boot-integration/)，加入如下配置到`application.yml`：

```
pebble:
  # 默认为".peb"，改为"":
  suffix:
  # 开发阶段禁用模板缓存:
  cache: false
```

对`Application`稍作改动，添加`WebMvcConfigurer`这个Bean：

```
@SpringBootApplication
public class Application {
    ...

    @Bean
    WebMvcConfigurer createWebMvcConfigurer(@Autowired HandlerInterceptor[] interceptors) {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // 映射路径`/static/`到classpath路径:
                registry.addResourceHandler("/static/**")
                        .addResourceLocations("classpath:/static/");
            }
        };
    }
}
```

现在就可以直接运行`Application`，启动后观察Spring Boot的日志：

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.0)

2022-11-25T10:49:31.100+08:00  INFO 13105 --- [           main] com.itranswarp.learnjava.Application     : Starting Application using Java 17 with PID 13105 (/Users/liaoxuefeng/Git/springboot-hello/target/classes started by liaoxuefeng in /Users/liaoxuefeng/Git/springboot-hello)
2022-11-25T10:49:31.107+08:00  INFO 13105 --- [           main] com.itranswarp.learnjava.Application     : No active profile set, falling back to 1 default profile: "default"
2022-11-25T10:49:32.404+08:00  INFO 13105 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2022-11-25T10:49:32.423+08:00  INFO 13105 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2022-11-25T10:49:32.426+08:00  INFO 13105 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.1]
2022-11-25T10:49:32.549+08:00  INFO 13105 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2022-11-25T10:49:32.551+08:00  INFO 13105 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1327 ms
2022-11-25T10:49:32.668+08:00  WARN 13105 --- [           main] com.zaxxer.hikari.HikariConfig           : HikariPool-1 - idleTimeout is close to or more than maxLifetime, disabling it.
2022-11-25T10:49:32.669+08:00  INFO 13105 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-11-25T10:49:32.996+08:00  INFO 13105 --- [           main] com.zaxxer.hikari.pool.PoolBase          : HikariPool-1 - Driver does not support get/set network timeout for connections. (feature not supported)
2022-11-25T10:49:32.998+08:00  INFO 13105 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection org.hsqldb.jdbc.JDBCConnection@31a2a9fa
2022-11-25T10:49:33.002+08:00  INFO 13105 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2022-11-25T10:49:33.391+08:00  WARN 13105 --- [           main] ocalVariableTableParameterNameDiscoverer : Using deprecated '-debug' fallback for parameter name resolution. Compile the affected code with '-parameters' instead or avoid its introspection: io.pebbletemplates.boot.autoconfigure.PebbleServletWebConfiguration
2022-11-25T10:49:33.398+08:00  WARN 13105 --- [           main] ocalVariableTableParameterNameDiscoverer : Using deprecated '-debug' fallback for parameter name resolution. Compile the affected code with '-parameters' instead or avoid its introspection: io.pebbletemplates.boot.autoconfigure.PebbleAutoConfiguration
2022-11-25T10:49:33.619+08:00  INFO 13105 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-11-25T10:49:33.637+08:00  INFO 13105 --- [           main] com.itranswarp.learnjava.Application     : Started Application in 3.151 seconds (process running for 3.835)
```

Spring Boot自动启动了嵌入式Tomcat，当看到`Started Application in xxx seconds`时，Spring Boot应用启动成功。

现在，我们在浏览器输入`localhost:8080`就可以直接访问页面。那么问题来了：

前面我们定义的数据源、声明式事务、JdbcTemplate在哪创建的？怎么就可以直接注入到自己编写的`UserService`中呢？

这些自动创建的Bean就是Spring Boot的特色：AutoConfiguration。

当我们引入`spring-boot-starter-jdbc`时，启动时会自动扫描所有的`XxxAutoConfiguration`：

* `DataSourceAutoConfiguration`：自动创建一个`DataSource`，其中配置项从`application.yml`的`spring.datasource`读取；
* `DataSourceTransactionManagerAutoConfiguration`：自动创建了一个基于JDBC的事务管理器；
* `JdbcTemplateAutoConfiguration`：自动创建了一个`JdbcTemplate`。

因此，我们自动得到了一个`DataSource`、一个`DataSourceTransactionManager`和一个`JdbcTemplate`。

类似的，当我们引入`spring-boot-starter-web`时，自动创建了：

* `ServletWebServerFactoryAutoConfiguration`：自动创建一个嵌入式Web服务器，默认是Tomcat；
* `DispatcherServletAutoConfiguration`：自动创建一个`DispatcherServlet`；
* `HttpEncodingAutoConfiguration`：自动创建一个`CharacterEncodingFilter`；
* `WebMvcAutoConfiguration`：自动创建若干与MVC相关的Bean。
* ...

引入第三方`pebble-spring-boot-starter`时，自动创建了：

* `PebbleAutoConfiguration`：自动创建了一个`PebbleViewResolver`。

Spring Boot大量使用`XxxAutoConfiguration`来使得许多组件被自动化配置并创建，而这些创建过程又大量使用了Spring的Conditional功能。例如，我们观察`JdbcTemplateAutoConfiguration`，它的代码如下：

```
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ DataSource.class, JdbcTemplate.class })
@ConditionalOnSingleCandidate(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(JdbcProperties.class)
@Import({ JdbcTemplateConfiguration.class, NamedParameterJdbcTemplateConfiguration.class })
public class JdbcTemplateAutoConfiguration {
}
```

当满足条件：

* `@ConditionalOnClass`：在classpath中能找到`DataSource`和`JdbcTemplate`；
* `@ConditionalOnSingleCandidate(DataSource.class)`：在当前Bean的定义中能找到唯一的`DataSource`；

该`JdbcTemplateAutoConfiguration`就会起作用。实际创建由导入的`JdbcTemplateConfiguration`完成：

```
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(JdbcOperations.class)
class JdbcTemplateConfiguration {
    @Bean
    @Primary
    JdbcTemplate jdbcTemplate(DataSource dataSource, JdbcProperties properties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcProperties.Template template = properties.getTemplate();
        jdbcTemplate.setFetchSize(template.getFetchSize());
        jdbcTemplate.setMaxRows(template.getMaxRows());
        if (template.getQueryTimeout() != null) {
            jdbcTemplate.setQueryTimeout((int) template.getQueryTimeout().getSeconds());
        }
        return jdbcTemplate;
    }
}
```

创建`JdbcTemplate`之前，要满足`@ConditionalOnMissingBean(JdbcOperations.class)`，即不存在`JdbcOperations`的Bean。

如果我们自己创建了一个`JdbcTemplate`，例如，在`Application`中自己写个方法：

```
@SpringBootApplication
public class Application {
    ...
    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
```

那么根据条件`@ConditionalOnMissingBean(JdbcOperations.class)`，Spring Boot就不会再创建一个重复的`JdbcTemplate`（因为`JdbcOperations`是`JdbcTemplate`的父类）。

可见，Spring Boot自动装配功能是通过自动扫描+条件装配实现的，这一套机制在默认情况下工作得很好，但是，如果我们要手动控制某个Bean的创建，就需要详细地了解Spring Boot自动创建的原理，很多时候还要跟踪`XxxAutoConfiguration`，以便设定条件使得某个Bean不会被自动创建。

### 练习

从[![]()](https://gitee.com/)下载练习：[使用Spring Boot编写hello应用程序](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/220.Spring%20Boot%E5%BC%80%E5%8F%91.1266265175882464/10.%E7%AC%AC%E4%B8%80%E4%B8%AASpring%20Boot%E5%BA%94%E7%94%A8.1282386201411617/springboot-hello.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

Spring Boot是一个基于Spring提供了开箱即用的一组套件，它可以让我们基于很少的配置和代码快速搭建出一个完整的应用程序。

Spring Boot有非常强大的AutoConfiguration功能，它是通过自动扫描+条件装配实现的。

## 使用开发者工具

在开发阶段，我们经常要修改代码，然后重启Spring Boot应用。经常手动停止再启动，比较麻烦。

Spring Boot提供了一个开发者工具，可以监控classpath路径上的文件。只要源码或配置文件发生修改，Spring Boot应用可以自动重启。在开发阶段，这个功能比较有用。

要使用这一开发者功能，我们只需添加如下依赖到`pom.xml`：

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>
```

然后，没有然后了。直接启动应用程序，然后试着修改源码，保存，观察日志输出，Spring Boot会自动重新加载。

默认配置下，针对`/static`、`/public`和`/templates`目录中的文件修改，不会自动重启，因为禁用缓存后，这些文件的修改可以实时更新。

### 练习

从[![]()](https://gitee.com/)下载练习：[使用devtools检测修改并自动重启](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/220.Spring%20Boot%E5%BC%80%E5%8F%91.1266265175882464/20.%E4%BD%BF%E7%94%A8%E5%BC%80%E5%8F%91%E8%80%85%E5%B7%A5%E5%85%B7.1282386532761633/springboot-devtools.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

Spring Boot提供了一个开发阶段非常有用的`spring-boot-devtools`，能自动检测classpath路径上文件修改并自动重启。

## 打包 应用

我们在Maven的[使用插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1309301217951777)一节中介绍了如何使用`maven-shade-plugin`打包一个可执行的jar包。在Spring Boot应用中，打包更加简单，因为Spring Boot自带一个更简单的`spring-boot-maven-plugin`插件用来打包，我们只需要在`pom.xml`中加入以下配置：

```
<project ...>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

无需任何配置，Spring Boot的这款插件会自动定位应用程序的入口Class，我们执行以下Maven命令即可打包：

```
$ mvn clean package
```

以`springboot-exec-jar`项目为例，打包后我们在`target`目录下可以看到两个jar文件：

```
$ ls
classes
generated-sources
maven-archiver
maven-status
springboot-exec-jar-1.0-SNAPSHOT.jar
springboot-exec-jar-1.0-SNAPSHOT.jar.original
```

其中，`springboot-exec-jar-1.0-SNAPSHOT.jar.original`是Maven标准打包插件打的jar包，它只包含我们自己的Class，不包含依赖，而`springboot-exec-jar-1.0-SNAPSHOT.jar`是Spring Boot打包插件创建的包含依赖的jar，可以直接运行：

```
$ java -jar springboot-exec-jar-1.0-SNAPSHOT.jar
```

这样，部署一个Spring Boot应用就非常简单，无需预装任何服务器，只需要上传jar包即可。

在打包的时候，因为打包后的Spring Boot应用不会被修改，因此，默认情况下，`spring-boot-devtools`这个依赖不会被打包进去。但是要注意，使用早期的Spring Boot版本时，需要配置一下才能排除`spring-boot-devtools`这个依赖：

```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <excludeDevtools>true</excludeDevtools>
    </configuration>
</plugin>
```

如果不喜欢默认的项目名+版本号作为文件名，可以加一个配置指定文件名：

```
<project ...>
    ...
    <build>
        <finalName>awesome-app</finalName>
        ...
    </build>
</project>
```

这样打包后的文件名就是`awesome-app.jar`。

### 练习

从[![]()](https://gitee.com/)下载练习：[使用Spring Boot插件打包可执行jar](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/220.Spring%20Boot%E5%BC%80%E5%8F%91.1266265175882464/30.%E6%89%93%E5%8C%85Spring%20Boot%E5%BA%94%E7%94%A8.1282386595676193/springboot-exec-jar.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

Spring Boot提供了一个Maven插件用于打包所有依赖到单一jar文件，此插件十分易用，无需配置。

## 打包瘦身

在上一节中，我们使用Spring Boot提供的`spring-boot-maven-plugin`打包Spring Boot应用，可以直接获得一个完整的可运行的jar包，把它上传到服务器上再运行就极其方便。

但是这种方式也不是没有缺点。最大的缺点就是包太大了，动不动几十MB，在网速不给力的情况下，上传服务器非常耗时。并且，其中我们引用到的Tomcat、Spring和其他第三方组件，只要版本号不变，这些jar就相当于每次都重复打进去，再重复上传了一遍。

真正经常改动的代码其实是我们自己编写的代码。如果只打包我们自己编写的代码，通常jar包也就几百KB。但是，运行的时候，classpath中没有依赖的jar包，肯定会报错。

所以问题来了：如何只打包我们自己编写的代码，同时又自动把依赖包下载到某处，并自动引入到classpath中。解决方案就是使用`spring-boot-thin-launcher`。

### 使用spring-boot-thin-launcher

我们先演示如何使用spring-boot-thin-launcher，再详细讨论它的工作原理。

首先复制一份上一节的Maven项目，并重命名为`springboot-thin-jar`：

```
<project ...>
    ...
    <groupId>com.itranswarp.learnjava</groupId>
    <artifactId>springboot-thin-jar</artifactId>
    <version>1.0-SNAPSHOT</version>
    ...
```

然后，修改`<build>`-`<plugins>`-`<plugin>`，给原来的`spring-boot-maven-plugin`增加一个`<dependency>`如下：

```
<project ...>
    ...
    <build>
        <finalName>awesome-app</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.boot.experimental</groupId>
                        <artifactId>spring-boot-thin-layout</artifactId>
                        <version>1.0.27.RELEASE</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
</project>
```

不需要任何其他改动了，我们直接按正常的流程打包，执行`mvn clean package`，观察`target`目录最终生成的可执行`awesome-app.jar`，只有79KB左右。

直接运行`java -jar awesome-app.jar`，效果和上一节完全一样。显然，79KB的jar肯定无法放下Tomcat和Spring这样的大块头。那么，运行时这个`awesome-app.jar`又是怎么找到它自己依赖的jar包呢？

实际上`spring-boot-thin-launcher`这个插件改变了`spring-boot-maven-plugin`的默认行为。它输出的jar包只包含我们自己代码编译后的class，一个很小的`ThinJarWrapper`，以及解析`pom.xml`后得到的所有依赖jar的列表。

运行的时候，入口实际上是`ThinJarWrapper`，它会先在指定目录搜索看看依赖的jar包是否都存在，如果不存在，先从Maven中央仓库下载到本地，然后，再执行我们自己编写的`main()`入口方法。这种方式有点类似很多在线安装程序：用户下载后得到的是一个很小的exe安装程序，执行安装程序时，会首先在线下载所需的若干巨大的文件，再进行真正的安装。

这个`spring-boot-thin-launcher`在启动时搜索的默认目录是用户主目录的`.m2`，我们也可以指定下载目录，例如，将下载目录指定为当前目录：

```
$ java -Dthin.root=. -jar awesome-app.jar
```

上述命令通过环境变量`thin.root`传入当前目录，执行后发现当前目录下自动生成了一个`repository`目录，这和Maven的默认下载目录`~/.m2/repository`的结构是完全一样的，只是它仅包含`awesome-app.jar`所需的运行期依赖项。

注意：只有首次运行时会自动下载依赖项，再次运行时由于无需下载，所以启动速度会大大加快。如果删除了repository目录，再次运行时就会再次触发下载。

### 预热

把79KB大小的`awesome-app.jar`直接扔到服务器执行，上传过程就非常快。但是，第一次在服务器上运行`awesome-app.jar`时，仍需要从Maven中央仓库下载大量的jar包，所以，`spring-boot-thin-launcher`还提供了一个`dryrun`选项，专门用来下载依赖项而不执行实际代码：

```
java -Dthin.dryrun=true -Dthin.root=. -jar awesome-app.jar
```

执行上述代码会在当前目录创建`repository`目录，并下载所有依赖项，但并不会运行我们编写的`main()`方法。此过程称之为“预热”（warm up）。

如果服务器由于安全限制不允许从外网下载文件，那么可以在本地预热，然后把`awesome-app.jar`和`repository`目录上传到服务器。只要依赖项没有变化，后续改动只需要上传`awesome-app.jar`即可。

### 练习

从[![]()](https://gitee.com/)下载练习：[瘦身Spring Boot的可执行jar包](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/220.Spring%20Boot%E5%BC%80%E5%8F%91.1266265175882464/35.%E7%98%A6%E8%BA%ABSpring%20Boot%E5%BA%94%E7%94%A8.1304267002478625/springboot-thin-jar.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

利用`spring-boot-thin-launcher`可以给Spring Boot应用瘦身。其原理是记录app依赖的jar包，在首次运行时先下载依赖项并缓存到本地。

## 使用Actuator 就是监控

在生产环境中，需要对应用程序的状态进行监控。前面我们已经介绍了使用JMX对Java应用程序包括JVM进行监控，使用JMX需要把一些监控信息以MBean的形式暴露给JMX Server，而Spring Boot已经内置了一个监控功能，它叫Actuator。

使用Actuator非常简单，只需添加如下依赖：

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

然后正常启动应用程序，Actuator会把它能收集到的所有信息都暴露给JMX。此外，Actuator还可以通过URL`/actuator/`挂载一些监控点，例如，输入`http://localhost:8080/actuator/health`，我们可以查看应用程序当前状态：

```
{
    "status": "UP"
}
```

许多网关作为反向代理需要一个URL来探测后端集群应用是否存活，这个URL就可以提供给网关使用。

Actuator默认把所有访问点暴露给JMX，但处于安全原因，只有`health`和`info`会暴露给Web。Actuator提供的所有访问点均在官方文档列出，要暴露更多的访问点给Web，需要在`application.yml`中加上配置：

```
management:
  endpoints:
    web:
      exposure:
        include: info, health, beans, env, metrics
```

要特别注意暴露的URL的安全性，例如，`/actuator/env`可以获取当前机器的所有环境变量，不可暴露给公网。

### 练习

从[![]()](https://gitee.com/)下载练习：[使用Actuator实现监控](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/220.Spring%20Boot%E5%BC%80%E5%8F%91.1266265175882464/40.%E4%BD%BF%E7%94%A8Actuator.1282386381766689/springboot-actuator.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

Spring Boot提供了一个Actuator，可以方便地实现监控，并可通过Web访问特定类型的监控。

## 使用profiles

Profile本身是Spring提供的功能，我们在[使用条件装配](https://www.liaoxuefeng.com/wiki/1252599548343744/1308043874664482)中已经讲到了，Profile表示一个环境的概念，如开发、测试和生产这3个环境：

* native
* test
* production

或者按git分支定义master、dev这些环境：

* master
* dev

在启动一个Spring应用程序的时候，可以传入一个或多个环境，例如：

```
-Dspring.profiles.active=test,master
```

大多数情况下，使用一个环境就足够了。

Spring Boot对Profiles的支持在于，可以在`application.yml`中为每个环境进行配置。下面是一个示例配置：

```
spring:
  application:
    name: ${APP_NAME:unnamed}
  datasource:
    url: jdbc:hsqldb:file:testdb
    username: sa
    password:
    dirver-class-name: org.hsqldb.jdbc.JDBCDriver
    hikari:
      auto-commit: false
      connection-timeout: 3000
      validation-timeout: 3000
      max-lifetime: 60000
      maximum-pool-size: 20
      minimum-idle: 1

pebble:
  suffix:
  cache: false

server:
  port: ${APP_PORT:8080}

---

spring:
  config:
    activate:
      on-profile: test

server:
  port: 8000

---

spring:
  config:
    activate:
      on-profile: production

server:
  port: 80

pebble:
  cache: true
```

注意到分隔符`---`，最前面的配置是默认配置，不需要指定Profile，后面的每段配置都必须以`spring.config.activate.on-profile: xxx`开头，表示一个Profile。上述配置默认使用8080端口，但是在`test`环境下，使用`8000`端口，在`production`环境下，使用`80`端口，并且启用Pebble的缓存。

如果我们不指定任何Profile，直接启动应用程序，那么Profile实际上就是`default`，可以从Spring Boot启动日志看出：

```
...
2022-11-25T11:10:34.006+08:00  INFO 13537 --- [           main] com.itranswarp.learnjava.Application     : No active profile set, falling back to 1 default profile: "default"
```

上述日志显示未设置Profile，使用默认的Profile为`default`。

要以`test`环境启动，可输入如下命令：

```
$ java -Dspring.profiles.active=test -jar springboot-profiles-1.0-SNAPSHOT.jar

...
2022-11-25T11:09:02.946+08:00  INFO 13510 --- [           main] com.itranswarp.learnjava.Application     : The following 1 profile is active: "test"
...
2022-11-25T11:09:05.124+08:00  INFO 13510 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8000 (http) with context path ''
...
```

从日志看到活动的Profile是`test`，Tomcat的监听端口是`8000`。

通过Profile可以实现一套代码在不同环境启用不同的配置和功能。假设我们需要一个存储服务，在本地开发时，直接使用文件存储即可，但是，在测试和生产环境，需要存储到云端如S3上，如何通过Profile实现该功能？

首先，我们要定义存储接口`StorageService`：

```
public interface StorageService {

    // 根据URI打开InputStream:
    InputStream openInputStream(String uri) throws IOException;

    // 根据扩展名+InputStream保存并返回URI:
    String store(String extName, InputStream input) throws IOException;
}
```

本地存储可通过`LocalStorageService`实现：

```
@Component
@Profile("default")
public class LocalStorageService implements StorageService {
    @Value("${storage.local:/var/static}")
    String localStorageRootDir;

    final Logger logger = LoggerFactory.getLogger(getClass());

    private File localStorageRoot;

    @PostConstruct
    public void init() {
        logger.info("Intializing local storage with root dir: {}", this.localStorageRootDir);
        this.localStorageRoot = new File(this.localStorageRootDir);
    }

    @Override
    public InputStream openInputStream(String uri) throws IOException {
        File targetFile = new File(this.localStorageRoot, uri);
        return new BufferedInputStream(new FileInputStream(targetFile));
    }

    @Override
    public String store(String extName, InputStream input) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + extName;
        File targetFile = new File(this.localStorageRoot, fileName);
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(targetFile))) {
            input.transferTo(output);
        }
        return fileName;
    }
}
```

而云端存储可通过`CloudStorageService`实现：

```
@Component
@Profile("!default")
public class CloudStorageService implements StorageService {
    @Value("${storage.cloud.bucket:}")
    String bucket;

    @Value("${storage.cloud.access-key:}")
    String accessKey;

    @Value("${storage.cloud.access-secret:}")
    String accessSecret;

    final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void init() {
        // TODO:
        logger.info("Initializing cloud storage...");
    }

    @Override
    public InputStream openInputStream(String uri) throws IOException {
        // TODO:
        throw new IOException("File not found: " + uri);
    }

    @Override
    public String store(String extName, InputStream input) throws IOException {
        // TODO:
        throw new IOException("Unable to access cloud storage.");
    }
}
```

注意到`LocalStorageService`使用了条件装配`@Profile("default")`，即默认启用`LocalStorageService`，而`CloudStorageService`使用了条件装配`@Profile("!default")`，即非`default`环境时，自动启用`CloudStorageService`。这样，一套代码，就实现了不同环境启用不同的配置。

### 练习

从[![]()](https://gitee.com/)下载练习：[使用Profile启动Spring Boot应用](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/220.Spring%20Boot%E5%BC%80%E5%8F%91.1266265175882464/50.%E4%BD%BF%E7%94%A8Profiles.1282388483112993/springboot-profiles.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

Spring Boot允许在一个配置文件中针对不同Profile进行配置；

Spring Boot在未指定Profile时默认为`default`。

## conditional，条件装配

使用Profile能根据不同的Profile进行条件装配，但是Profile控制比较糙，如果想要精细控制，例如，配置本地存储，AWS存储和阿里云存储，将来很可能会增加Azure存储等，用Profile就很难实现。

Spring本身提供了条件装配`@Conditional`，但是要自己编写比较复杂的`Condition`来做判断，比较麻烦。Spring Boot则为我们准备好了几个非常有用的条件：

* @ConditionalOnProperty：如果有指定的配置，条件生效；
* @ConditionalOnBean：如果有指定的Bean，条件生效；
* @ConditionalOnMissingBean：如果没有指定的Bean，条件生效；
* @ConditionalOnMissingClass：如果没有指定的Class，条件生效；
* @ConditionalOnWebApplication：在Web环境中条件生效；
* @ConditionalOnExpression：根据表达式判断条件是否生效。

我们以最常用的`@ConditionalOnProperty`为例，把上一节的`StorageService`改写如下。首先，定义配置`storage.type=xxx`，用来判断条件，默认为`local`：

```
storage:
  type: ${STORAGE_TYPE:local}
```

设定为`local`时，启用`LocalStorageService`：

```
@Component
@ConditionalOnProperty(value = "storage.type", havingValue = "local", matchIfMissing = true)
public class LocalStorageService implements StorageService {
    ...
}
```

设定为`aws`时，启用`AwsStorageService`：

```
@Component
@ConditionalOnProperty(value = "storage.type", havingValue = "aws")
public class AwsStorageService implements StorageService {
    ...
}
```

设定为`aliyun`时，启用`AliyunStorageService`：

```
@Component
@ConditionalOnProperty(value = "storage.type", havingValue = "aliyun")
public class AliyunStorageService implements StorageService {
    ...
}
```

注意到`LocalStorageService`的注解，当指定配置为`local`，或者配置不存在，均启用`LocalStorageService`。

可见，Spring Boot提供的条件装配使得应用程序更加具有灵活性。

### 练习

从[![]()](https://gitee.com/)下载练习：[使用Spring Boot提供的条件装配](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/220.Spring%20Boot%E5%BC%80%E5%8F%91.1266265175882464/60.%E4%BD%BF%E7%94%A8Conditional.1282386318852129/springboot-conditional.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

Spring Boot提供了几个非常有用的条件装配注解，可实现灵活的条件装配。

## 加载配置文件

加载配置文件可以直接使用注解`@Value`，例如，我们定义了一个最大允许上传的文件大小配置：

```
storage:
  local:
    max-size: 102400
```

在某个FileUploader里，需要获取该配置，可使用`@Value`注入：

```
@Component
public class FileUploader {
    @Value("${storage.local.max-size:102400}")
    int maxSize;

    ...
}
```

在另一个`UploadFilter`中，因为要检查文件的MD5，同时也要检查输入流的大小，因此，也需要该配置：

```
@Component
public class UploadFilter implements Filter {
    @Value("${storage.local.max-size:100000}")
    int maxSize;

    ...
}
```

多次引用同一个`@Value`不但麻烦，而且`@Value`使用字符串，缺少编译器检查，容易造成多处引用不一致（例如，`UploadFilter`把缺省值误写为`100000`）。

为了更好地管理配置，Spring Boot允许创建一个Bean，持有一组配置，并由Spring Boot自动注入。

假设我们在`application.yml`中添加了如下配置：

```
storage:
  local:
    # 文件存储根目录:
    root-dir: ${STORAGE_LOCAL_ROOT:/var/storage}
    # 最大文件大小，默认100K:
    max-size: ${STORAGE_LOCAL_MAX_SIZE:102400}
    # 是否允许空文件:
    allow-empty: false
    # 允许的文件类型:
    allow-types: jpg, png, gif
```

可以首先定义一个Java Bean，持有该组配置：

```
public class StorageConfiguration {

    private String rootDir;
    private int maxSize;
    private boolean allowEmpty;
    private List<String> allowTypes;

    // TODO: getters and setters
}
```

保证Java Bean的属性名称与配置一致即可。然后，我们添加两个注解：

```
@Configuration
@ConfigurationProperties("storage.local")
public class StorageConfiguration {
    ...
}
```

注意到`@ConfigurationProperties("storage.local")`表示将从配置项`storage.local`读取该项的所有子项配置，并且，`@Configuration`表示`StorageConfiguration`也是一个Spring管理的Bean，可直接注入到其他Bean中：

```
@Component
public class StorageService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    StorageConfiguration storageConfig;

    @PostConstruct
    public void init() {
        logger.info("Load configuration: root-dir = {}", storageConfig.getRootDir());
        logger.info("Load configuration: max-size = {}", storageConfig.getMaxSize());
        logger.info("Load configuration: allowed-types = {}", storageConfig.getAllowTypes());
    }
}
```

这样一来，引入`storage.local`的相关配置就很容易了，因为只需要注入`StorageConfiguration`这个Bean，这样可以由编译器检查类型，无需编写重复的`@Value`注解。

### 练习

从[![]()](https://gitee.com/)下载练习：[加载配置文件](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/220.Spring%20Boot%E5%BC%80%E5%8F%91.1266265175882464/70.%E5%8A%A0%E8%BD%BD%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6.1304267426103329/springboot-configuration.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

Spring Boot提供了`@ConfigurationProperties`注解，可以非常方便地把一段配置加载到一个Bean中。

## 禁用自动配置

Spring Boot大量使用自动配置和默认配置，极大地减少了代码，通常只需要加上几个注解，并按照默认规则设定一下必要的配置即可。例如，配置JDBC，默认情况下，只需要配置一个`spring.datasource`：

```
spring:
  datasource:
    url: jdbc:hsqldb:file:testdb
    username: sa
    password:
    dirver-class-name: org.hsqldb.jdbc.JDBCDriver
```

Spring Boot就会自动创建出`DataSource`、`JdbcTemplate`、`DataSourceTransactionManager`，非常方便。

但是，有时候，我们又必须要禁用某些自动配置。例如，系统有主从两个数据库，而Spring Boot的自动配置只能配一个，怎么办？

这个时候，针对`DataSource`相关的自动配置，就必须关掉。我们需要用`exclude`指定需要关掉的自动配置：

```
@SpringBootApplication
// 启动自动配置，但排除指定的自动配置:
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class Application {
    ...
}
```

现在，Spring Boot不再给我们自动创建`DataSource`、`JdbcTemplate`和`DataSourceTransactionManager`了，要实现主从数据库支持，怎么办？

让我们一步一步开始编写支持主从数据库的功能。首先，我们需要把主从数据库配置写到`application.yml`中，仍然按照Spring Boot默认的格式写，但`datasource`改为`datasource-master`和`datasource-slave`：

```
spring:
  datasource-master:
    url: jdbc:hsqldb:file:testdb
    username: sa
    password:
    dirver-class-name: org.hsqldb.jdbc.JDBCDriver
  datasource-slave:
    url: jdbc:hsqldb:file:testdb
    username: sa
    password:
    dirver-class-name: org.hsqldb.jdbc.JDBCDriver
```

注意到两个数据库实际上是同一个库。如果使用MySQL，可以创建一个只读用户，作为`datasource-slave`的用户来模拟一个从库。

下一步，我们分别创建两个HikariCP的`DataSource`：

```
public class MasterDataSourceConfiguration {
    @Bean("masterDataSourceProperties")
    @ConfigurationProperties("spring.datasource-master")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("masterDataSource")
    DataSource dataSource(@Autowired @Qualifier("masterDataSourceProperties") DataSourceProperties props) {
        return props.initializeDataSourceBuilder().build();
    }
}

public class SlaveDataSourceConfiguration {
    @Bean("slaveDataSourceProperties")
    @ConfigurationProperties("spring.datasource-slave")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("slaveDataSource")
    DataSource dataSource(@Autowired @Qualifier("slaveDataSourceProperties") DataSourceProperties props) {
        return props.initializeDataSourceBuilder().build();
    }
}
```

注意到上述class并未添加`@Configuration`和`@Component`，要使之生效，可以使用`@Import`导入：

```
@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@Import({ MasterDataSourceConfiguration.class, SlaveDataSourceConfiguration.class})
public class Application {
    ...
}
```

此外，上述两个`DataSource`的Bean名称分别为`masterDataSource`和`slaveDataSource`，我们还需要一个最终的`@Primary`标注的`DataSource`，它采用Spring提供的`AbstractRoutingDataSource`，代码实现如下：

```
class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        // 从ThreadLocal中取出key:
        return RoutingDataSourceContext.getDataSourceRoutingKey();
    }
}
```

`RoutingDataSource`本身并不是真正的`DataSource`，它通过Map关联一组`DataSource`，下面的代码创建了包含两个`DataSource`的`RoutingDataSource`，关联的key分别为`masterDataSource`和`slaveDataSource`：

```
public class RoutingDataSourceConfiguration {
    @Primary
    @Bean
    DataSource dataSource(
            @Autowired @Qualifier("masterDataSource") DataSource masterDataSource,
            @Autowired @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        var ds = new RoutingDataSource();
        // 关联两个DataSource:
        ds.setTargetDataSources(Map.of(
                "masterDataSource", masterDataSource,
                "slaveDataSource", slaveDataSource));
        // 默认使用masterDataSource:
        ds.setDefaultTargetDataSource(masterDataSource);
        return ds;
    }

    @Bean
    JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    DataSourceTransactionManager dataSourceTransactionManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

仍然需要自己创建`JdbcTemplate`和`PlatformTransactionManager`，注入的是标记为`@Primary`的`RoutingDataSource`。

这样，我们通过如下的代码就可以切换`RoutingDataSource`底层使用的真正的`DataSource`：

```
RoutingDataSourceContext.setDataSourceRoutingKey("slaveDataSource");
jdbcTemplate.query(...);
```

只不过写代码切换DataSource即麻烦又容易出错，更好的方式是通过注解配合AOP实现自动切换，这样，客户端代码实现如下：

```
@Controller
public class UserController {
	@RoutingWithSlave // <-- 指示在此方法中使用slave数据库
	@GetMapping("/profile")
	public ModelAndView profile(HttpSession session) {
        ...
    }
}
```

实现上述功能需要编写一个`@RoutingWithSlave`注解，一个AOP织入和一个`ThreadLocal`来保存key。由于代码比较简单，这里我们不再详述。

如果我们想要确认是否真的切换了`DataSource`，可以覆写`determineTargetDataSource()`方法并打印出`DataSource`的名称：

```
class RoutingDataSource extends AbstractRoutingDataSource {
    ...

    @Override
    protected DataSource determineTargetDataSource() {
        DataSource ds = super.determineTargetDataSource();
        logger.info("determin target datasource: {}", ds);
        return ds;
    }
}
```

访问不同的URL，可以在日志中看到两个`DataSource`，分别是`HikariPool-1`和`hikariPool-2`：

```
2020-06-14 17:55:21.676  INFO 91561 --- [nio-8080-exec-7] c.i.learnjava.config.RoutingDataSource   : determin target datasource: HikariDataSource (HikariPool-1)
2020-06-14 17:57:08.992  INFO 91561 --- [io-8080-exec-10] c.i.learnjava.config.RoutingDataSource   : determin target datasource: HikariDataSource (HikariPool-2)
```

我们用一个图来表示创建的DataSource以及相关Bean的关系：

```ascii
┌────────────────────┐       ┌──────────────────┐
│@Primary            │<──────│   JdbcTemplate   │
│RoutingDataSource   │       └──────────────────┘
│ ┌────────────────┐ │       ┌──────────────────┐
│ │MasterDataSource│ │<──────│DataSource        │
│ └────────────────┘ │       │TransactionManager│
│ ┌────────────────┐ │       └──────────────────┘
│ │SlaveDataSource │ │
│ └────────────────┘ │
└────────────────────┘
```

注意到`DataSourceTransactionManager`和`JdbcTemplate`引用的都是`RoutingDataSource`，所以，这种设计的一个限制就是：在一个请求中，一旦切换了内部数据源，在同一个事务中，不能再切到另一个，否则，`DataSourceTransactionManager`和`JdbcTemplate`操作的就不是同一个数据库连接。

### 练习

从[![]()](https://gitee.com/)下载练习：[禁用DataSourceAutoConfiguration并配置多数据源](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/220.Spring%20Boot%E5%BC%80%E5%8F%91.1266265175882464/80.%E7%A6%81%E7%94%A8%E8%87%AA%E5%8A%A8%E9%85%8D%E7%BD%AE.1282389045149729/springboot-multi-datasource.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

可以通过`@EnableAutoConfiguration(exclude = {...})`指定禁用的自动配置；

可以通过`@Import({...})`导入自定义配置。


## 添加 Filter


我们在Spring中已经学过了[集成Filter](https://www.liaoxuefeng.com/wiki/1252599548343744/1282384114745378)，本质上就是通过代理，把Spring管理的Bean注册到Servlet容器中，不过步骤比较繁琐，需要配置`web.xml`。

在Spring Boot中，添加一个`Filter`更简单了，可以做到零配置。我们来看看在Spring Boot中如何添加`Filter`。

Spring Boot会自动扫描所有的`FilterRegistrationBean`类型的Bean，然后，将它们返回的`Filter`自动注册到Servlet容器中，无需任何配置。

我们还是以`AuthFilter`为例，首先编写一个`AuthFilterRegistrationBean`，它继承自`FilterRegistrationBean`：

```
@Component
public class AuthFilterRegistrationBean extends FilterRegistrationBean<Filter> {
    @Autowired
    UserService userService;

    @Override
    public Filter getFilter() {
        setOrder(10);
        return new AuthFilter();
    }

    class AuthFilter implements Filter {
        ...
    }
}
```

`FilterRegistrationBean`本身不是`Filter`，它实际上是`Filter`的工厂。Spring Boot会调用`getFilter()`，把返回的`Filter`注册到Servlet容器中。因为我们可以在`FilterRegistrationBean`中注入需要的资源，然后，在返回的`AuthFilter`中，这个内部类可以引用外部类的所有字段，自然也包括注入的`UserService`，所以，整个过程完全基于Spring的IoC容器完成。

再注意到`AuthFilterRegistrationBean`使用了`setOrder(10)`，因为Spring Boot支持给多个`Filter`排序，数字小的在前面，所以，多个`Filter`的顺序是可以固定的。

我们再编写一个`ApiFilter`，专门过滤`/api/*`这样的URL。首先编写一个`ApiFilterRegistrationBean`

```
@Component
public class ApiFilterRegistrationBean extends FilterRegistrationBean<Filter> {
    @PostConstruct
    public void init() {
        setOrder(20);
        setFilter(new ApiFilter());
        setUrlPatterns(List.of("/api/*"));
    }

    class ApiFilter implements Filter {
        ...
    }
}
```

这个`ApiFilterRegistrationBean`和`AuthFilterRegistrationBean`又有所不同。因为我们要过滤URL，而不是针对所有URL生效，因此，在`@PostConstruct`方法中，通过`setFilter()`设置一个`Filter`实例后，再调用`setUrlPatterns()`传入要过滤的URL列表。

### 练习

从[![]()](https://gitee.com/)下载练习：[添加Filter并指定顺序](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/220.Spring%20Boot%E5%BC%80%E5%8F%91.1266265175882464/90.%E6%B7%BB%E5%8A%A0Filter.1282389221310497/springboot-filter.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

在Spring Boot中添加`Filter`更加方便，并且支持对多个`Filter`进行排序。
