# JVM系列:(5)生产环境实例分析

## java application项目（非web项目）

改进前：

```
-Xms128m
-Xmx128m
-XX:NewSize=64m
-XX:PermSize=64m
-XX:+UseConcMarkSweepGC
-XX:CMSInitiatingOccupancyFraction=78
-XX:ThreadStackSize=128
-Xloggc:logs/gc.log
-Dsun.rmi.dgc.server.gcInterval=3600000
-Dsun.rmi.dgc.client.gcInterval=3600000
-Dsun.rmi.server.exceptionTrace=true
```

问题:

1. permsize 设置较小,很容易达到报警范围(0.8)
2. 没有设置MaxPermSize，堆增长会带来额外压力。
3. NewSize较大，old gen 剩余空间64m，一方面可能会带来old区容易增长到报警范围（监控数据显示oldgenused长期在50m左右，接近78%，容易出现full gc）,另一方面也存在promontion fail风险

改进后：

```
-Xms128m
-Xmx128m
-Xmn24m
-XX:PermSize=80m
-XX:MaxPermSize=80m
-Xss256k
-XX:SurvivorRatio=1
-XX:MaxTenuringThreshold=20
-XX:+UseParNewGC
-XX:+UseConcMarkSweepGC
-XX:CMSInitiatingOccupancyFraction=75
-XX:+UseCMSCompactAtFullCollection
-XX:+CMSParallelRemarkEnabled
-XX:CMSFullGCsBeforeCompaction=2
-XX:SoftRefLRUPolicyMSPerMB=0
-XX:+PrintClassHistogram
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
-XX:+PrintHeapAtGC
-Xloggc:logs/gc.log
-Dsun.rmi.dgc.server.gcInterval=3600000
-Dsun.rmi.dgc.client.gcInterval=3600000
-Dsun.rmi.server.exceptionTrace=true
```

修改点：

1. PermSize与MaxPermSize都设置为80，一方面避免non heap warn(报警阀值0.8 非对内存一般占用到60M以内），一方面避免堆伸缩带来的压力
2. 通过设置Xmn=24M及SurvivorRatio=1 使得Eden区=from space=to space=8M,降低了Eden区大小，降低YGC的时间(降低到3-4ms左右),同时通过设MaxTenuringThreshold=20，使得old gen的增长很缓慢。带来的问题是YGC的次数明显提高了很多。
3. 其他参数优化 修改后带来的好处见[JVM参数设置](http://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html)

再次改进后

```
-Xms128m
-Xmx128m
-Xmn36m
-XX:PermSize=80m
-XX:MaxPermSize=80m
-Xss256k
-XX:SurvivorRatio=1
-XX:MaxTenuringThreshold=20
-XX:+UseParNewGC
-XX:+UseConcMarkSweepGC
-XX:CMSInitiatingOccupancyFraction=73
-XX:+UseCMSCompactAtFullCollection
-XX:+CMSParallelRemarkEnabled
-XX:CMSFullGCsBeforeCompaction=2
-XX:SoftRefLRUPolicyMSPerMB=0
-XX:+PrintClassHistogram
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
-XX:+PrintHeapAtGC
-Xloggc:logs/gc.log
-Dsun.rmi.dgc.server.gcInterval=3600000
-Dsun.rmi.dgc.client.gcInterval=3600000
-Dsun.rmi.server.exceptionTrace=true
```

修改点：

   在上面的基础上调整Xmn大小到36M，设置CMSInitiatingOccupancyFraction=73。

   Dden区与Survivor区大小都增加到12M，通过[CMSInitiatingOccupancyFraction计算公式](http://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html#CMSInitiatingOccupancyFraction_value),计算得出value为73是，可以避免promotion faild问题，同时满足堆内存监控报警值在80%：内存大小128M*80%=102.4M 102.4M-36M=66.4M(老生代达到此值报警） 老生代达到67.15M（92M*0.73）将发生Full GC，所以在老生代大小达到66.4M时也就是WARN报警时将很有可能出现Full GC。

​    增大了Eden和Survivor区的值，会减小YGC的次数，但由于空间变大理论上也会相应的增加YGC的时间，不过由于新生代本身就很小（才36M）这点儿变化可以忽略掉。实际的监控值显示YGC的时间在4-5ms之间。是可以接受范围。

   SurvivorRatio 这个值还得在仔细考虑下,有待优化中

 

**网上某个牛人的配置 :每天几百万pv一点问题都没有，网站没有停顿**

```
JAVA_ARGS=
"
-Dresin.home=$SERVER_ROOT
-server
-Xms6000M
-Xmx6000M
-Xmn500M
-XX:PermSize=500M
-XX:MaxPermSize=500M
-XX:SurvivorRatio=65536
-XX:MaxTenuringThreshold=0
-Xnoclassgc
-XX:+DisableExplicitGC
-XX:+UseParNewGC
-XX:+UseConcMarkSweepGC
-XX:+UseCMSCompactAtFullCollection
-XX:CMSFullGCsBeforeCompaction=0
-XX:+CMSClassUnloadingEnabled
-XX:-CMSParallelRemarkEnabled
-XX:CMSInitiatingOccupancyFraction=90
-XX:SoftRefLRUPolicyMSPerMB=0
-XX:+PrintClassHistogram
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
-XX:+PrintHeapAtGC
-Xloggc:log/gc.log
"
```

说明一下， -XX:SurvivorRatio=65536 -XX:MaxTenuringThreshold=0就是去掉了救助空间； 

-Xnoclassgc禁用类垃圾回收，性能会高一点； 

-XX:+DisableExplicitGC禁止System.gc()，免得程序员误调用gc方法影响性能； 

-XX:+UseParNewGC，对年轻代采用多线程并行回收，这样收得快； 

带CMS参数的都是和并发回收相关的，不明白的可以上网搜索； 

CMSInitiatingOccupancyFraction，这个参数设置有很大技巧，基本上满足(Xmx-Xmn)*(100-CMSInitiatingOccupancyFraction)/100>=Xmn就不会出现promotion failed。在我的应用中Xmx是6000，Xmn是500，那么Xmx-Xmn是5500兆，也就是年老代有5500兆，CMSInitiatingOccupancyFraction=90说明年老代到90%满的时候开始执行对年老代的并发垃圾回收（CMS），这时还剩10%的空间是5500*10%=550兆，所以即使Xmn（也就是年轻代共500兆）里所有对象都搬到年老代里，550兆的空间也足够了，所以只要满足上面的公式，就不会出现垃圾回收时的promotion failed； 

SoftRefLRUPolicyMSPerMB这个参数我认为可能有点用，官方解释是softly reachable objects will remain alive for some amount of time after the last time they were referenced. The default value is one second of lifetime per free megabyte in the heap，我觉得没必要等1秒； 

```
-Xmx4000M
-Xms4000M
-Xmn600M
-XX:PermSize=500M
-XX:MaxPermSize=500M
-Xss256K
-XX:+DisableExplicitGC
-XX:SurvivorRatio=1
-XX:+UseConcMarkSweepGC
-XX:+UseParNewGC
-XX:+CMSParallelRemarkEnabled
-XX:+UseCMSCompactAtFullCollection
-XX:CMSFullGCsBeforeCompaction=0
-XX:+CMSClassUnloadingEnabled
-XX:LargePageSizeInBytes=128M
-XX:+UseFastAccessorMethods
-XX:+UseCMSInitiatingOccupancyOnly
-XX:CMSInitiatingOccupancyFraction=80
-XX:SoftRefLRUPolicyMSPerMB=0
-XX:+PrintClassHistogram
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
-XX:+PrintHeapAtGC
-Xloggc:log/gc.log
```

改进方案：

上面方法不太好，因为没有用到救助空间，所以年老代容易满，CMS执行会比较频繁。我改善了一下，还是用救助空间，但是把救助空间加大，这样也不会有promotion failed。
具体操作上，32位Linux和64位Linux好像不一样，64位系统似乎只要配置MaxTenuringThreshold参数，CMS还是有暂停。为了解决暂停问题和promotion failed问题，最后我设置-XX:SurvivorRatio=1 ，并把MaxTenuringThreshold去掉，这样即没有暂停又不会有promotoin failed，而且更重要的是，年老代和永久代上升非常慢（因为好多对象到不了年老代就被回收了），所以CMS执行频率非常低，好几个小时才执行一次，这样，服务器都不用重启了。


某网友:

```
JAVA_ARGS=
"
-Dresin.home=$SERVER_ROOT
-server
-Xmx3000M
-Xms3000M
-Xmn600M
-XX:PermSize=500M
-XX:MaxPermSize=500M
-Xss256K
-XX:+DisableExplicitGC
-XX:SurvivorRatio=1
-XX:+UseConcMarkSweepGC
-XX:+UseParNewGC
-XX:+CMSParallelRemarkEnabled
-XX:+UseCMSCompactAtFullCollection
-XX:CMSFullGCsBeforeCompaction=0
-XX:+CMSClassUnloadingEnabled
-XX:LargePageSizeInBytes=128M
-XX:+UseFastAccessorMethods
-XX:+UseCMSInitiatingOccupancyOnly
-XX:CMSInitiatingOccupancyFraction=70
-XX:SoftRefLRUPolicyMSPerMB=0
-XX:+PrintClassHistogram
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
-XX:+PrintHeapAtGC
-Xloggc:log/gc.log
";
```

64位jdk参考设置，年老代涨得很慢，CMS执行频率变小，CMS没有停滞，也不会有promotion failed问题，内存回收得很干净

 

## 参考

[JVM参数调优，无停滞实践](http://www.cjsdn.net/post/view?bid=62&id=198084&sty=1&tpg=1&age=0]http://www.cjsdn.net/post/view?bid=62&id=198084&sty=1&tpg=1&age=0)