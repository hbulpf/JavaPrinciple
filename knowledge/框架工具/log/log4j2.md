# Log4J2
## Log4J2问题汇总

1. [开发中遇到的问题---【ERROR in ch.qos.logback.core.joran.spi.Interpreter@49:40 - no applicable act】](https://www.cnblogs.com/hujunwei/p/12581687.html)

启动报错：“ERROR in ch.qos.logback.core.joran.spi.Interpreter@49:40 - no applicable act”；

项目要求用log4j2，日志引入和自带的logback冲突导致，导致启动的时候，无法确定按谁的规则去读配置文件了。

方案：在pom.xml文件中引入下面的依赖；

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <exclusions>
        <exclusion>
            <artifactId>logback-core</artifactId>
            <groupId>ch.qos.logback</groupId>
        </exclusion>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

