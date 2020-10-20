前言：什么是chproxy？从名字就能了解ch代表clickhouse，proxy为代理，即专为clickhouse使用的代理。在clickhouse集群中，每一台机器都是单独的实例，我们可以使用其中的一台作为查询机器。此时如何更好的完成负载均衡是我们所关注的，chproxy即是这么一个工具。

```
Chproxy, is an http proxy and load balancer for ClickHouse database.
1
```

## **如何使用chproxy**

第一步下载chproxy，可以直接在下方引文中下载，也可以通过命令下载：

```
$ mkdir -p /data/chproxy
$ cd /data/chproxy
$ wget https://github.com/Vertamedia/chproxy/releases/download/v1.14.0/chproxy-linux-amd64-v1.14.0.tar.gz
$ tar -xzvf chproxy-*.gz
1234
```

第二步配置chproxy：

```
server:
  http:
      listen_addr: ":8080" #对外暴露的端口
      #allowed_networks: ["0.0.0.0/0"] #网络访问控制
users:
  - name: "default" #访问时需要写的用户名
    to_cluster: "distributed-write" #当前用户对应的集群
    to_user: "default" 
clusters:
  - name: "distributed-write" # 集群的详细配置
    nodes: [
      "127.0.0.1:8123" # 多个node节点可以在此处填写
    ]
    users:
      - name: "default" # clickhouse的用户名/密码
        password: ""
caches:
  - name: "shortterm"
    dir: "/data/chproxy/cache/shortterm"
    max_size: 150Mb
    expire: 130s

hack_me_please: true # Special option hack_me_please: true may be used for disabling all the security-related checks during config validation(主要是禁用安全控制，也即是所有的ip都放开访问，测试环境可开启)
1234567891011121314151617181920212223
```

多副本集群的配置：

```
server:
  http:
      listen_addr: ":8080"
      allowed_networks: ["0.0.0.0/0"]
users:
  - name: "default"
    to_cluster: "distributed-write"
    to_user: "default"
clusters:
  - name: "distributed-write"
    replicas:
      - name: "replica1"
        nodes: ["127.0.0.1:8123", "127.0.0.1:8125"]
      - name: "replica2"
        nodes: ["127.0.0.1:8124", "127.0.0.1:8126"]
        #假设该集群为双副本集群，此处可以这样配置
    users:
      - name: "default"
        password: ""
caches:
  - name: "shortterm"
    dir: "/data/chproxy/cache/shortterm"
    max_size: 150Mb
    expire: 130s
123456789101112131415161718192021222324
```

更多的配置详情可以在参考[2]中官网配置中查看。

配置启动脚本：

```
$ vim /data/chproxy/restart.sh
#!/bin/bash
cd $(dirname)
ps -ef | grep chproxy | head -2 | tail -1 | awk '{print $2}' | xargs kill -9
sudo -u chproxy nohup ./chproxy-linux-amd64 -config=./config/config.yml >> ./logs/chproxy.out 2>&1 &
12345
```

## **chproxy实际使用**

示例架构：

 ![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC80YjU4M2RmYWU3Mjk4ZGFkNmVmZWMwN2MzZDc1MzQxMS8w?x-oss-process=image/format,png)
*（图片来自参考文章[1]）*

解析：先从上层来看，该作者构建了多个chproxy实例，使用阿里云负载均衡LB来完成不同chproxy的访问均衡。针对ch集群，读操作是在分布表上，并且为其赋予readonly的账户。底层使用复制表存储数据，可通过replica写本地表。

## **一些问题**

**数据应该写分布式表还是直接写本地表？**

建议分布式表只用来读，而写入只写本地表。写分布式表好处是可以通过ch的分布表能力完成数据分发，包括随机分发或者指定hash值分发等，这样最终机器的数据分布大体上一致。特别通过hash值将某个组合列的数据分发到同一台数据，在使用查询时我们可以直接使用本地表进行联查join等操作，此时查询的效率会有较高的提升。（不过此处如果扩容，也会有一定的问题，感兴趣的可以一起讨论这个问题，什么样的hash最好，一致性hash可以解决这个问题么？）

但分布表写入会出现一些问题，导致负载等升高，笔者就遇到过19.9版本分布式表写入会导致内存升高且不下降的问题，最终会被系统kill掉。

使用本地表写有很多种方式，可以自己写均衡模式，分别向集群中不同机器写数据，也可以通过外部的负载均衡来写入本地表，这种方式带来的弊端是有可能导致数据不均匀。当然如果数据量比较大，且每次写入的数据量差距不大，那这种方式就比较好了。

**jdbc使用报错？**

错误备注：

```
Exception in thread "Thread-0" java.lang.RuntimeException: ru.yandex.clickhouse.except.ClickHouseUnknownException: ClickHouse exception, code: 1002, host: 127.0.0.1, port: 8080; Magic is not correct: 251 at ru.yandex.clickhouse.ClickHouseConnectionImpl.initTimeZone(ClickHouseConnectionImpl.java:97)at ru.yandex.clickhouse.ClickHouseConnectionImpl.<init>(ClickHouseConnectionImpl.java:78)at ru.yandex.clickhouse.ClickHouseDriver.connect(ClickHouseDriver.java:55)at ru.yandex.clickhouse.ClickHouseDataSource.getConnection(ClickHouseDataSource.java:44)
java.lang.Thread.run(Thread.java:748)
12
```

该问题排查了很久，最终的解决方案是，在使用url的地方写入地址为：

```
jdbc:clickhouse://127.0.0.1:8123/default?compress=0"
1
```

该设置是禁用压缩。
由于笔者测试的clickhouse启用了该配置<enable_http_compression>1</enable_http_compression>

找到jdbc源码报错的位置为:
![img](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9xcWFkYXB0LnFwaWMuY24vdHhkb2NwaWMvMC9kNGQ1M2QzZDIyZWI3ZDQxNmViYjc3MmI4OGIwMjZmOS8w?x-oss-process=image/format,png)

推测是设置了数据解压，但是发现无法解压，我们在chproxy中没有配置compress，所以把url的compress设置为0时，该错误就消失了。

## **参考文档**

1. clickhouse + chproxy 集群搭建：https://www.jianshu.com/p/9498fedcfee7
2. chproxy官方地址：https://github.com/Vertamedia/chproxy