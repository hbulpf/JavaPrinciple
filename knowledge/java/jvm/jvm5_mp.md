# JVM系列:(5)JVM参数优化经验

> JVM参数优化主要从吞吐量throughput和服务暂停pause时间两个要素来考虑，优化主要是对堆区参数设置的优化，基于这些考虑，本文给出了一些JVM经验设置规则。


## GC性能的考虑

对于GC的性能主要有2个方面的指标：吞吐量throughput（工作时间不算gc的时间占总的时间比）和暂停pause（gc发生时app对外显示无法响应）。

### 1. Total Heap

默认情况下，vm会增加/减少堆大小以维持free space在整个vm中占的比例，这个比例由MinHeapFreeRatio和MaxHeapFreeRatio指定。

一般而言，server端的app会有以下规则：

1. 对vm分配尽可能多的内存；
2. 将Xms和Xmx设为一样的值。如果虚拟机启动时设置使用的内存比较小，这个时候又需要初始化很多对象，虚拟机就必须重复地增加内存。
3. 处理器核数增加，内存也跟着增大。

### 2. Young Generation

- 另一个影响app流畅性的因素是young generation的大小。young generation越大，YGC越少；但是在固定堆大小情况下，更大的young generation就意味着小的tenured generation，就意味着更多的FGC(FGC会引发YGC)。
- NewRatio反映的是young generation和tenured generation的大小比例。NewSize和MaxNewSize反映的是young generation大小的下限和上限，将这两个值设为一样就固定了young generation的大小
- SurvivorRatio是eden和survior大小比例，调整SurvivorRatio也可以优化survivor的大小，但这对于性能的影响不是很大。

一般而言，server端的app会有以下规则：

1. 首先决定能分配给vm的最大的堆内存，然后设定最佳的young generation的大小；
2. 堆内存固定后，增加young generation的大小意味着减小tenured generation大小。让tenured generation在任何时候足够大，能够容纳所有存活的对象（可以留10%-20%的空余）。

## 经验设置规则

1. 年轻代大小选择
  
   - 响应时间优先的应用: 尽可能设大,直到接近系统的最低响应时间限制(根据实际情况选择).在此种情况下,年轻代收集发生的频率也是最小的.同时,减少到达老年代的对象.
   - 吞吐量优先的应用: 尽可能的设置大,可能到达Gbit的程度.因为对响应时间没有要求,垃圾收集可以并行进行,一般适合8CPU以上的应用.
     避免设置过小.当新生代设置过小时会导致:1.YGC次数更加频繁 2.可能导致YGC对象直接进入tenured generation,如果此时tenured generation满了,会触发FGC.
   
2. 老年代大小选择
   - 响应时间优先的应用:老年代使用并发收集器,所以其大小需要小心设置。一般要考虑并发会话率和会话持续时间等一些参数.如果堆设置小了,会造成内存碎片,高回收频率以及应用暂停;如果堆大了,则需要较长的收集时间.最优化的方案,一般需要参考这些数据获得:并发垃圾收集信息、持久代并发收集次数、传统GC信息、花在年轻代和老年代回收上的时间比例。
   
   - 吞吐量优先的应用:一般吞吐量优先的应用都有一个很大的年轻代和一个较小的老年代.原因是,这样可以尽可能回收掉大部分短期对象,减少中期的对象,而老年代尽存放长期存活对象.
   
3. 较小堆引起的碎片问题 
   因为老年代的并发收集器使用标记-清除算法,所以不会对堆进行压缩.当收集器回收时,会把相邻的空间进行合并,这样可以分配给较大的对象.但是,当堆空间较小时,运行一段时间以后,就会出现"碎片",如果并发收集器找不到足够的空间,那么并发收集器将会停止,然后使用传统的标记-清除方式进行回收.如果出现"碎片",可能需要进行如下配置:  
   
   * -XX:+UseCMSCompactAtFullCollection:使用并发收集器时,开启对老年代的压缩.  
   * -XX:CMSFullGCsBeforeCompaction=0:上面配置开启的情况下,这里设置多少次FGC后,对老年代进行压缩
   
4. 用64位操作系统，Linux下64位的jdk比32位jdk要慢一些，但是吃得内存更多，吞吐量更大

5. Xmx和Xms设置一样大，MaxPermSize和MinPermSize设置一样大，这样可以减轻伸缩堆大小带来的压力

6. 使用CMS的好处是用尽量少的新生代，经验值是128M－256M， 老生代利用CMS并行收集， 这样能保证系统低延迟的吞吐效率。 实际上CMS的收集停顿时间非常的短，2G的内存， 大约20－80ms的应用程序停顿时间

7. 系统停顿的时候可能是GC的问题也可能是程序的问题，多用jmap和jstack查看，或者killall -3 java，然后查看java控制台日志，能看出很多问题。

8. 仔细了解自己的应用，如果用了缓存，那么老年代应该大一些，缓存的HashMap不应该无限制长，建议采用LRU算法的Map做缓存，LRUMap的最大长度也要根据实际情况设定。

9. 采用并发回收时，年轻代小一点，老年代要大，因为年老大用的是并发回收，即使时间长点也不会影响其他程序继续运行，网站不会停顿

10. JVM参数的设置(特别是 –Xmx –Xms –Xmn -XX:SurvivorRatio -XX:MaxTenuringThreshold等参数的设置没有一个固定的公式，需要根据实际数据、YGC次数等多方面来衡量。

   为了避免promotion faild可能会导致-Xmn设置偏小，也意味着YGC的次数增多，处理并发访问的能力下降等问题。每个参数的调整都需要经过详细的性能测试，才能找到特定应用的最佳配置。

### promotion failed

垃圾回收时promotion failed是个很头痛的问题，一般可能是两种原因产生，第一个原因是Survivor空间不够，Survivor空间里的对象还不应该被移动到老年代，但年轻代又有很多对象需要放入Survivor空间；第二个原因是老年代没有足够的空间接纳来自年轻代的对象；这两种情况都会转向Full GC，网站停顿时间较长。

解决方方案一：

* 第一个原因的最终解决办法是去掉Survivor空间，设置 `-XX:SurvivorRatio=65536 -XX:MaxTenuringThreshold=0`
即可，第二个原因的解决办法是设置 CMSInitiatingOccupancyFraction 为某个值（假设70），这样老年代空间到70%时就开始执行CMS，老年代有足够的空间接纳来自年轻代的对象。

解决方案一的改进方案：

* 上面方法不太好，因为没有用到Survivor空间，所以老年代容易满，CMS执行会比较频繁。改善一下，还是用Survivor空间，但是把Survivor空间加大，这样也不会有promotion failed。

  具体操作上，32位Linux和64位Linux不一样，64位系统只要配置MaxTenuringThreshold参数，CMS还是有暂停。为了解决暂停问题和promotion failed问题，最后设置 `-XX:SurvivorRatio=1` ，并把MaxTenuringThreshold 去掉，这样既没有暂停又不会有promotoin failed，而且更重要的是，老年代和永久代上升非常慢（因为好多对象到不了老年代就被回收了），所以CMS执行频率非常低，好几个小时才执行一次，这样，服务器都不用重启了。配置如下：

   ```
   -Xmx4000M -Xms4000M -Xmn600M -XX:PermSize=500M -XX:MaxPermSize=500M -Xss256K 
   -XX:+DisableExplicitGC -XX:SurvivorRatio=1 -XX:+UseConcMarkSweepGC 
   -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection 
   -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSClassUnloadingEnabled 
   -XX:LargePageSizeInBytes=128M -XX:+UseFastAccessorMethods 
   -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=80 
   -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+PrintClassHistogram -XX:+PrintGCDetails 
   -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xloggc:log/gc.log
   ```


### CMSInitiatingOccupancyFraction值与Xmn的关系公式

上面介绍了promontion faild产生的原因是Eden空间不足的情况下将Eden与From survivor中的存活对象存入To survivor区时,To survivor区的空间不足，再次晋升到old gen区，而old gen区内存也不够的情况下产生了promontion faild从而导致full gc。那可以推断出：Eden+from survivor < old gen区剩余内存时，不会出现promontion faild的情况，即：

```
(Xmx-Xmn)*(1-CMSInitiatingOccupancyFraction/100)>=(Xmn-Xmn/(SurvivorRatio+2)) 
```

进而推断出：

```
CMSInitiatingOccupancyFraction <=((Xmx-Xmn)-(Xmn-Xmn/(SurvivorRatio+2)))/(Xmx-Xmn)*100
```

例如：
```
当Xmx=128 Xmn=36 SurvivorRatio=1时 
CMSInitiatingOccupancyFraction<=((128.0-36)-(36-36/(1+2)))/(128-36)*100 =73.913

当Xmx=128 Xmn=24 SurvivorRatio=1时 
CMSInitiatingOccupancyFraction<=((128.0-24)-(24-24/(1+2)))/(128-24)*100=84.615…

当Xmx=3000 Xmn=600 SurvivorRatio=1时 
CMSInitiatingOccupancyFraction<=((3000.0-600)-(600-600/(1+2)))/(3000-600)*100=83.33

CMSInitiatingOccupancyFraction低于70% 需要调整Xmn或SurvivorRatio值。
```




# 参考

1. [JVM系列三:JVM参数设置、分析](https://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html) .https://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html)
1. [JVM -JVM优化参数设置](https://www.liangzl.com/get-article-detail-151737.html).https://www.liangzl.com/get-article-detail-151737.html
2. [JAVA HOTSPOT VM](http://www.helloying.com/blog/archives/164).http://www.helloying.com/blog/archives/164
3. [JVM 几个重要的参数](http://www.iteye.com/wiki/jvm/2870-JVM) .http://www.iteye.com/wiki/jvm/2870-JVM
4. [java jvm 参数 -Xms -Xmx -Xmn -Xss 调优总结](http://hi.baidu.com/sdausea/blog/item/c599ef13fcd3a7dbf6039e12.html).http://hi.baidu.com/sdausea/blog/item/c599ef13fcd3a7dbf6039e12.html
5. [Java HotSpot VM Options](http://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html) . http://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html
6. [Frequently Asked Questions About the Java HotSpot VM](http://www.oracle.com/technetwork/java/hotspotfaq-138619.html) . http://www.oracle.com/technetwork/java/hotspotfaq-138619.html
7. [Java SE HotSpot at a Glance](http://www.oracle.com/technetwork/java/javase/tech/index-jsp-136373.html) . http://www.oracle.com/technetwork/java/javase/tech/index-jsp-136373.html
8. [Java性能调优笔记](http://blog.csdn.net/yang_net/archive/2010/08/22/5830820.aspx) . http://blog.csdn.net/yang_net/archive/2010/08/22/5830820.aspx
9. [说说MaxTenuringThreshold这个参数](http://blog.bluedavy.com/?p=70). http://blog.bluedavy.com/?p=70
10. [GC调优方法总结](http://blog.csdn.net/pigeon21/archive/2011/01/27/6166217.aspx).http://blog.csdn.net/pigeon21/archive/2011/01/27/6166217.aspx
11. [Java 6 JVM参数选项大全（中文版）](http://kenwublog.com/docs/java6-jvm-options-chinese-edition.htm).http://kenwublog.com/docs/java6-jvm-options-chinese-edition.htm

