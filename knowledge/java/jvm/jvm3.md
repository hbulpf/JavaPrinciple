# JVM系列:(3)GC策略&内存申请与对象衰老

> 本文主要讲解JVM中垃圾回收（GC）机制，包括YGC和FGC概念、触发条件、触发过程。

## GC机制

JVM里的GC(Garbage Collection)的算法有很多种，如标记清除收集算法，压缩收集算法，分代收集算法等。

现在比较常用的是分代收集（generational collection,也是SUN JVM使用的,J2SE1.2之后引入），即将内存分为几个区域，
将不同生命周期的对象放在不同区域里:**young generation**，**tenured generation**和**permanet generation**。

绝大部分的object被分配在young generation(生命周期短)，并且大部分的object在这里消亡。
当young generation满了之后，将引发 **minor collection(YGC)**。
在minor collection后存活的object会被移动到**tenured generation(**生命周期比较长**)**。
最后，**tenured generation**满之后触发**major collection**。**major collection（Full GC）**会触发整个heap的回收，包括回收 young generation。
permanet generation区域（也称为非堆区域）比较稳定，主要存放classloader信息。

young generation 由eden、2个survivor 区域组成。其中一个survivor区域一直是空的，是eden区域和另一个survivor区域在下一次copy collection后活着的object的目的地。object在survivo区域被复制,直到转移到tenured区。

![](../imgs/jvm3.jpg)

我们要尽量减少 Full gc 的次数。*tenured generation* 一般比较大,收集的时间较长, 频繁的FGC(Full gc)会导致应用的性能受到严重的影响。

### 堆内存GC

JVM(采用分代回收的策略)，用较高的频率对年轻的对象(young generation)进行YGC，而对老对象(*tenured* generation)满了后才进行FGC。这样就不需要每次GC都将内存中所有对象都检查一遍。

### 非堆内存不GC

GC不会在主程序运行期对PermGen Space进行清理，所以如果应用中有很多CLASS(特别是动态生成类，PermGen Space存放的内容不仅限于类),就很可能出现PermGen Space错误。

## 内存申请、对象衰老过程

**一、内存申请过程**

1. JVM会试图为相关Java对象在Eden中初始化一块内存区域；
2. 当Eden空间足够时，内存申请结束。否则到下一步；
3. JVM试图释放在Eden中所有不活跃的对象（YGC），释放后若Eden空间仍然不足以放入新对象，则试图将部分Eden中活跃对象放入Survivor区；
4. Survivor区被用来作为Eden及tenured的中间交换区域，当tenured区空间足够时，Survivor区的对象会被移到tenured区，否则会被保留在Survivor区；
5. 当tenured区空间不够时，JVM会在tenured区进行major collection（FGC）；
6. 完全垃圾收集后，若Survivor及tenured区仍然无法存放从Eden复制过来的部分对象，导致JVM无法为新对象创建内存区域，则出现"Out of memory错误"；

**二、对象衰老过程**

1. 新创建的对象的内存都分配自eden。YGC的过程就是将eden和在survivor space中的活对象copy到空闲survivor space中。

2. 对象在young generation里经历了一定次数(可以通过参数配置)的YGC后，就会被移到tenured generation中，称为tenuring。

3. GC触发条件

| **GC类型** | **触发条件**                                                 | **触发时发生了什么**                                         | **注意**                                                     | **查看方式**         |
| ---------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | -------------------- |
| YGC        | eden空间不足                                                 | 1.清空Eden+from survivor中所有未引用的对象占用的内存;将eden+from survivor(S0)中所有存活的对象copy到to survivor(S1)中;<br/> 2.一些对象将晋升到tenured中:to survivor放不下的,存活次数超过turning threshold中的;<br/> 3.重新计算tenuring threshold(serial parallel GC会触发此项)重新调整Eden 和from的大小(parallel GC会触发此项) | 全过程暂停应用,是否为多线程处理由具体的GC决定                | `jstat –gcutil gc log` |
| FGC        | 1.tenured空间不足;<br/> 2.PermGen空间不足<br/>  3.显示调用System.GC<br/> 4.RMI等定时触发<br/>  5.YGC时的悲观策略<br/> 6.dump live的内存信息时(jmap –dump:live) | 1.清空堆中未引用的对象<br/> 2.清空PermGen中已经被卸载的classloader中加载的class信息<br/>  3.如配置了CollectGenOFirst,则先触发YGC(针对serial GC),如配置了ScavengeBeforeFullGC,则先触发YGC(针对serial GC) | 全过程暂停应用,是否为多线程处理由具体的GC决定  是否压缩需要看配置的具体GC | `jstat –gcutil gc log`|

注意： permanent generation空间不足会引发Full GC,仍然不够会引发PermGen Space错误。

对象进程查看方式为
```
jstat –gc 进程ID 间隔时间
```

# 参考

1. java程序启动参数设置优化_分代收集算法. https://blog.csdn.net/qq_27384769/article/details/79477789