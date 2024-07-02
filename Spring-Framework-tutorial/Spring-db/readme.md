# 访问数据库

数据库基本上是现代应用程序的标准存储，绝大多数程序都把自己的业务数据存储在关系数据库中，可见，访问数据库几乎是所有应用程序必备能力。

我们在前面已经介绍了Java程序访问数据库的标准接口JDBC，它的实现方式非常简洁，即：Java标准库定义接口，各数据库厂商以“驱动”的形式实现接口。应用程序要使用哪个数据库，就把该数据库厂商的驱动以jar包形式引入进来，同时自身仅使用JDBC接口，编译期并不需要特定厂商的驱动。

使用JDBC虽然简单，但代码比较繁琐。Spring为了简化数据库访问，主要做了以下几点工作：

* 提供了简化的访问JDBC的模板类，不必手动释放资源；
* 提供了一个统一的DAO类以实现Data Access Object模式；
* 把`SQLException`封装为`DataAccessException`，这个异常是一个`RuntimeException`，并且让我们能区分SQL异常的原因，例如，`DuplicateKeyException`表示违反了一个唯一约束；
* 能方便地集成Hibernate、JPA和MyBatis这些数据库访问框架。


## 使用JDBC


我们在前面介绍[JDBC编程](https://www.liaoxuefeng.com/wiki/1252599548343744/1255943820274272)时已经讲过，Java程序使用JDBC接口访问关系数据库的时候，需要以下几步：

* 创建全局`DataSource`实例，表示数据库连接池；
* 在需要读写数据库的方法内部，按如下步骤访问数据库：
  * 从全局`DataSource`实例获取`Connection`实例；
  * 通过`Connection`实例创建`PreparedStatement`实例；
  * 执行SQL语句，如果是查询，则通过`ResultSet`读取结果集，如果是修改，则获得`int`结果。

正确编写JDBC代码的关键是使用`try ... finally`释放资源，涉及到事务的代码需要正确提交或回滚事务。

在Spring使用JDBC，首先我们通过IoC容器创建并管理一个`DataSource`实例，然后，Spring提供了一个`JdbcTemplate`，可以方便地让我们操作JDBC，因此，通常情况下，我们会实例化一个`JdbcTemplate`。顾名思义，这个类主要使用了[Template模式](https://www.liaoxuefeng.com/wiki/1252599548343744/1281319636041762)。

编写示例代码或者测试代码时，我们强烈推荐使用[HSQLDB](http://hsqldb.org/)这个数据库，它是一个用Java编写的关系数据库，可以以内存模式或者文件模式运行，本身只有一个jar包，非常适合演示代码或者测试代码。

我们以实际工程为例，先创建Maven工程`spring-data-jdbc`，然后引入以下依赖：

* org.springframework:spring-context:6.0.0
* org.springframework:spring-jdbc:6.0.0
* jakarta.annotation:jakarta.annotation-api:2.1.1
* com.zaxxer:HikariCP:5.0.1
* org.hsqldb:hsqldb:2.7.1

在`AppConfig`中，我们需要创建以下几个必须的Bean：

```
@Configuration
@ComponentScan
@PropertySource("jdbc.properties")
public class AppConfig {

    @Value("${jdbc.url}")
    String jdbcUrl;

    @Value("${jdbc.username}")
    String jdbcUsername;

    @Value("${jdbc.password}")
    String jdbcPassword;

    @Bean
    DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
```

在上述配置中：

1. 通过`@PropertySource("jdbc.properties")`读取数据库配置文件；
2. 通过`@Value("${jdbc.url}")`注入配置文件的相关配置；
3. 创建一个DataSource实例，它的实际类型是`HikariDataSource`，创建时需要用到注入的配置；
4. 创建一个JdbcTemplate实例，它需要注入`DataSource`，这是通过方法参数完成注入的。

最后，针对HSQLDB写一个配置文件`jdbc.properties`：

```
# 数据库文件名为testdb:
jdbc.url=jdbc:hsqldb:file:testdb

# Hsqldb默认的用户名是sa，口令是空字符串:
jdbc.username=sa
jdbc.password=
```

可以通过HSQLDB自带的工具来初始化数据库表，这里我们写一个Bean，在Spring容器启动时自动创建一个`users`表：

```
@Component
public class DatabaseInitializer {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS users (" //
                + "id BIGINT IDENTITY NOT NULL PRIMARY KEY, " //
                + "email VARCHAR(100) NOT NULL, " //
                + "password VARCHAR(100) NOT NULL, " //
                + "name VARCHAR(100) NOT NULL, " //
                + "UNIQUE (email))");
    }
}
```

现在，所有准备工作都已完毕。我们只需要在需要访问数据库的Bean中，注入`JdbcTemplate`即可：

```
@Component
public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    ...
}
```

### JdbcTemplate用法

Spring提供的`JdbcTemplate`采用Template模式，提供了一系列以回调为特点的工具方法，目的是避免繁琐的`try...catch`语句。

我们以具体的示例来说明JdbcTemplate的用法。

首先我们看`T execute(ConnectionCallback<T> action)`方法，它提供了Jdbc的`Connection`供我们使用：

```
public User getUserById(long id) {
    // 注意传入的是ConnectionCallback:
    return jdbcTemplate.execute((Connection conn) -> {
        // 可以直接使用conn实例，不要释放它，回调结束后JdbcTemplate自动释放:
        // 在内部手动创建的PreparedStatement、ResultSet必须用try(...)释放:
        try (var ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            ps.setObject(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User( // new User object:
                            rs.getLong("id"), // id
                            rs.getString("email"), // email
                            rs.getString("password"), // password
                            rs.getString("name")); // name
                }
                throw new RuntimeException("user not found by id.");
            }
        }
    });
}
```

也就是说，上述回调方法允许获取Connection，然后做任何基于Connection的操作。

我们再看`T execute(String sql, PreparedStatementCallback<T> action)`的用法：

```
public User getUserByName(String name) {
    // 需要传入SQL语句，以及PreparedStatementCallback:
    return jdbcTemplate.execute("SELECT * FROM users WHERE name = ?", (PreparedStatement ps) -> {
        // PreparedStatement实例已经由JdbcTemplate创建，并在回调后自动释放:
        ps.setObject(1, name);
        try (var rs = ps.executeQuery()) {
            if (rs.next()) {
                return new User( // new User object:
                        rs.getLong("id"), // id
                        rs.getString("email"), // email
                        rs.getString("password"), // password
                        rs.getString("name")); // name
            }
            throw new RuntimeException("user not found by id.");
        }
    });
}
```

最后，我们看`T queryForObject(String sql, RowMapper<T> rowMapper, Object... args)`方法：

```
public User getUserByEmail(String email) {
    // 传入SQL，参数和RowMapper实例:
    return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?",
            (ResultSet rs, int rowNum) -> {
                // 将ResultSet的当前行映射为一个JavaBean:
                return new User( // new User object:
                        rs.getLong("id"), // id
                        rs.getString("email"), // email
                        rs.getString("password"), // password
                        rs.getString("name")); // name
            },
            email);
}
```

在`queryForObject()`方法中，传入SQL以及SQL参数后，`JdbcTemplate`会自动创建`PreparedStatement`，自动执行查询并返回`ResultSet`，我们提供的`RowMapper`需要做的事情就是把`ResultSet`的当前行映射成一个JavaBean并返回。整个过程中，使用`Connection`、`PreparedStatement`和`ResultSet`都不需要我们手动管理。

`RowMapper`不一定返回JavaBean，实际上它可以返回任何Java对象。例如，使用`SELECT COUNT(*)`查询时，可以返回`Long`：

```
public long getUsers() {
    return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", (ResultSet rs, int rowNum) -> {
        // SELECT COUNT(*)查询只有一列，取第一列数据:
        return rs.getLong(1);
    });
}
```

如果我们期望返回多行记录，而不是一行，可以用`query()`方法：

```
public List<User> getUsers(int pageIndex) {
    int limit = 100;
    int offset = limit * (pageIndex - 1);
    return jdbcTemplate.query("SELECT * FROM users LIMIT ? OFFSET ?",
            new BeanPropertyRowMapper<>(User.class),
            limit, offset);
}
```

上述`query()`方法传入的参数仍然是SQL、SQL参数以及`RowMapper`实例。这里我们直接使用Spring提供的`BeanPropertyRowMapper`。如果数据库表的结构恰好和JavaBean的属性名称一致，那么`BeanPropertyRowMapper`就可以直接把一行记录按列名转换为JavaBean。

如果我们执行的不是查询，而是插入、更新和删除操作，那么需要使用`update()`方法：

```
public void updateUser(User user) {
    // 传入SQL，SQL参数，返回更新的行数:
    if (1 != jdbcTemplate.update("UPDATE users SET name = ? WHERE id = ?", user.getName(), user.getId())) {
        throw new RuntimeException("User not found by id");
    }
}
```

只有一种`INSERT`操作比较特殊，那就是如果某一列是自增列（例如自增主键），通常，我们需要获取插入后的自增值。`JdbcTemplate`提供了一个`KeyHolder`来简化这一操作：

```
public User register(String email, String password, String name) {
    // 创建一个KeyHolder:
    KeyHolder holder = new GeneratedKeyHolder();
    if (1 != jdbcTemplate.update(
        // 参数1:PreparedStatementCreator
        (conn) -> {
            // 创建PreparedStatement时，必须指定RETURN_GENERATED_KEYS:
            var ps = conn.prepareStatement("INSERT INTO users(email, password, name) VALUES(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, email);
            ps.setObject(2, password);
            ps.setObject(3, name);
            return ps;
        },
        // 参数2:KeyHolder
        holder)
    ) {
        throw new RuntimeException("Insert failed.");
    }
    // 从KeyHolder中获取返回的自增值:
    return new User(holder.getKey().longValue(), email, password, name);
}
```

`JdbcTemplate`还有许多重载方法，这里我们不一一介绍。需要强调的是，`JdbcTemplate`只是对JDBC操作的一个简单封装，它的目的是尽量减少手动编写`try(resource) {...}`的代码，对于查询，主要通过`RowMapper`实现了JDBC结果集到Java对象的转换。

我们总结一下`JdbcTemplate`的用法，那就是：

* 针对简单查询，优选`query()`和`queryForObject()`，因为只需提供SQL语句、参数和`RowMapper`；
* 针对更新操作，优选`update()`，因为只需提供SQL语句和参数；
* 任何复杂的操作，最终也可以通过`execute(ConnectionCallback)`实现，因为拿到`Connection`就可以做任何JDBC操作。

实际上我们使用最多的仍然是各种查询。如果在设计表结构的时候，能够和JavaBean的属性一一对应，那么直接使用`BeanPropertyRowMapper`就很方便。如果表结构和JavaBean不一致怎么办？那就需要稍微改写一下查询，使结果集的结构和JavaBean保持一致。

例如，表的列名是`office_address`，而JavaBean属性是`workAddress`，就需要指定别名，改写查询如下：

```
SELECT id, email, office_address AS workAddress, name FROM users WHERE email = ?
```

### 练习

从[![]()](https://gitee.com/)下载练习：[使用JdbcTemplate](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/210.Spring%E5%BC%80%E5%8F%91.1266263217140032/30.%E8%AE%BF%E9%97%AE%E6%95%B0%E6%8D%AE%E5%BA%93.1282383540125729/10.%E4%BD%BF%E7%94%A8JDBC.1282383699509281/spring-data-jdbc.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

Spring提供了`JdbcTemplate`来简化JDBC操作；

使用`JdbcTemplate`时，根据需要优先选择高级方法；

任何JDBC操作都可以使用保底的`execute(ConnectionCallback)`方法。


## 使用声明式任务


使用Spring操作JDBC虽然方便，但是我们在前面讨论JDBC的时候，讲到过[JDBC事务](https://www.liaoxuefeng.com/wiki/1252599548343744/1321748500840481)，如果要在Spring中操作事务，没必要手写JDBC事务，可以使用Spring提供的高级接口来操作事务。

Spring提供了一个`PlatformTransactionManager`来表示事务管理器，所有的事务都由它负责管理。而事务由`TransactionStatus`表示。如果手写事务代码，使用`try...catch`如下：

```
TransactionStatus tx = null;
try {
    // 开启事务:
    tx = txManager.getTransaction(new DefaultTransactionDefinition());
    // 相关JDBC操作:
    jdbcTemplate.update("...");
    jdbcTemplate.update("...");
    // 提交事务:
    txManager.commit(tx);
} catch (RuntimeException e) {
    // 回滚事务:
    txManager.rollback(tx);
    throw e;
}
```

Spring为啥要抽象出`PlatformTransactionManager`和`TransactionStatus`？原因是JavaEE除了提供JDBC事务外，它还支持分布式事务JTA（Java Transaction API）。分布式事务是指多个数据源（比如多个数据库，多个消息系统）要在分布式环境下实现事务的时候，应该怎么实现。分布式事务实现起来非常复杂，简单地说就是通过一个分布式事务管理器实现两阶段提交，但本身数据库事务就不快，基于数据库事务实现的分布式事务就慢得难以忍受，所以使用率不高。

Spring为了同时支持JDBC和JTA两种事务模型，就抽象出`PlatformTransactionManager`。因为我们的代码只需要JDBC事务，因此，在`AppConfig`中，需要再定义一个`PlatformTransactionManager`对应的Bean，它的实际类型是`DataSourceTransactionManager`：

```
@Configuration
@ComponentScan
@PropertySource("jdbc.properties")
public class AppConfig {
    ...
    @Bean
    PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

使用编程的方式使用Spring事务仍然比较繁琐，更好的方式是通过声明式事务来实现。使用声明式事务非常简单，除了在`AppConfig`中追加一个上述定义的`PlatformTransactionManager`外，再加一个`@EnableTransactionManagement`就可以启用声明式事务：

```
@Configuration
@ComponentScan
@EnableTransactionManagement // 启用声明式
@PropertySource("jdbc.properties")
public class AppConfig {
    ...
}
```

然后，对需要事务支持的方法，加一个`@Transactional`注解：

```
@Component
public class UserService {
    // 此public方法自动具有事务支持:
    @Transactional
    public User register(String email, String password, String name) {
       ...
    }
}
```

或者更简单一点，直接在Bean的`class`处加上，表示所有`public`方法都具有事务支持：

```
@Component
@Transactional
public class UserService {
    ...
}
```

Spring对一个声明式事务的方法，如何开启事务支持？原理仍然是AOP代理，即通过自动创建Bean的Proxy实现：

```
public class UserService$$EnhancerBySpringCGLIB extends UserService {
    UserService target = ...
    PlatformTransactionManager txManager = ...

    public User register(String email, String password, String name) {
        TransactionStatus tx = null;
        try {
            tx = txManager.getTransaction(new DefaultTransactionDefinition());
            target.register(email, password, name);
            txManager.commit(tx);
        } catch (RuntimeException e) {
            txManager.rollback(tx);
            throw e;
        }
    }
    ...
}
```

注意：声明了`@EnableTransactionManagement`后，不必额外添加`@EnableAspectJAutoProxy`。

### 回滚事务

默认情况下，如果发生了`RuntimeException`，Spring的声明式事务将自动回滚。在一个事务方法中，如果程序判断需要回滚事务，只需抛出`RuntimeException`，例如：

```
@Transactional
public buyProducts(long productId, int num) {
    ...
    if (store < num) {
        // 库存不够，购买失败:
        throw new IllegalArgumentException("No enough products");
    }
    ...
}
```

如果要针对Checked Exception回滚事务，需要在`@Transactional`注解中写出来：

```
@Transactional(rollbackFor = {RuntimeException.class, IOException.class})
public buyProducts(long productId, int num) throws IOException {
    ...
}
```

上述代码表示在抛出`RuntimeException`或`IOException`时，事务将回滚。

为了简化代码，我们强烈建议业务异常体系从`RuntimeException`派生，这样就不必声明任何特殊异常即可让Spring的声明式事务正常工作：

```
public class BusinessException extends RuntimeException {
    ...
}

public class LoginException extends BusinessException {
    ...
}

public class PaymentException extends BusinessException {
    ...
}
```

### 事务边界

在使用事务的时候，明确事务边界非常重要。对于声明式事务，例如，下面的`register()`方法：

```
@Component
public class UserService {
    @Transactional
    public User register(String email, String password, String name) { // 事务开始
       ...
    } // 事务结束
}
```

它的事务边界就是`register()`方法开始和结束。

类似的，一个负责给用户增加积分的`addBonus()`方法：

```
@Component
public class BonusService {
    @Transactional
    public void addBonus(long userId, int bonus) { // 事务开始
       ...
    } // 事务结束
}
```

它的事务边界就是`addBonus()`方法开始和结束。

在现实世界中，问题总是要复杂一点点。用户注册后，能自动获得100积分，因此，实际代码如下：

```
@Component
public class UserService {
    @Autowired
    BonusService bonusService;

    @Transactional
    public User register(String email, String password, String name) {
        // 插入用户记录:
        User user = jdbcTemplate.insert("...");
        // 增加100积分:
        bonusService.addBonus(user.id, 100);
    }
}
```

现在问题来了：调用方（比如`RegisterController`）调用`UserService.register()`这个事务方法，它在内部又调用了`BonusService.addBonus()`这个事务方法，一共有几个事务？如果`addBonus()`抛出了异常需要回滚事务，`register()`方法的事务是否也要回滚？

问题的复杂度是不是一下子提高了10倍？

### 事务传播

要解决上面的问题，我们首先要定义事务的传播模型。

假设用户注册的入口是`RegisterController`，它本身没有事务，仅仅是调用`UserService.register()`这个事务方法：

```
@Controller
public class RegisterController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ModelAndView doRegister(HttpServletRequest req) {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        User user = userService.register(email, password, name);
        return ...
    }
}
```

因此，`UserService.register()`这个事务方法的起始和结束，就是事务的范围。

我们需要关心的问题是，在`UserService.register()`这个事务方法内，调用`BonusService.addBonus()`，我们期待的事务行为是什么：

```
@Transactional
public User register(String email, String password, String name) {
    // 事务已开启:
    User user = jdbcTemplate.insert("...");
    // ???:
    bonusService.addBonus(user.id, 100);
} // 事务结束
```

对于大多数业务来说，我们期待`BonusService.addBonus()`的调用，和`UserService.register()`应当融合在一起，它的行为应该如下：

`UserService.register()`已经开启了一个事务，那么在内部调用`BonusService.addBonus()`时，`BonusService.addBonus()`方法就没必要再开启一个新事务，直接加入到`BonusService.register()`的事务里就好了。

其实就相当于：

1. `UserService.register()`先执行了一条INSERT语句：`INSERT INTO users ...`
2. `BonusService.addBonus()`再执行一条INSERT语句：`INSERT INTO bonus ...`

因此，Spring的声明式事务为事务传播定义了几个级别，默认传播级别就是REQUIRED，它的意思是，如果当前没有事务，就创建一个新事务，如果当前有事务，就加入到当前事务中执行。

我们观察`UserService.register()`方法，它在`RegisterController`中执行，因为`RegisterController`没有事务，因此，`UserService.register()`方法会自动创建一个新事务。

在`UserService.register()`方法内部，调用`BonusService.addBonus()`方法时，因为`BonusService.addBonus()`检测到当前已经有事务了，因此，它会加入到当前事务中执行。

因此，整个业务流程的事务边界就清晰了：它只有一个事务，并且范围就是`UserService.register()`方法。

有的童鞋会问：把`BonusService.addBonus()`方法的`@Transactional`去掉，变成一个普通方法，那不就规避了复杂的传播模型吗？

去掉`BonusService.addBonus()`方法的`@Transactional`，会引来另一个问题，即其他地方如果调用`BonusService.addBonus()`方法，那就没法保证事务了。例如，规定用户登录时积分+5：

```
@Controller
public class LoginController {
    @Autowired
    BonusService bonusService;

    @PostMapping("/login")
    public ModelAndView doLogin(HttpServletRequest req) {
        User user = ...
        bonusService.addBonus(user.id, 5);
    }
}
```

可见，`BonusService.addBonus()`方法必须要有`@Transactional`，否则，登录后积分就无法添加了。

默认的事务传播级别是`REQUIRED`，它满足绝大部分的需求。还有一些其他的传播级别：

`SUPPORTS`：表示如果有事务，就加入到当前事务，如果没有，那也不开启事务执行。这种传播级别可用于查询方法，因为SELECT语句既可以在事务内执行，也可以不需要事务；

`MANDATORY`：表示必须要存在当前事务并加入执行，否则将抛出异常。这种传播级别可用于核心更新逻辑，比如用户余额变更，它总是被其他事务方法调用，不能直接由非事务方法调用；

`REQUIRES_NEW`：表示不管当前有没有事务，都必须开启一个新的事务执行。如果当前已经有事务，那么当前事务会挂起，等新事务完成后，再恢复执行；

`NOT_SUPPORTED`：表示不支持事务，如果当前有事务，那么当前事务会挂起，等这个方法执行完成后，再恢复执行；

`NEVER`：和`NOT_SUPPORTED`相比，它不但不支持事务，而且在监测到当前有事务时，会抛出异常拒绝执行；

`NESTED`：表示如果当前有事务，则开启一个嵌套级别事务，如果当前没有事务，则开启一个新事务。

上面这么多种事务的传播级别，其实默认的`REQUIRED`已经满足绝大部分需求，`SUPPORTS`和`REQUIRES_NEW`在少数情况下会用到，其他基本不会用到，因为把事务搞得越复杂，不仅逻辑跟着复杂，而且速度也会越慢。

定义事务的传播级别也是写在`@Transactional`注解里的：

```
@Transactional(propagation = Propagation.REQUIRES_NEW)
public Product createProduct() {
    ...
}
```

现在只剩最后一个问题了：Spring是如何传播事务的？

我们[在JDBC中使用事务](https://www.liaoxuefeng.com/wiki/1252599548343744/1321748500840481)的时候，是这么个写法：

```
Connection conn = openConnection();
try {
    // 关闭自动提交:
    conn.setAutoCommit(false);
    // 执行多条SQL语句:
    insert(); update(); delete();
    // 提交事务:
    conn.commit();
} catch (SQLException e) {
    // 回滚事务:
    conn.rollback();
} finally {
    conn.setAutoCommit(true);
    conn.close();
}
```

Spring使用声明式事务，最终也是通过执行JDBC事务来实现功能的，那么，一个事务方法，如何获知当前是否存在事务？

答案是[使用ThreadLocal](https://www.liaoxuefeng.com/wiki/1252599548343744/1306581251653666)。Spring总是把JDBC相关的`Connection`和`TransactionStatus`实例绑定到`ThreadLocal`。如果一个事务方法从`ThreadLocal`未取到事务，那么它会打开一个新的JDBC连接，同时开启一个新的事务，否则，它就直接使用从`ThreadLocal`获取的JDBC连接以及`TransactionStatus`。

因此，事务能正确传播的前提是，方法调用是在一个线程内才行。如果像下面这样写：

```
@Transactional
public User register(String email, String password, String name) { // BEGIN TX-A
    User user = jdbcTemplate.insert("...");
    new Thread(() -> {
        // BEGIN TX-B:
        bonusService.addBonus(user.id, 100);
        // END TX-B
    }).start();
} // END TX-A
```

在另一个线程中调用`BonusService.addBonus()`，它根本获取不到当前事务，因此，`UserService.register()`和`BonusService.addBonus()`两个方法，将分别开启两个完全独立的事务。

换句话说，事务只能在当前线程传播，无法跨线程传播。

那如果我们想实现跨线程传播事务呢？原理很简单，就是要想办法把当前线程绑定到`ThreadLocal`的`Connection`和`TransactionStatus`实例传递给新线程，但实现起来非常复杂，根据异常回滚更加复杂，不推荐自己去实现。

### 练习

从[![]()](https://gitee.com/)下载练习：[使用声明式事务](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/210.Spring%E5%BC%80%E5%8F%91.1266263217140032/30.%E8%AE%BF%E9%97%AE%E6%95%B0%E6%8D%AE%E5%BA%93.1282383540125729/20.%E4%BD%BF%E7%94%A8%E5%A3%B0%E6%98%8E%E5%BC%8F%E4%BA%8B%E5%8A%A1.1282383642886177/spring-data-tx.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

Spring提供的声明式事务极大地方便了在数据库中使用事务，正确使用声明式事务的关键在于确定好事务边界，理解事务传播级别。


## 使用DAO


在传统的多层应用程序中，通常是Web层调用业务层，业务层调用数据访问层。业务层负责处理各种业务逻辑，而数据访问层只负责对数据进行增删改查。因此，实现数据访问层就是用`JdbcTemplate`实现对数据库的操作。

编写数据访问层的时候，可以使用DAO模式。DAO即Data Access Object的缩写，它没有什么神秘之处，实现起来基本如下：

```
public class UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    User getById(long id) {
        ...
    }

    List<User> getUsers(int page) {
        ...
    }

    User createUser(User user) {
        ...
    }

    User updateUser(User user) {
        ...
    }

    void deleteUser(User user) {
        ...
    }
}
```

Spring提供了一个`JdbcDaoSupport`类，用于简化DAO的实现。这个`JdbcDaoSupport`没什么复杂的，核心代码就是持有一个`JdbcTemplate`：

```
public abstract class JdbcDaoSupport extends DaoSupport {

    private JdbcTemplate jdbcTemplate;

    public final void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        initTemplateConfig();
    }

    public final JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    ...
}
```

它的意图是子类直接从`JdbcDaoSupport`继承后，可以随时调用`getJdbcTemplate()`获得`JdbcTemplate`的实例。那么问题来了：因为`JdbcDaoSupport`的`jdbcTemplate`字段没有标记`@Autowired`，所以，子类想要注入`JdbcTemplate`，还得自己想个办法：

```
@Component
@Transactional
public class UserDao extends JdbcDaoSupport {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        super.setJdbcTemplate(jdbcTemplate);
    }
}
```

有的童鞋可能看出来了：既然`UserDao`都已经注入了`JdbcTemplate`，那再把它放到父类里，通过`getJdbcTemplate()`访问岂不是多此一举？

如果使用传统的XML配置，并不需要编写`@Autowired JdbcTemplate jdbcTemplate`，但是考虑到现在基本上是使用注解的方式，我们可以编写一个`AbstractDao`，专门负责注入`JdbcTemplate`：

```
public abstract class AbstractDao extends JdbcDaoSupport {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        super.setJdbcTemplate(jdbcTemplate);
    }
}
```

这样，子类的代码就非常干净，可以直接调用`getJdbcTemplate()`：

```
@Component
@Transactional
public class UserDao extends AbstractDao {
    public User getById(long id) {
        return getJdbcTemplate().queryForObject(
                "SELECT * FROM users WHERE id = ?",
                new BeanPropertyRowMapper<>(User.class),
                id
        );
    }
    ...
}
```

倘若肯再多写一点样板代码，就可以把`AbstractDao`改成泛型，并实现`getById()`，`getAll()`，`deleteById()`这样的通用方法：

```
public abstract class AbstractDao<T> extends JdbcDaoSupport {
    private String table;
    private Class<T> entityClass;
    private RowMapper<T> rowMapper;

    public AbstractDao() {
        // 获取当前类型的泛型类型:
        this.entityClass = getParameterizedType();
        this.table = this.entityClass.getSimpleName().toLowerCase() + "s";
        this.rowMapper = new BeanPropertyRowMapper<>(entityClass);
    }

    public T getById(long id) {
        return getJdbcTemplate().queryForObject("SELECT * FROM " + table + " WHERE id = ?", this.rowMapper, id);
    }

    public List<T> getAll(int pageIndex) {
        int limit = 100;
        int offset = limit * (pageIndex - 1);
        return getJdbcTemplate().query("SELECT * FROM " + table + " LIMIT ? OFFSET ?",
                new Object[] { limit, offset },
                this.rowMapper);
    }

    public void deleteById(long id) {
        getJdbcTemplate().update("DELETE FROM " + table + " WHERE id = ?", id);
    }
    ...
}
```

这样，每个子类就自动获得了这些通用方法：

```
@Component
@Transactional
public class UserDao extends AbstractDao<User> {
    // 已经有了:
    // User getById(long)
    // List<User> getAll(int)
    // void deleteById(long)
}

@Component
@Transactional
public class BookDao extends AbstractDao<Book> {
    // 已经有了:
    // Book getById(long)
    // List<Book> getAll(int)
    // void deleteById(long)
}
```

可见，DAO模式就是一个简单的数据访问模式，是否使用DAO，根据实际情况决定，因为很多时候，直接在Service层操作数据库也是完全没有问题的。

### 练习

从[![]()](https://gitee.com/)下载练习：[使用DAO模式](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/210.Spring%E5%BC%80%E5%8F%91.1266263217140032/30.%E8%AE%BF%E9%97%AE%E6%95%B0%E6%8D%AE%E5%BA%93.1282383540125729/30.%E4%BD%BF%E7%94%A8DAO.1282383605137441/spring-data-dao.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

Spring提供了`JdbcDaoSupport`来便于我们实现DAO模式；

可以基于泛型实现更通用、更简洁的DAO模式。


## 集成 Hibernate


使用`JdbcTemplate`的时候，我们用得最多的方法就是`List<T> query(String, RowMapper, Object...)`。这个`RowMapper`的作用就是把`ResultSet`的一行记录映射为Java Bean。

这种把关系数据库的表记录映射为Java对象的过程就是ORM：Object-Relational Mapping。ORM既可以把记录转换成Java对象，也可以把Java对象转换为行记录。

使用`JdbcTemplate`配合`RowMapper`可以看作是最原始的ORM。如果要实现更自动化的ORM，可以选择成熟的ORM框架，例如[Hibernate](https://hibernate.org/)。

我们来看看如何在Spring中集成Hibernate。

Hibernate作为ORM框架，它可以替代`JdbcTemplate`，但Hibernate仍然需要JDBC驱动，所以，我们需要引入JDBC驱动、连接池，以及Hibernate本身。在Maven中，我们加入以下依赖项：

* org.springframework:spring-context:6.0.0
* org.springframework:spring-orm:6.0.0
* jakarta.annotation:jakarta.annotation-api:2.1.1
* jakarta.persistence:jakarta.persistence-api:3.1.0
* org.hibernate:hibernate-core:6.1.4.Final
* com.zaxxer:HikariCP:5.0.1
* org.hsqldb:hsqldb:2.7.1

在`AppConfig`中，我们仍然需要创建`DataSource`、引入JDBC配置文件，以及启用声明式事务：

```
@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {
    @Bean
    DataSource createDataSource() {
        ...
    }
}
```

为了启用Hibernate，我们需要创建一个`LocalSessionFactoryBean`：

```
public class AppConfig {
    @Bean
    LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource) {
        var props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", "update"); // 生产环境不要使用
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        props.setProperty("hibernate.show_sql", "true");
        var sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        // 扫描指定的package获取所有entity class:
        sessionFactoryBean.setPackagesToScan("com.itranswarp.learnjava.entity");
        sessionFactoryBean.setHibernateProperties(props);
        return sessionFactoryBean;
    }
}
```

注意我们在[定制Bean](https://www.liaoxuefeng.com/wiki/1252599548343744/1308043627200545)中讲到过`FactoryBean`，`LocalSessionFactoryBean`是一个`FactoryBean`，它会再自动创建一个`SessionFactory`，在Hibernate中，`Session`是封装了一个JDBC `Connection`的实例，而`SessionFactory`是封装了JDBC `DataSource`的实例，即`SessionFactory`持有连接池，每次需要操作数据库的时候，`SessionFactory`创建一个新的`Session`，相当于从连接池获取到一个新的`Connection`。`SessionFactory`就是Hibernate提供的最核心的一个对象，但`LocalSessionFactoryBean`是Spring提供的为了让我们方便创建`SessionFactory`的类。

注意到上面创建`LocalSessionFactoryBean`的代码，首先用`Properties`持有Hibernate初始化`SessionFactory`时用到的所有设置，常用的设置请参考[Hibernate文档](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#configurations)，这里我们只定义了3个设置：

* `hibernate.hbm2ddl.auto=update`：表示自动创建数据库的表结构，注意不要在生产环境中启用；
* `hibernate.dialect=org.hibernate.dialect.HSQLDialect`：指示Hibernate使用的数据库是HSQLDB。Hibernate使用一种HQL的查询语句，它和SQL类似，但真正在“翻译”成SQL时，会根据设定的数据库“方言”来生成针对数据库优化的SQL；
* `hibernate.show_sql=true`：让Hibernate打印执行的SQL，这对于调试非常有用，我们可以方便地看到Hibernate生成的SQL语句是否符合我们的预期。

除了设置`DataSource`和`Properties`之外，注意到`setPackagesToScan()`我们传入了一个`package`名称，它指示Hibernate扫描这个包下面的所有Java类，自动找出能映射为数据库表记录的JavaBean。后面我们会仔细讨论如何编写符合Hibernate要求的JavaBean。

紧接着，我们还需要创建`HibernateTransactionManager`：

```
public class AppConfig {
    @Bean
    PlatformTransactionManager createTxManager(@Autowired SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}
```

`HibernateTransactionManager`是配合Hibernate使用声明式事务所必须的。到此为止，所有的配置都定义完毕，我们来看看如何将数据库表结构映射为Java对象。

考察如下的数据库表：

```
CREATE TABLE user
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    createdAt BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`)
);
```

其中，`id`是自增主键，`email`、`password`、`name`是`VARCHAR`类型，`email`带唯一索引以确保唯一性，`createdAt`存储整型类型的时间戳。用JavaBean表示如下：

```
public class User {
    private Long id;
    private String email;
    private String password;
    private String name;
    private Long createdAt;

    // getters and setters
    ...
}
```

这种映射关系十分易懂，但我们需要添加一些注解来告诉Hibernate如何把`User`类映射到表记录：

```
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    public Long getId() { ... }

    @Column(nullable = false, unique = true, length = 100)
    public String getEmail() { ... }

    @Column(nullable = false, length = 100)
    public String getPassword() { ... }

    @Column(nullable = false, length = 100)
    public String getName() { ... }

    @Column(nullable = false, updatable = false)
    public Long getCreatedAt() { ... }
}
```

如果一个JavaBean被用于映射，我们就标记一个`@Entity`。默认情况下，映射的表名是`user`，如果实际的表名不同，例如实际表名是`users`，可以追加一个`@Table(name="users")`表示：

```
@Entity
@Table(name="users)
public class User {
    ...
}
```

每个属性到数据库列的映射用`@Column()`标识，`nullable`指示列是否允许为`NULL`，`updatable`指示该列是否允许被用在`UPDATE`语句，`length`指示`String`类型的列的长度（如果没有指定，默认是`255`）。

对于主键，还需要用`@Id`标识，自增主键再追加一个`@GeneratedValue`，以便Hibernate能读取到自增主键的值。

细心的童鞋可能还注意到，主键`id`定义的类型不是`long`，而是`Long`。这是因为Hibernate如果检测到主键为`null`，就不会在`INSERT`语句中指定主键的值，而是返回由数据库生成的自增值，否则，Hibernate认为我们的程序指定了主键的值，会在`INSERT`语句中直接列出。`long`型字段总是具有默认值`0`，因此，每次插入的主键值总是0，导致除第一次外后续插入都将失败。

`createdAt`虽然是整型，但我们并没有使用`long`，而是`Long`，这是因为使用基本类型会导致findByExample查询会添加意外的条件，这里只需牢记，作为映射使用的JavaBean，所有属性都使用包装类型而不是基本类型。

使用Hibernate时，不要使用基本类型的属性，总是使用包装类型，如Long或Integer。

类似的，我们再定义一个`Book`类：

```
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    public Long getId() { ... }

    @Column(nullable = false, length = 100)
    public String getTitle() { ... }

    @Column(nullable = false, updatable = false)
    public Long getCreatedAt() { ... }
}
```

如果仔细观察`User`和`Book`，会发现它们定义的`id`、`createdAt`属性是一样的，这在数据库表结构的设计中很常见：对于每个表，通常我们会统一使用一种主键生成机制，并添加`createdAt`表示创建时间，`updatedAt`表示修改时间等通用字段。

不必在`User`和`Book`中重复定义这些通用字段，我们可以把它们提到一个抽象类中：

```
@MappedSuperclass
public abstract class AbstractEntity {

    private Long id;
    private Long createdAt;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    public Long getId() { ... }

    @Column(nullable = false, updatable = false)
    public Long getCreatedAt() { ... }

    @Transient
    public ZonedDateTime getCreatedDateTime() {
        return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault());
    }

    @PrePersist
    public void preInsert() {
        setCreatedAt(System.currentTimeMillis());
    }
}
```

对于`AbstractEntity`来说，我们要标注一个`@MappedSuperclass`表示它用于继承。此外，注意到我们定义了一个`@Transient`方法，它返回一个“虚拟”的属性。因为`getCreatedDateTime()`是计算得出的属性，而不是从数据库表读出的值，因此必须要标注`@Transient`，否则Hibernate会尝试从数据库读取名为`createdDateTime`这个不存在的字段从而出错。

再注意到`@PrePersist`标识的方法，它表示在我们将一个JavaBean持久化到数据库之前（即执行INSERT语句），Hibernate会先执行该方法，这样我们就可以自动设置好`createdAt`属性。

有了`AbstractEntity`，我们就可以大幅简化`User`和`Book`：

```
@Entity
public class User extends AbstractEntity {

    @Column(nullable = false, unique = true, length = 100)
    public String getEmail() { ... }

    @Column(nullable = false, length = 100)
    public String getPassword() { ... }

    @Column(nullable = false, length = 100)
    public String getName() { ... }
}
```

注意到使用的所有注解均来自`jakarta.persistence`，它是JPA规范的一部分。这里我们只介绍使用注解的方式配置Hibernate映射关系，不再介绍传统的比较繁琐的XML配置。通过Spring集成Hibernate时，也不再需要`hibernate.cfg.xml`配置文件，用一句话总结：

使用Spring集成Hibernate，配合JPA注解，无需任何额外的XML配置。

类似`User`、`Book`这样的用于ORM的Java Bean，我们通常称之为Entity Bean。

最后，我们来看看如果对`user`表进行增删改查。因为使用了Hibernate，因此，我们要做的，实际上是对`User`这个JavaBean进行“增删改查”。我们编写一个`UserService`，注入`SessionFactory`：

```
@Component
@Transactional
public class UserService {
    @Autowired
    SessionFactory sessionFactory;
}
```

### Insert操作

要持久化一个`User`实例，我们只需调用`persist()`方法。以`register()`方法为例，代码如下：

```
public User register(String email, String password, String name) {
    // 创建一个User对象:
    User user = new User();
    // 设置好各个属性:
    user.setEmail(email);
    user.setPassword(password);
    user.setName(name);
    // 不要设置id，因为使用了自增主键
    // 保存到数据库:
    sessionFactory.getCurrentSession().persist(user);
    // 现在已经自动获得了id:
    System.out.println(user.getId());
    return user;
}
```

### Delete操作

删除一个`User`相当于从表中删除对应的记录。注意Hibernate总是用`id`来删除记录，因此，要正确设置`User`的`id`属性才能正常删除记录：

```
public boolean deleteUser(Long id) {
    User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
    if (user != null) {
        sessionFactory.getCurrentSession().remove(user);
        return true;
    }
    return false;
}
```

通过主键删除记录时，一个常见的用法是先根据主键加载该记录，再删除。注意到当记录不存在时，`load()`返回`null`。

### Update操作

更新记录相当于先更新`User`的指定属性，然后调用`merge()`方法：

```
public void updateUser(Long id, String name) {
    User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
    user.setName(name);
    sessionFactory.getCurrentSession().merge(user);
}
```

前面我们在定义`User`时，对有的属性标注了`@Column(updatable=false)`。Hibernate在更新记录时，它只会把`@Column(updatable=true)`的属性加入到`UPDATE`语句中，这样可以提供一层额外的安全性，即如果不小心修改了`User`的`email`、`createdAt`等属性，执行`update()`时并不会更新对应的数据库列。但也必须牢记：这个功能是Hibernate提供的，如果绕过Hibernate直接通过JDBC执行`UPDATE`语句仍然可以更新数据库的任意列的值。

最后，我们编写的大部分方法都是各种各样的查询。根据`id`查询我们可以直接调用`load()`，如果要使用条件查询，例如，假设我们想执行以下查询：

```
SELECT * FROM user WHERE email = ? AND password = ?
```

我们来看看可以使用什么查询。

### 使用HQL查询

一种常用的查询是直接编写Hibernate内置的HQL查询：

```
List<User> list = sessionFactory.getCurrentSession()
        .createQuery("from User u where u.email = ?1 and u.password = ?2", User.class)
        .setParameter(1, email).setParameter(2, password)
        .list();
```

和SQL相比，HQL使用类名和属性名，由Hibernate自动转换为实际的表名和列名。详细的HQL语法可以参考[Hibernate文档](https://docs.jboss.org/hibernate/orm/6.1/userguide/html_single/Hibernate_User_Guide.html#query-language)。

除了可以直接传入HQL字符串外，Hibernate还可以使用一种`NamedQuery`，它给查询起个名字，然后保存在注解中。使用`NamedQuery`时，我们要先在`User`类标注：

```
@NamedQueries(
    @NamedQuery(
        // 查询名称:
        name = "login",
        // 查询语句:
        query = "SELECT u FROM User u WHERE u.email = :e AND u.password = :pwd"
    )
)
@Entity
public class User extends AbstractEntity {
    ...
}
```

注意到引入的`NamedQuery`是`jakarta.persistence.NamedQuery`，它和直接传入HQL有点不同的是，占位符使用`:e`和`:pwd`。

使用`NamedQuery`只需要引入查询名和参数：

```
public User login(String email, String password) {
    List<User> list = sessionFactory.getCurrentSession()
        .createNamedQuery("login", User.class) // 创建NamedQuery
        .setParameter("e", email) // 绑定e参数
        .setParameter("pwd", password) // 绑定pwd参数
        .list();
    return list.isEmpty() ? null : list.get(0);
}
```

直接写HQL和使用`NamedQuery`各有优劣。前者可以在代码中直观地看到查询语句，后者可以在`User`类统一管理所有相关查询。

### 练习

从[![]()](https://gitee.com/)下载练习：[集成Hibernate](https://gitee.com/liaoxuefeng/learn-java/blob/master/practices/Java%E6%95%99%E7%A8%8B/210.Spring%E5%BC%80%E5%8F%91.1266263217140032/30.%E8%AE%BF%E9%97%AE%E6%95%B0%E6%8D%AE%E5%BA%93.1282383540125729/40.%E9%9B%86%E6%88%90Hibernate.1266263275862720/spring-data-hibernate.zip?utm_source=blog_lxf) （推荐使用[IDE练习插件](https://www.liaoxuefeng.com/wiki/1252599548343744/1266092093733664)快速下载）

### 小结

在Spring中集成Hibernate需要配置的Bean如下：

* DataSource；
* LocalSessionFactory；
* HibernateTransactionManager。

推荐使用Annotation配置所有的Entity Bean。
