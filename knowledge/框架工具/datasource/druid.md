# 数据库连接池Druid

## Druid是什么？

Druid首先是一个数据库连接池。

Druid是目前最好的数据库连接池，在功能、性能、扩展性方面，都超过其他数据库连接池，包括DBCP、C3P0、BoneCP、Proxool、JBoss DataSource。

Druid已经在阿里巴巴部署了超过600个应用，经过一年多生产环境大规模部署的严苛考验。

同时Druid不仅仅是一个数据库连接池，它包括四个部分，Druid是一个JDBC组件，它包括三个部分：
- 基于Filter－Chain模式的插件体系。
- DruidDataSource 高效可管理的数据库连接池。
- SQLParser

##### Druid可以做什么？

- 替换DBCP和C3P0。Druid提供了一个高效、功能强大、可扩展性好的数据库连接池。
- 监控数据库访问性能，Druid内置提供了一个功能强大的StatFilter插件，能够详细统计SQL的执行性能，这对于线上分析数据库访问性能有帮助。
- 数据库密码加密。直接把数据库密码写在配置文件中，这是不好的行为，容易导致安全问题。DruidDruiver和DruidDataSource都支持PasswordCallback。
- SQL执行日志，Druid提供了不同的LogFilter，能够支持Common-Logging、Log4j和JdkLog，可以按需要选择相应的LogFilter，监控你应用的数据库访问情况。
- 扩展JDBC，如果对JDBC层有编程的需求，可以通过Druid提供的Filter机制，很方便编写JDBC层的扩展插件。

在项目中使用Druid非常简单，只要修改配置文件就可以了.


## 如何使用 Druid


maven 坐标仓库

```xml
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.16</version>
    </dependency>
```



# 参考

1. [阿里数据库连接池Druid](https://blog.csdn.net/sanyaoxu_2/article/details/83450711)
1. [数据库阿里连接池 druid配置详解](https://blog.csdn.net/hj7jay/article/details/51686418)