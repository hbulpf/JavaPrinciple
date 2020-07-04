# 一文看懂JVM的GC

我们知道 JVM 调优主要调的是**垃圾收集器的选择**和**参数的设置**，所以我们对垃圾回收的知识必须要掌握了解，不然怎么调优呢，那么什么是垃圾呢，我们类比生活中的垃圾，就是不要的东西，需要清除的东西，那么第一步就是要找到垃圾，在Java中我们有两种方式。 

## 怎么定义垃圾

### 引用计数法

给对象中添加一个引用计数器，每当有一个地方引用他时，计数器值就+1。当引用失效时，计数器值就-1。任何时刻计数器为 0 的对象就是不可能在被使用，判断为不可达，等待 gc 清理。这个方法几乎报废，因为如果AB相互持有引用，导致永远不能被回收，大家看下面这段代码。。

```
/**
 * @author jack xu
 */
public class ReferenceCount {

    public Object instance = null;

    public static void main(String[] args) {
        testGC();
    }

    public static void testGC() {
        ReferenceCount a = new ReferenceCount();
        ReferenceCount b = new ReferenceCount();

        a.instance = b;
        b.instance = a;

        a = null;
        b = null;
    }
}
复制代码
```

最后在看下图就明白了，最后这2个对象已经不可能再被访问了，但由于他们相互引用着对方，导致它们的引用计数永远都不会为0，通过引用计数算法，也就永远无法通知 GC 收集器回收它们。**这就导致了内存泄露，最后会导致内存溢出。** 

![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1080" height="311"></svg>)



### 可达性分析

可达性分析算法的基本思路是，通过一些被称为引用链（GC Roots）的对象作为起点，从这些节点开始向下搜索，搜索走过的路径被称为（Reference Chain)，当一个对象到 GC Roots 没有任何引用链相连时（即从 GC Roots 节点到该节点不可达），则证明该对象是不可用的，如下图所示。。



![img](https://user-gold-cdn.xitu.io/2020/5/4/171dfd1017878956?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

 在Java中，可作为GC Roots对象的列表：



- Java虚拟机栈（栈帧中的本地变量表）中引用的对象
- 本地方法栈中JNI（既一般说的Native方法）引用的对象。
- 方法区中类静态属性引用的对象
- 方法区中常量的引用对象。
- 类加载器
- 线程 Thread 类

## 垃圾回收算法

在确定了哪些垃圾可以被回收后，垃圾收集器要做的事情就是开始进行垃圾回收，但是这里面涉及到一个问题是：**如何高效地进行垃圾回收？** 这里一共有三种算法。

### 标记-清除(Mark-Sweep)

这是最基础的垃圾回收算法，标记-清除算法分为两个阶段：标记阶段和清除阶段。标记阶段是标记出所有需要被回收的对象，清除阶段就是回收被标记的对象所占用的空间。 

![img](https://user-gold-cdn.xitu.io/2020/5/4/171decda8ebe44f7?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

 缺点：



- 位置不连续，产生碎片
- 效率偏低，两遍扫描，标记和清除都比较耗时

### 复制算法 (Copying)

为了解决 Mark-Sweep 算法的缺陷，Copying 算法就被提了出来。它将可用内存按容量划分为大小相等的两块，每次只使用其中的一块。当这一块的内存用完了，就将还存活着的对象复制到另外一块上面，然后再把已使用的内存空间一次清理掉，这样一来就不容易出现内存碎片的问题。



![img](https://user-gold-cdn.xitu.io/2020/5/4/171dec3d9757ed86?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

 优点：没有碎片，空间连续



缺点：**导致50%的内存空间始终空闲浪费！**

### 标记-整理(Mark-Compact)

为了解决 Copying 算法的缺陷，充分利用内存空间，提出了 Mark-Compact 算法。该算法标记阶段和 Mark-Sweep 一样，但是在完成标记之后，它不是直接清理可回收对象，而是将存活对象都向一端移动，然后清理掉端边界以外的内存。



![img](https://user-gold-cdn.xitu.io/2020/5/4/171dec9a5291a33d?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

 优点：没有碎片，空间连续



缺点：效率偏低，两遍扫描，指针需要调整

## 分代收集算法

分代收集算法（Generational Collection）严格来说并不是一种思想或理论，而是融合上述3种基础的算法思想，而产生的针对不同情况所采用不同算法的一套组合拳。

我们知道大多数对象都是朝生夕死的，所以我们把堆分为了新生代、老年代，以及永生代（JDK8 里面叫做元空间），方便他们按照不同的代进行不同的垃圾回收。新生代又被进一步划分为 Eden（伊甸园）和 Survivor（幸存者）区，他们的比例是8：1：1。 

![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="792" height="399"></svg>)

**下面我用图解来演示一下分代垃圾收集过程：**



第一步：新分配的对象会放在伊甸园，伊甸园满了就会触发 minor gc，minor gc 会清除包括 s0 , s1 在内所有年轻代里面不用的垃圾。 

![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1023" height="612"></svg>)



第二步：伊甸园里面没有被清除的对象就是幸存下来的，将他们年龄+1，放到 s0 区



![img](https://user-gold-cdn.xitu.io/2020/5/4/171dfa8c448f9f28?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



第三步：伊甸园里面满了以后再次触发 minor gc，伊甸园幸存的对象年龄+1放到s1区，s0区幸存的对象年龄+1放到 s1 区，这样 s0 区就空出来了 

![img](https://user-gold-cdn.xitu.io/2020/5/4/171dfac28a075f0e?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

 第四步：如此反复 

![img](https://user-gold-cdn.xitu.io/2020/5/4/171dfaf08e1f56d0?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



第五步：幸存者区达到年龄后，进入到老年代，默认是15岁（CMS里默认是6岁），这个年龄是可以自己调的 

![img](https://user-gold-cdn.xitu.io/2020/5/4/171dfb01689dc058?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

 第六步：如果老年代内存满了，就会触发 major GC 或者 full GC。触发 full GC 就会出现所谓的 STW（stop the world）现象。内存越大，STW 的时间也越长，所以内存也不仅仅是越大越好。



**看了上面的图解后我们知道，年轻代用的是复制算法，因为对象大多数生命周期短，回收非常频繁，用复制算法效率高；而老年代用的是标记清除或标记整理算法，因为在老年代对象存活时间比较长，复制来复制去没必要。**

## 垃圾收集器

垃圾收集器是垃圾回收算法的具体实现，说白了就是落地，我们介绍下面几个常用的收集器。



![img](https://user-gold-cdn.xitu.io/2020/5/4/171deda376a5e18d?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



### Serial/Serial Old

Serial/Serial Old 收集器是最基本最古老的收集器，它是一个单线程收集器，并且在它进行垃圾收集时，必须暂停所有用户线程，会 Stop The World。Serial 收集器是针对新生代的收集器，采用的是 **Copying** 算法。Serial Old 收集器是针对老年代的收集器，采用的是 Mark-Compact 算法。它的优点是实现简单高效，但是缺点是会给用户带来**停顿**。   

![img](https://user-gold-cdn.xitu.io/2020/5/4/171deeff79645936?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

 参数控制：-XX:+UseSerialGC  -XX：+UseSerialOldGC



### ParNew

ParNew 收集器其实就是 Serial 收集器的多线程版本，多线程并行进行垃圾收集。在多核 CPU 时，比 Serial 效率高，在单核 CPU 时和 Serial 是差不多的。作用在新生代，使用**复制**算法，配合CMS使用。   

![img](https://user-gold-cdn.xitu.io/2020/5/4/171def4817d4ef97?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

 参数控制：-XX:+UseParNewGC



### Parallel Scavenge/Parallel Old

Parallel Scavenge 收集器是一个新生代的多线程收集器。他也是**并行收集**，看上去和 ParNew 一样，但是 Parallel Scanvenge 更关注系统的**吞吐量**，其采用的是 **Copying** 算法。

Parallel Old 是Parallel Scavenge 收集器的老年代版本，也是并行收集器，使用多线程和 Mark-Compact 算法，也是更加关注吞吐量。

参数控制：-XX：+UseParallelGC   -XX：+UseParallelOldGC

### CMS

CMS（Concurrent Mark Sweep）收集器是一种以获取最短回收**停顿时间**为目标的收集器，它是一种**并发收集器**，采用的是 **Mark-Sweep 算法**。

> 小伙伴注意了：并行指的是垃圾回收线程之间并行执行，并发指的是用户线程和垃圾回收线程一起执行。

它的运作过程相对于前面几种收集器来说要更复杂一些，整个过程分为4个步骤，包括：

- 初始标记（CMS initial mark）
- 并发标记（CMS concurrent mark）
- 重新标记（CMS remark）
- 并发清除（CMS concurrent sweep）

**其中初始标记、重新标记这两个步骤仍然需要“Stop The World”**。初始标记仅仅只是标记一下 GC Roots 能直接关联到的对象，速度很快，并发标记阶段就是进行 GC Roots Tracing 的过程，而重新标记阶段则是为了修正并发标记期间，因用户程序继续运作而导致标记产生变动的那一部分对象的标记记录，这个阶段的停顿时间一般会比初始标记阶段稍长一些，但远比并发标记的时间短。

> 由于整个过程中耗时最长的并发标记和并发清除过程中，收集器线程都可以与用户线程一起工作，所以总体上来说，CMS收集器的内存回收过程是与用户线程一起并发地执行。



![img](https://user-gold-cdn.xitu.io/2020/5/4/171df094268411c7?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



参数控制：-XX:+UseConcMarkSweepGC

### G1

G1 收集器是当今收集器技术发展最前沿的成果，它是一款面向服务端应用的收集器，它能充分利用多 CPU、多核环境。因此它是一款并行与并发收集器，并且它可以**设置停顿时间**。与 CMS 收集器相比G1收集器有以下特点：

1、**空间整合**，G1 收集器采用**标记-整理**算法，不会产生内存空间碎片。分配大对象时不会因为无法找到连续空间而提前触发下一次 GC。

2、**可预测停顿**，这是 G1 的另一大优势，降低停顿时间是 G1 和 CMS 的共同关注点，但 G1 除了追求低停顿外，还能建立可预测的停顿时间模型，能让使用者明确指定在一个长度为N毫秒的时间片段内，消耗在垃圾收集上的时间不得超过N毫秒，这几乎已经是实时 Java（RTSJ）的垃圾收集器的特征了。

> 上面提到的垃圾收集器，收集的范围都是整个新生代或者老年代，而 G1 不再是这样。使用G1收集器时，Java 堆的内存布局与其他收集器有很大差别，它将整个 Java 堆划分为多个大小相等的独立区域（Region），虽然还保留有新生代和老年代的概念，但新生代和老年代不再是物理隔阂了，它们都是一部分（可以不连续）Region 的集合。

工作过程如下几步:

- 初始标记（Initial Marking）：标记一下GC Roots能够关联的对象，并且修改TAMS的值，需要暂停用户线程

- 并发标记（Concurrent Marking）：从GC Roots进行可达性分析，找出存活的对象，与用户线程并发执行

- 最终标记（Final Marking）：修正在并发标记阶段因为用户程序的并发执行导致变动的数据，需暂停用户线程

- 筛选回收（Live Data Counting and Evacuation）：对各个 Region 的回收价值和成本进行排序，根据用户所期望的GC停顿时间制定回收计划 

  ![img](https://user-gold-cdn.xitu.io/2020/5/4/171df2ecb553bc06?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

   参数控制：-XX：+UseG1GC

## 垃圾收集器的选择

垃圾收集器的选择主要看两个关键指标，**停顿时间**和**吞吐量**。

- 停顿时间：垃圾收集器进行垃圾回收终端应用执行响应的时间
- 吞吐量：运行用户代码时间/(运行用户代码时间+垃圾收集时间）

> 停顿时间越短就越适合需要和用户交互的程序，良好的响应速度能提升用户体验；高吞吐量则可以高效地利用CPU时间，尽快完成程序的运算任务，主要适合在后台运算而不需要太多交互的任务。

我们把上面介绍的垃圾收集器再分下类

- 并发收集器[停顿时间优先]：CMS、G1   ---->适用于相对时间有要求的场景，比如Web
- 并行收集器[吞吐量优先]：Parallel Scanvent 和 Parallel Old  ----> 适用于科学计算、后台处理等若交互场景
- 串行收集器：Serial 和 Serial Old ---->适合内存比较小，嵌入式的设备

最后在说几句，JamesGosling 在1995年设计 Java 的时候并没有意识到这个语言将来会有更多的 Web 开发，停顿时间要比较小的场景，所有一开始是串行化，需要 Stop The World，这个放到现在来说是不敢想象的，试想你上着淘宝正嗨的时候，突然网页打不开了，这你能忍？后来慢慢有了 Java8 默认的 PS+Parallel Old，这个是吞吐量优先的，后来 Java8、Java9 对时间有了更高的要求，就有了 CMS、G1 以及本文没有介绍的 ZGC，所以随着时代的进步垃圾收集器也在不断的改造升级。

最后看下 [Oracle 官网](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/collectors.html#sthref28) 告诉我们如何选择一个垃圾收集器，因为网上都是博客翻译来翻译去，不是很正确，我们看官网的会更加权威。

![img](https://user-gold-cdn.xitu.io/2020/5/4/171df743878a8f19?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



# 参考

1. https://juejin.im/post/5ef94baff265da230b52e477