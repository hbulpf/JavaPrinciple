前言：clickhouse使用者越来越多，在具体的项目中结合现有的ORM框架也是必须的，本文主要记录了在微服务中结合mybaits和mybaits-plus的过程，当然，具体的配置还需结合项目的用途再详细配置。此外，在文章的结尾介绍了使用框架出现的一些问题和提出一些解决思路，也欢迎一起探讨交流。

# **引入Jar包**

```
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.3.2</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
123456789
```

若只使用mybatis不需要mybaits-plus依赖：

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
</dependency>
12345678
```

# **添加mybatis配置**

```
mybatis:
  #定义xml存放位置
  mapperLocations: classpath:mapper/*.xml
  #定义实体类存放位置
  typeAliasesPackage: iceyung.entity
  configuration:
    #设置map-underscore-to-camel-case属性为true来开启驼峰功能
    map-underscore-to-camel-case: true
    default-statement-timeout: 30
    default-fetch-size: 100
#mybatis-plus配置控制台打印完整带参数SQL语句
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
1234567891011121314
```

更多的参数可以参考mybatis和plus的文档，或者延续之前使用的配置即可，若有特殊的需求也可以自定义相应的配置。

# **mybatis-plus的BaseMapper和ServiceImpl**

TestEntity：

```
@Data
@TableName("test_zookeeper")
public class TestEntity {
    @TableId("Id")
    long id;
    @TableField("Code")
    int code;
    @TableField("Type")
    String type;
}
12345678910
```

TestMapper：

```
public interface TestMapper extends BaseMapper<TestEntity> {
}
12
```

TestService：

```
public interface TestService extends IService<TestEntity> {
}
12
```

TestServiceImpl：

```
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, TestEntity> implements TestService {
}
123
```

测试：

```
testService.list()
1
```

可以得出当前表内的数据。

# **mybatis使用xml构建clickhouse查询语句等操作**

上述的查询操作是依赖plus的特性完成的，我们也可以直接使用mybatis的xml的mapper完成clickhosue查询操作。

在application类中声明dao接口的扫描并注入：

```
@MapperScan("iceyung.dao")
1
```

Dao和Mapper的定义：

```
public interface TestDao {
    TestEntity getById(Integer id); 
}

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="iceyung.dao.TestDao">
   
    <select id="getById" resultType="iceyung.entity.TestEntity" parameterType="java.lang.Integer" >
        select  *
        from test_zookeeper
        where Id = #{id}
    </select>
</mapper>
1234567891011121314
```

# **重试配置**

开启重试：@EnableRetry，测试：

```
@Retryable(value = { ClickHouseException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 1))
public void call() throws Exception {
    testService.list();
    throw new ClickHouseException(1,"test",null,"127.0.0.1",8123);
}

@Recover
public void recover(ClickHouseException e) {
    log.info(e.getMessage());
}
12345678910
```

- @Retryable注解

被注解的方法发生异常时会重试

value：指定发生的异常进行重试

include：和value一样，默认空，当exclude也为空时，所有异常都重试

exclude：指定异常不重试，默认空，当include也为空时，所有异常都重试

maxAttemps：重试次数，默认3

backoff：重试补偿机制，默认没有

- @Backoff注解

delay:指定延迟后重试

multiplier:指定延迟的倍数，默认为1。比如delay=5000l,multiplier=2时，第一次重试为5秒后，第二次为5*2=10秒，第三次为5*2*2=20秒

- @Recover

当重试到达指定次数时，被注解的方法将被回调，可以在该方法中进行日志处理。需要注意的是发生的异常和入参类型一致时才会回调。

# **一些问题**

- 查询或写入失败的问题

笔者在初期使用mybatis时，出现过较多的response to fail失败错误，具体的分析可以看之前关于官方clickhouse jdbc的分析文章。在将长连接改为短连接后，目前运行较稳定，作为分析业务来看，稳定性暂时能接受，针对一些其它对稳定性要求较高的业务，推荐使用重试机制，防止因ch报错而导致当前接口出现异常，影响线上服务体验。

- 大数据量写入的性能问题

clickhouse推荐使用大批量少批次的写入模式，在使用框架进行数据写入时，笔者遇到过数据写入耗时过长等现象（写入数据量大约一次100万条，insert语句拼接valuse方式写入），改为纯jdbc写入则现象消失，所以若你也有类似的问题，建议框架只做查询使用，对于大批量的数据导入使用相应的工具或直接jdbc连接操作，避免框架在大数据量写入过程中出现耗时过久的问题。

# **参考文章**

1. spring的重试：https://blog.csdn.net/u014800380/article/details/84238472
2. mybaits结合springboot：https://blog.csdn.net/steven_zhulin/article/details/90726347