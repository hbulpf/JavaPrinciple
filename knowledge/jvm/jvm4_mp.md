# JVM系列:(4)JVM基本参数

> 不管是YGC还是FGC,GC过程中都会对导致程序运行中断,正确选择不同的GC策略,调整GC的参数可以极大减少由于GC导致的程序运行中断，从而提高Java程序的效率。然而调整GC参数是一个极为复杂的过程，由于各类程序具有不同的特点，如：web和GUI程序就有很大区别（Web可以适当的停顿，但GUI停顿是客户无法接受的），另外机器配置不同（主要cup个数，内存不同），使用的GC配置也会不同。本文将注重介绍JVM、GC的一些重要参数的设置来提高系统的性能。



## JVM的基本参数

- **-Xms**, 初始堆大小。默认值为物理内存的1/64(<1GB),  默认(MinHeapFreeRatio参数可以调整)空余堆内存小于40%时，JVM就会增大堆直到-Xmx的最大限制.
- **-Xmx**,最大堆大小。默认值为物理内存的1/4(<1GB)，默认(MaxHeapFreeRatio参数可以调整)空余堆内存大于70%时，JVM会减少堆直到 -Xms的最小限制
- **-Xmn**,年轻代大小,此处的大小是（eden+2*survivor space). 与 `jmap -heap` 中显示的New gen是不同的。 整个堆大小=年轻代大小 + 老年代大小 + 持久代大小. 持久代一般固定大小为 64m,增大年轻代后,将会减小老年代大小.此值对系统性能影响较大,Sun官方推荐配置为整个堆的3/8
- **-XX:NewSize**,设置年轻代大小
- **-XX:MaxNewSize**,年轻代最大值
- **-XX:PermSize**,设置持久代(PermGen)初始值,默认值为物理内存的1/64,持久代一般固定大小为 64m
- **-XX:MaxPermSize**,设置持久代最大值,默认值为物理内存的1/64,持久代一般固定大小为 64m
- **-Xss**,每个线程的堆栈大小,JDK5.0以后每个线程堆栈大小为1M,以前每个线程堆栈大小为256K.根据应用的线程所需内存大小进行调整.在相同物理内存下,减小这个值能生成更多的线程.但是操作系统对一个进程内的线程数还是有限制的,不能无限生成,经验值在3000~5000左右 一般小的应用。如果栈不是很深，128k应该够用，大的应用建议使用256k。这个选项对性能影响比较大，需要严格的测试。
- **-XX:ThreadStackSize**，和-Xss相似,官方文档似乎没有解释,在论坛中有这样一句话:" -Xss is translated in a VM flag named ThreadStackSize”。
- **-XX:NewRatio**，年轻代(包括Eden和两个Survivor区)与老年代的比值(除去持久代)，-XX:NewRatio=4表示年轻代与老年代所占比值为1:4,年轻代占整个堆栈的1/5。注意：Xms=Xmx并且设置了Xmn的情况下，该参数不需要进行设置。
- **-XX:SurvivorRatio**，Eden区与Survivor区的大小比值，默认设置为8,两个Survivor区与一个Eden区的比值为2:8,一个Survivor区占整个年轻代的1/10
- **-XX:LargePageSizeInBytes**，内存页的大小不可设置过大， 会影响PermGen的大小。
- **-XX:+UseFastAccessorMethods**,原始类型的快速优化
- **-XX:+DisableExplicitGC**,关闭System.gc(),这个参数需要严格的测试
- **-XX:MaxTenuringThreshold**,垃圾最大年龄。如果设置为0的话,则年轻代对象不经过Survivor区,直接进入老年代。对于老年代比较多的应用,可以提高效率。如果将此值设置为一个较大值,则年轻代对象会在Survivor区进行多次复制,这样可以增加对象在年轻代的存活时间,增加在年轻代即被回收的概率。该参数只有在串行GC时才有效.
- **-XX:+AggressiveOpts**,加快编译
- **-XX:+UseBiasedLocking**,锁机制的性能改善
- **-Xnoclassgc**，禁用垃圾回收  
- **-XX:SoftRefLRUPolicyMSPerMB**,  每兆堆空闲空间中SoftReference的存活时间,默认为1s,
- **-XX:PretenureSizeThreshold**，对象超过多大是直接在老生代分配，默认为0，单位字节新生代采用Parallel Scavenge GC时无效,另一种直接在旧生代分配的情况是大的数组对象,且数组中无外部引用对象.
- **-XX:TLABWasteTargetPercent**，TLAB占eden区的百分比,默认1%
- **-XX:+CollectGen0First**,FullGC时是否先YGC，默认false

## 并行收集器相关参数

- **-XX:+UseParallelGC**,  Full GC采用parallel MSC，选择垃圾收集器为并行收集器.此配置仅对年轻代有效.即该配置下,年轻代使用并发收集,而老年代仍旧使用串行收集。
- **-XX:+UseParNewGC**,设置年轻代为并行收集，可与CMS收集同时使用。 JDK5.0以上,JVM会根据系统配置自行设置,所以无需再设置此值。
- **-XX:ParallelGCThreads**,并行收集器的线程数,此值最好配置与处理器数目相等,同样适用于CMS.
- **-XX:+UseParallelOldGC**,老年代垃圾收集方式为并行收集(Parallel Compacting)。这个是JAVA 6出现的参数选项
- **-XX:MaxGCPauseMillis**，每次年轻代垃圾回收的最长时间(最大暂停时间),如果无法满足此时间,JVM会自动调整年轻代大小,以满足此值。
- **-XX:+UseAdaptiveSizePolicy**，自动选择年轻代区大小和相应的Survivor区比例，  设置此选项后,并行收集器会自动选择年轻代区大小和相应的Survivor区比例,以达到目标系统规定的最低相应时间或者收集频率等,此值建议使用并行收集器时,一直打开.
- **-XX:GCTimeRatio**，设置垃圾回收时间占程序运行时间的百分比,公式为1/(1+n)
- **-XX:+ScavengeBeforeFullGC**，Full GC前调用YGC，默认为true，

## CMS相关参数

- **-XX:+UseConcMarkSweepGC**,使用CMS内存收集，测试中配置这个以后,-XX:NewRatio=4的配置失效了,原因不明.所以,此时年轻代大小最好用-Xmn设置
- **-XX:+AggressiveHeap**,试图是使用大量的物理内存,长时间大内存使用的优化，能检查计算资源（内存， 处理器数量, 至少需要256MB内存
- **-XX:CMSFullGCsBeforeCompaction**,多少次后进行内存压缩,由于并发收集器不对内存空间进行压缩,整理,所以运行一段时间以后会产生"碎片",使得运行效率降低.此值设置运行多少次GC以后对内存空间进行压缩,整理.
- **-XX:+CMSParallelRemarkEnabled**，降低标记停顿
- **-XX+UseCMSCompactAtFullCollection**，在FGC时， 对老年代的压缩。CMS是不会移动内存的， 非常容易产生碎片， 导致内存不够用， 因此， 内存的压缩这个时候就会被启用。 增加这个参数是个好习惯。 可能会影响性能,但是可以消除碎片
-  **-XX:+UseCMSInitiatingOccupancyOnly**，使用手动定义初始化，定义开始CMS收集，实际上禁止hostspot自行触发CMS GC
- **-XX:CMSInitiatingOccupancyFraction=70**,使用CMS作为垃圾回收,使用70％后开始CMS收集，默认为92
- **-XX:CMSInitiatingPermOccupancyFraction**，  设置Perm Gen使用到达多少比率时触发，默认为92
- **-XX:+CMSIncrementalMode**，设置为增量模式,主要用于单CPU情况

## 辅助信息

- **-XX:+PrintGC**,输出形式: [GC 118250K->113543K(130112K), 0.0094143 secs] [Full GC 121376K->10414K(130112K), 0.0650971 secs]
- **-XX:+PrintGCDetails**,输出形式:[GC [DefNew: 8614K->781K(9088K), 0.0123035 secs] 118250K->113543K(130112K), 0.0124633 secs] [GC [DefNew: 8614K->8614K(9088K), 0.0000665 secs ][Tenured: 112761K->10414K(121024K), 0.0433488 secs] 121376K->10414K(130112K), 0.0436268 secs]
- **-XX:+PrintGCTimeStamps**
- **-XX:+PrintGC:PrintGCTimeStamps**，可与-XX:+PrintGC、 -XX:+PrintGCDetails混合使用，输出形式: 11.851: [GC 98328K->93620K(130112K), 0.0082960 secs]
- **-XX:+PrintGCApplicationStoppedTime**,打印垃圾回收期间程序暂停的时间.可与上面混合使用.输出形式:Total time for which application threads were stopped: 0.0468229 seconds
- **-XX:+PrintGCApplicationConcurrentTime**,打印每次垃圾回收前,程序未中断的执行时间.可与上面混合使用,输出形式:Application time: 0.5291524 seconds
- **-XX:+PrintHeapAtGC**,打印GC前后的详细堆栈信息
- **-Xloggc:filename**,把相关日志信息记录到文件以便分析. 与上面几个配合使用
- **-XX:+PrintClassHistogram**， 垃圾会在打印直方图之前收集。
- **-XX:+PrintTLAB**,查看TLAB空间的使用情况
- **-XX:+PrintTenuringDistribution**，查看YGC后新的存活周期的阈值,所需的幸存者大小1048576字节，新阈值7（最大15）





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

