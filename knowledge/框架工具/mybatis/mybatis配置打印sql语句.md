# mybatis配置打印sql语句

spring-mybatis.xml：
```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="configLocation" value="classpath:conf/mybatis-config.xml"></property>
        <!-- 自动扫描mapping.xml文件 -->
        <property name="mapperLocations" value="classpath:com/cyber/vip/dao/*.xml"></property>
    </bean>
```

mybatis-config.xml:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration   
    PUBLIC "-//mybatis.org//DTD Config 3.0//EN"  
    "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <!-- 打印查询语句 -->
        <setting name="logImpl" value="STDOUT_LOGGING" />
    </settings>
    
    <!-- mapper已经在spring-mybatis.xml中的sqlSessionFactory配置，这里不再需要配置 -->
<!--     <mappers> -->
<!--         <mapper resource="com/a/b/c/dao/BusinessInfoDaoMapper.xml" /> -->
<!--     </mappers> -->
</configuration>
```

不错，打印SQL只需要加一个setting就可以了。

mybatis的日志打印方式比较多，SLF4J | LOG4J | LOG4J2 | JDK_LOGGING | COMMONS_LOGGING | STDOUT_LOGGING | NO_LOGGING，可以根据自己的需要进行配置

 settings的更多参数可以参考官网文档：http://www.mybatis.org/mybatis-3/zh/configuration.html#settings