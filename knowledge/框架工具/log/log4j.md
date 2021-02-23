# Log4j

## 使用log4j

maven依赖

```
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.30</version>
</dependency>
```

log4j.properties

```
#dev env [debug] product env [info]
log4j.rootLogger=DEBUG, stdout
# Console output...
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```


## mybatis log4j打印sql语句


打印指定包下(方法)sql语句，便于调试

```
#dev env [debug] product env [info]
log4j.rootLogger=ERROR, stdout
# Console output...
# 细化到打印某个mapper
# log4j.logger.包名.方法名=TRACE
log4j.logger.net.cybclass.online.dao.VideoMapper.selectById=TRACE
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```

# 参考
1. [Log4j.properties配置详解](https://www.cnblogs.com/zhangguangxiang/p/12007924.html)
2. [mybatis log4j打印sql语句](https://www.cnblogs.com/chenyanbin/p/13027973.html)
3. https://www.cnblogs.com/LemonFive/p/10737658.html
4. https://blog.csdn.net/baixf/article/details/89925352