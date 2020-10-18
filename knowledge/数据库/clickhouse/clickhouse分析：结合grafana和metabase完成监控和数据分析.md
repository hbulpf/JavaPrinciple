前言：关于clickhouse的监控和可视化界面，想必刚接触到ch的人是一头雾水，大厂往往会给ch集群定制各种监控和可视化分析，普通用户就需要我们自己寻求现成的开源工具，在一些常见的开源工具上，我们可以较好的使用和定制我们想要展示的内容。

本文从grafana和metabase两个组件出发，分析了其安装和配合ch使用的过程。

# **grafana**

## **安装**

下载安装，见[1][2]，ch需要注意下载插件并解压到插件目录即可。

## **使用**

可以直接导入json来完成clickhouse的看板的生成。

 ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC8zOGUwMmI3N2FjOTg0ZDI0ZDE1YjE1ZGRmMzllOWE3MS8w?x-oss-process=image/format,png)

## **常用的看板**

*ClickHouse Queries*

该看板要开启clickhouse的log配置才可以使用，其它的看板也有和其它监控工具结合完成的。

```
<log_queries>1</log_queries>
1
```

 大部分的监控都是基于system下的query_log,events和metrics等表完成的。

![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC9iOTkwY2IxMjI4ZTcwYzgyMjk4YmY4NjkzNGU5MDJlNy8w?x-oss-process=image/format,png) ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC9hMWFjNWU1MDZjZTg5NjU1MjJmMmVmODNkYTdhZGNhYy8w?x-oss-process=image/format,png)

我们也可以自己定制一些个性化的SQL展示出来，在这一点上与Metabase有点相似。

# **Metabase**

## **安装**

之前笔者也分析过一些关于metabase的安装等文章，所以对metabase比较熟悉，在得知其也支持clickhouse时，当然也是赶紧试用一下。除了metabase，业界使用的最多的是superset工具来做ch的数据分析可视化操作。

### **jar安装**

### java -jar metabase.jar

由于metabase默认没有clickhouse驱动，我们需要在额外添加该插件，如果你已经启动过metabase，会发现当前目录有plugins，将driver放入重启即可。(注意插件与metabase版本的对应关系，不然会出现一些使用异常，不然会出现如下异常)

```
No method in multimethod 'connection-details->spec' for dispatch value: :clickhouse
1
```

根据参考文章中的一些提示可以使用变量指定启动的端口号等配置：

```
export MB_JETTY_PORT=8080
1
```

出现如下异常：**Distinct, non-empty sequence of Field clauses**
​ ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC82NGE3YWNiYmVhMGQzZmIyZWEwYTdiM2I4MzFjMjQ5OS8w?x-oss-process=image/format,png)

这里出现的原因主要是metabase没有扫描完成我们的库，因为metabase需要把我们库中的字段等信息存储起来，没有扫描完成自然就没办法展示数据，笔者使用0.35版本无法扫描完成，更新到0.36版本才正常显示（若依然不能正常显示可在管理页面重新触发扫描）。

### **docker安装**

docker命令：

docker run -d -p 3000:3000 --name metabase metabase/metabase

挂载clickhouse-driver的启动：

```
 docker run -d -p 3000:3000 \
  --mount type=bind,source=/path/to/plugins,destination=/plugins \
  --name metabase metabase/metabase
123
```

 ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC83MmI0ZTc3ZmYyMGI2OTVkZDMzMzdkZjRkN2NlODdiYy8w?x-oss-process=image/format,png)

## **配置**

添加一个数据库：
​ ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC9hNzA2MWRhZmRmMDdmYTBlNzgyZmFjYmE2YTY5MzE3Ny8w?x-oss-process=image/format,png)

## **一些case**

简单的表test_zookeeper（之前测试使用的表），结合官方文档的测试sql，完成几个场景的构建，建表语句：

```
CREATE TABLE default.test_zookeeper
(
    `Id`     Int32,
    `Code`  Int32,
    `Type` String
) ENGINE = ReplicatedMergeTree('/clickhouse/tables/{layer}-{shard}/default_test_zookeeper',
           '{replica}') PARTITION BY Code ORDER BY (Id, Code)
    SETTINGS index_granularity = 8192;
12345678
```

- 求相同code下的最大id趋势图

 ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC8xN2VjNWRjMjNhMmNiYmMxM2U4M2UyMTQ1NGEzY2NmYS8w?x-oss-process=image/format,png)

- 官方的一些分析

![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC82NzQyMzZhYWQzNzMzYzhmZTg4N2NhNWQwMmIxNGRiYi8w?x-oss-process=image/format,png)

首先是当前表的count数，Code Test Zookeeper为Code出现的次数图，Type Test Zookeeper为Type出现的次数图。

- 求Code为2020-2021之间出现的条目次数：

```
select Code,count(Type) as count from test_zookeeper where Code between 2020 and 2021 group by Code ;
1
```

![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC82ODZkMDc1OGQ0YThkMGMwYzc4M2ZiNmQ5OWFiOTYzOS8w?x-oss-process=image/format,png)

- 求Code大于2020，Type中字符出现的次数的排行：

```
select Type, count(*) as count from test_zookeeper where Code > 2020 group by Type order by count(*) desc;
1
```

 ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC83NWQzODYxNzhiNTA5ZjMwY2I0YTY0NGMxZGY1YmQ5Mi8w?x-oss-process=image/format,png)

# **参考文章**

1. grafana-clickhouse插件: https://grafana.com/grafana/plugins/vertamedia-clickhouse-datasource
2. grafana官方：https://grafana.com/
3. grafana-clickhouse相关看板：https://grafana.com/grafana/dashboards?search=clickhouse
4. ClickHouse datasource for Grafana 4.6+：https://github.com/Vertamedia/clickhouse-grafana
5. metabase：
   https://www.metabase.com/
   https://downloads.metabase.com/v0.36.0/metabase.jar
6. metabase挂载clickhouse数据源：https://blog.csdn.net/jiangshouzhuang/article/details/103917772
7. metabase-clickhouse-driver: https://github.com/enqueue/metabase-clickhouse-driver
8. 开源metabase安装：https://www.jianshu.com/p/2ae75635d237