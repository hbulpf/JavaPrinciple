# JVM系列:(2)HotSpot垃圾收集器的种类

> JVM垃圾收集器是GC过程的核心组件，本文以HotSpot虚拟机为例，主要介绍 serial collector、parallel collector、concurrent collector 3种垃圾收集器并比较了他们的区别。

## Collector种类   

GC在 HotSpot VM 5.0里有四种：

~~incremental (sometimes called train) low pause collector~~已被废弃,不再介绍.

|**类别**                     | serial collector                                             | parallel collector(throughput collector)              | concurrent collector(concurrent low pause collector)     |
| ---------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
|**介绍**                     | 单线程收集器 使用单线程去完成所有的gc工作，没有线程间的通信，这种方式会相对高效 | 并行收集器 使用多线程的方式，利用多CPU来提高GC的效率 主要以到达一定的吞吐量为目标 | 并发收集器 使用多线程的方式,利用多CPU来提高GC的效率 并发完成大部分工作，使得gc pause短 |
|**试用场景**                 | 单处理器机器且没有pause time的要求                           | 适用于科学技术和后台处理 有中规模/大规模数据集大小的应用且运行在多处理器上，关注吞吐量(throughput) | 适合中规模/大规模数据集大小的应用，应用服务器,电信领域 关注response time，而不是throughput |
|**使用**                     | Client模式下默认可用<br/> 可用`-XX:+UseSerialGC` 强制使用 <br/>优点:对server应用没什么优点 <br/>缺点:慢,不能充分发挥硬件资源 | Server模式下默认<br/>--YGC:PS FGC:Parallel MSC<br/>可用`-XX:+UseParallelGC` 或 `-XX:+UseParallelOldGC` 强制指定<br/>--ParallelGC代表FGC<br/>为Parallel MSC<br/>--ParallelOldGC代表FGC为Parallel Compacting<br/>优点:高效**<br/>缺点**:当heap变大后,造成的暂停时间会变得比较长 | 可用 `-XX:+UseConcMarkSweepGC` 强制指定 <br/>**<br/>优点**:对old进行回收时,对应用造成的暂停时间非常端,适合对latency要求比较高的应用 <br/> **<br/>缺点**:<br/>1.内存碎片和浮动垃圾<br/>2.old区的内存分配效率低<br/>3.回收的整个耗时比较长<br/>4.和应用争抢CPU |
|**内存回收触发**             |**YGC**<br/> eden空间不足 <br/>**FGC**<br/>old空间不足 <br/>perm空间不足 <br/>显示调用System.gc() ,包括RMI等的定时触发YGC<br/>时的悲观策略 <br/>dump live的内存信息时(jmap –dump:live) |**YGC**<br/>eden空间不足<br/> **FGC**<br/> old空间不足 <br/>perm空间不足 <br/>显示调用System.gc() ,包括RMI等的定时触发YGC的悲观策略<br/> YGC前&YGC后 <br/>dump live的内存信息时(jmap –dump:live) |**YGC**<br/> eden空间不足<br/> **CMS GC**<br/> 1.old Gen的使用率大的一定的比率 默认为92% <br/>2.配置了CMSClassUnloadingEnabled,且Perm Gen的使用达到一定的比率 默认为92% <br/>3.Hotspot自己根据估计决定是否要触法 <br/>4.在配置了ExplictGCInvokesConcurrent的情况下显示调用了System.gc.**<br/> Full GC(Serial MSC)**<br/> promotion failed 或 concurrent Mode Failure时; |
|**内存回收触发时发生了什么** |**YGC**<br/>清空eden+from中所有no ref的对象占用的内存 <br/>将eden+from中的所有存活的对象copy到to中,在这个过程中一些对象将晋升到old中: to放不下的,存活次数超过tenuring threshold的 <br/>重新计算Tenuring Threshold; <br/>单线程做以上动作,全程暂停应用<br/> **FGC**<br/> 如果配置了CollectGen0First,则先触发YGC<br/> 清空heap中no ref的对象,permgen中已经被卸载的classloader中加载的class的信息<br/> 单线程做以上动作,全程暂停应用 |**YGC**<br/> 同serial动作基本相同,不同点: <br/>1.多线程处理 2.YGC<br/>的最后不仅重新计算Tenuring Threshold,还会重新调整Eden和From的大小<br/> FGC<br/> 1.如配置了ScavengeBeforeFullGC(默认),则先触发YGC<br/> 2.MSC:清空heap中的no ref对象,permgen中已经被卸载的classloader中加载的class信息,并进行压缩 3.Compacting:清空heap中部分no ref的对象,permgen中已经被卸载的classloader中加载的class信息,并进行部分压缩 多线程做以上动作. |**YGC**<br/> 同serial动作基本相同,不同点: 1.多线程处理<br/> **CMSGC**<br/> 1.old gen到达比率时只清除old gen中no ref的对象所占用的空间 <br/>2.perm gen到达比率时只清除已被清除的classloader加载的class信息<br/>**FGC**<br/> 同serial |
|**细节参数**                 | `-XX:+UseSerialGC`强制使用 <br/>`-XX:SurvivorRatio=x`控制eden/s0/s1的大小 <br/>`-XX:MaxTenuringThreshold`,用于控制对象在新生代存活的最大次数 <br/>`-XX:PretenureSizeThreshold=x`,控制超过多大的字节的对象就在old分配. | `-XX:SurvivorRatio=x`,控制eden/s0/s1的大小 <br/>`-XX:MaxTenuringThreshold`,用于控制对象在新生代存活的最大次数 <br/>`-XX:UseAdaptiveSizePolicy` 去掉YGC<br/>后动态调整eden from已经tenuringthreshold的动作<br/>`-XX:ParallelGCThreads` 设置并行的线程数 | `-XX:CMSInitiatingOcCPUancyFraction` 设置old gen使用到达多少比率时触发 <br/>`-XX:CMSInitiatingPermOcCPUancyFraction`,设置Perm Gen使用到达多少比率时触发 <br/>`-XX:+UseCMSInitiatingOcCPUancyOnly`禁止hostspot自行触发CMS GC |



![](imgs/jvm3.jpg)

注：

- throughput collector与concurrent low pause collector的区别是throughput collector只在young area使用使用多线程，而concurrent low pause collector则在tenured generation也使用多线程。
- 根据官方文档，他们俩个需要在多CPU的情况下，才能发挥作用。在一个CPU的情况下，会不如默认的serial collector，因为线程管理需要耗费CPU资源。而在两个CPU的情况下，也提高不大。只是在更多CPU的情况下，才会有所提高。当然 concurrent low pause collector有一种模式可以在CPU较少的机器上，提供尽可能少的停顿的模式，见**CMS GC Incremental mode**。
- 当要使用throughput collector时，在java opt里加上-XX:+UseParallelGC，启动throughput collector收集。也可加上-XX:ParallelGCThreads=<desired number>来改变线程数。还有两个参数 -XX:MaxGCPauseMillis=<nnn>和 -XX:GCTimeRatio=<nnn>，MaxGCPauseMillis=<nnn>用来控制最大暂停时间，而-XX: GCTimeRatio可以提高GC说占CPU的比，以最大话的减小heap。

**附注SUN的官方说明**： 

> 1. The throughput collector: this collector uses a parallel version of the young generation collector. It is used if the -XX:+UseParallelGC option is passed on the command line. The tenured generation collector is the same as the serial collector.
>
> 2. The concurrent low pause collector: this collector is used if the -Xincgc™ or -XX:+UseConcMarkSweepGC is passed on the command line. The concurrent collector is used to collect the tenured generation and does most of the collection concurrently with the execution of the application. The application is paused for short periods during the collection. A parallel version of the young generation copying collector is used with the concurrent collector. The concurrent low pause collector is used if the option -XX:+UseConcMarkSweepGC is passed on the command line.
>
> 3. The incremental (sometimes called train) low pause collector: this collector is used only if -XX:+UseTrainGC is passed on the command line. This collector has not changed since the J2SE Platform version 1.4.2 and is currently not under active development. It will not be supported in future releases. Please see the 1.4.2 GC Tuning Document for information on this collector.

## CMS GC Incremental mode

当要使用 concurrent low pause collector时，在java的opt里加上 -XX:+UseConcMarkSweepGC。concurrent low pause collector还有一种为CPU少的机器准备的模式，叫Incremental mode。这种模式使用一个CPU来在程序运行的过程中GC，只用很少的时间暂停程序，检查对象存活。

在Incremental mode里，每个收集过程中，会暂停两次，第二次略长。第一次用来，简单从root查询存活对象。第二次用来，详细检查存活对象。整个过程如下：

1. stop all application threads; do the initial mark; resume all application threads（第一次暂停，初始化标记）
2. do the concurrent mark (uses one procesor for the concurrent work)（运行时标记）
3. do the concurrent pre-clean (uses one processor for the concurrent work)（准备清理）
4. stop all application threads; do the remark; resume all application threads（第二次暂停，标记，检查）
5. do the concurrent sweep (uses one processor for the concurrent work)(运行过程中清理)
6. do the concurrent reset (uses one processor for the concurrent work)(复原)

当要使用Incremental mode时，需要使用以下几个变量：

```
-XX:+CMSIncrementalMode default: disabled 启动CMS模式（must with -XX:+UseConcMarkSweepGC）
-XX:+CMSIncrementalPacing default: disabled 提供自动校正功能
-XX:CMSIncrementalDutyCycle=<N> default: 50 启动CMS的上线
-XX:CMSIncrementalDutyCycleMin=<N> default: 10 启动CMS的下线
-XX:CMSIncrementalSafetyFactor=<N> default: 10 用来计算循环次数
-XX:CMSIncrementalOffset=<N> default: 0 最小循环次数（This is the percentage (0-100) by which the incremental mode duty cycle is shifted to the right within the period between minor collections.）
-XX:CMSExpAvgFactor=<N> default: 25 提供一个指导收集数
```

SUN推荐的使用参数是：

```
-XX:+UseConcMarkSweepGC \
-XX:+CMSIncrementalMode \
-XX:+CMSIncrementalPacing \
-XX:CMSIncrementalDutyCycleMin=0 \
-XX:CMSIncrementalDutyCycle=10 \
-XX:+PrintGC Details \
-XX:+PrintGCTimeStamps \
-XX:-TraceClassUnloading
```

注：如果使用throughput collector和concurrent low pause collector，这两种垃圾收集器，需要适当的提高内存大小，以为多线程做准备。

**如何选择collector:**

- app运行在单处理器机器上且没有pause time的要求，让vm选择或者`UseSerialGC`.
- 重点考虑peak application performance(高性能)，没有pause time太严格要求，让vm选择或者UseParallelGC+`UseParallelOldGC` (optionally).
- 重点考虑response time,pause time要小 `UseConcMarkSweepGC`.

**Summary**

```
import java.util.ArrayList;
import java.util.List;
public class SummaryCase {
    public static void main(String[] args) throws InterruptedException {
        List<Object> caches = new ArrayList<Object>();
        for (int i = 0; i < 7; i++) {
            caches.add(new byte[1024 * 1024 * 3]);
            Thread.sleep(1000);
        }
        caches.clear();
        for (int i = 0; i < 2; i++) {
            caches.add(new byte[1024 * 1024 * 3]);
            Thread.sleep(1000);
        }
    }
}
```



用以下两种参数执行,会执行几次YGC几次FGC?

- `-Xms30M -Xmx30M -Xmn10M -Xloggc:gc.log -XX:+PrintTenuringDistribution -XX:+UseParallelGC`

  ```
    Java HotSpot(TM) 64-Bit Server VM (25.45-b02) for windows-amd64 JRE (1.8.0_45-b15), built on Apr 30 2015 12:40:44 by "java_re" with MS VC++ 10.0 (VS2010)
    Memory: 4k page, physical 8236488k(695548k free), swap 14421096k(1910172k free)
    CommandLine flags: -XX:InitialHeapSize=31457280 -XX:MaxHeapSize=31457280 -XX:MaxNewSize=10485760 -XX:NewSize=10485760 -XX:+PrintGC -XX:+PrintGCTimeStamps -XX:+PrintTenuringDistribution -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC 
    1.153: [GC (Allocation Failure) 
    Desired survivor size 1048576 bytes, new threshold 7 (max 15)
     6101K->4132K(29696K), 0.0043983 secs]
    3.159: [GC (Allocation Failure) 
    Desired survivor size 1048576 bytes, new threshold 7 (max 15)
     10419K->10316K(29696K), 0.0061822 secs]
    5.168: [GC (Allocation Failure) 
    Desired survivor size 1048576 bytes, new threshold 7 (max 15)
     16581K->16500K(29696K), 0.0049605 secs]
    5.173: [Full GC (Ergonomics)  16500K->16297K(29696K), 0.0072860 secs]
    7.181: [Full GC (Ergonomics)  22577K->933K(29696K), 0.0169888 secs]
  ```


- `-Xms30M -Xmx30M -Xmn10M -Xloggc:gc.log -XX:+PrintTenuringDistribution -XX:+UseSerialGC`

  ```
    Java HotSpot(TM) 64-Bit Server VM (25.45-b02) for windows-amd64 JRE (1.8.0_45-b15), built on Apr 30 2015 12:40:44 by "java_re" with MS VC++ 10.0 (VS2010)
    Memory: 4k page, physical 8236488k(944408k free), swap 14421096k(2656012k free)
    CommandLine flags: -XX:InitialHeapSize=31457280 -XX:MaxHeapSize=31457280 -XX:MaxNewSize=10485760 -XX:NewSize=10485760 -XX:+PrintGC -XX:+PrintGCTimeStamps -XX:+PrintTenuringDistribution -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseSerialGC 
    1.119: [GC (Allocation Failure) 
    Desired survivor size 524288 bytes, new threshold 1 (max 15)
    - age   1:     961392 bytes,     961392 total
     6101K->4010K(29696K), 0.0051928 secs]
    3.127: [GC (Allocation Failure) 
    Desired survivor size 524288 bytes, new threshold 15 (max 15)
     10298K->10150K(29696K), 0.0032352 secs]
    5.131: [GC (Allocation Failure) 
    Desired survivor size 524288 bytes, new threshold 15 (max 15)
     16416K->16294K(29696K), 0.0107512 secs]
    7.145: [Full GC (Allocation Failure)  22573K->933K(29696K), 0.0041995 secs]
  ```

