前言：编译使用的clickhouse版本为: 19.5.3.1，本次测试比较主要以这个版本为主，鉴于clickhouse更新速度快，新版本的使用还需多查看release的更新日志。

### **问题描述**

使用复制表之后，随着数据量的增加，zookeeper是瓶颈？这个问题估计任何一个对ch关注的人都会看到，当然解决这个问题是需要花费较大精力的。本次我主要想分享ch官方团队提出的减压方案，以及我们能通过什么手段来对zookeeper减压。

针对这个问题，最近接触到了头条关于这部分的优化，头条总结的是ch把zookeeper用成了目录服务，日志服务和协调服务，当znode达到几百万后，zk出现异常，常见是连接失败，此时有些表会出现readonly模式。

针对目录服务和日志服务随着数据量的增加会使得zookeeper的镜像快照增加，这个可以参见我的之前的文章关于zookeeper存储的分析。大厂对于该部分的处理是直接重新调整了ch使用zk的方式，用较小的数据量来完成复制表之间的数据一致性校验。这种与数据量无关的zk使用方式目前还不能接触到源码，当然也希望之后能有更多这方面源码的分享。

### **use_minimalistic_part_header_in_zookeeper**

通过创建表指定use_minimalistic_part_header_in_zookeeper参数为1。

从ClickHouse release 19.1.6, 2019-01-24版本开始支持该配置，官方文档解释该配置可向上兼容，但是对于之前的版本应该谨慎使用该配置，减少了zk上的数据可能会导致replica表出现一些未知的错误。**官方文档显示从ClickHouse release v20.1.2.4, 2020-01-22开始默认开启这个配置，当然你要退回19.1之前的版本就需要把这个关闭，不然会出现问题**。

```
Enable use_minimalistic_part_header_in_zookeeper setting for ReplicatedMergeTree by default. This will significantlyreduce amount of data stored in ZooKeeper. This setting is supported since version 19.1 and we already use it inproduction in multiple services without any issues for more than half a year. Disable this setting if you have a chanceto downgrade to versions older than 19.1. 
1
```

首先创建本次使用的表，建表语句：

```
CREATE TABLE default.test_zookeeper
(
    `Id`    Int32,
    `Code`  Int32,
    `Type` String
) ENGINE = ReplicatedMergeTree('/clickhouse/tables/{layer}-{shard}/default_test_zookeeper',
           '{replica}') PARTITION BY Code ORDER BY (Id, Code)
    SETTINGS index_granularity = 8192;
12345678
```

指定参数时的建表语句：

```
CREATE TABLE default.test_zookeeper_minimalistic
(
    `Id`   Int32,
    `Code`  Int32,
    `Type` String
) ENGINE = ReplicatedMergeTree('/clickhouse/tables/{layer}-{shard}/default_test_zookeeper_minimalistic',
           '{replica}') PARTITION BY Code ORDER BY (Id, Code) SETTINGS index_granularity = 8192,
    use_minimalistic_part_header_in_zookeeper = 1;
12345678
```

创建之前的zookeeper的文件夹列表：

 ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC9kZmRhNWU0OWExMDBhMDcxMTk2MmU4ZGU5YjRjZTNkNC8w?x-oss-process=image/format,png)

创建表之后的文件夹列表：

 ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC82ZDQzMDUzNzQ3YTgzODM4MDRjODVmMjc0M2UyNjFjYy8w?x-oss-process=image/format,png)

我们写入几条数据之后查看：

 ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC9kMWZlY2E5NzlkZmVlOWFmYTRlNzA5NjU2NWQ2ZTUwMy8w?x-oss-process=image/format,png)

对比表test_zookeeper

 [外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-RgCxaNbp-1595340297788)(https://qqadapt.qpic.cn/txdocpic/0/78a88c5e87ea4c709aeeb0ee51ba687b/0?w=882&h=1372)]

replicas/parts部分有明显的不同，此处会减少部分数据，减少的部分主要为columns，主要存储的是当前版本的列名和列类型。

执行语句将其完成合并：

```
optimize table  default.test_zookeeper_minimalistic;
optimize table  default.test_zookeeper;
12
```

合并之后，在两者表的log文件夹下都显示出了merge的日志，看来这部分内容还存在了zookeeper中。

### **如何减少日志在zk上的存储**

例如：ReplicatedMergeTreeBlockOutputStream.cpp：

```
//    ops.emplace_back(zkutil::makeCreateRequest(
//        storage.zookeeper_path + "/log/log-",
//        log_entry.toString(),
//        zkutil::CreateMode::PersistentSequential));
LOG_DEBUG(log, "test : Replication log :  " << log_entry.toString() << " .");
12345
```

我们注释该部分，并打印出该部分的内入，发现在zookeeper中的该部分log信息不再存储。

添加log，打印log_entry.toString()的数据：

```
2020.01.21 14:33:51.866180 [ 46 ] {86efe234-9691-4d95-b704-0b3b38adc3e2} <Debug> default.test_test (Replicated OutputStream): Replication log :  format version: 4
create_time: 2020-01-21 14:33:51
source replica: cluster01-01-01
block_id: 202001_976485413081811652_2698895322145168848
get
202001_4_4_0
 .
1234567
```

在源码中可以看到很多类似的写zk的操作，部分log的写入可以省略，但注意该部分只是在测试环境使用，还未经过大规模的验证。

在StorageReplicatedMergeTree中，我们能看到上述使用了配置后的存储的操作，此处就减少了写入的数据，直接写了parts而不是colums和checksums。

 