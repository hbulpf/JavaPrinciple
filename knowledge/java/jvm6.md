# JVM系列(6):JVM监测工具

## 需要监测的数据

 需要监测的数据：(内存使用情况 谁使用了内存 GC的状况)

### 内存使用情况--heap&PermGen

```
@ 表示通过jmap –heap *pid* 可以获取的值
# 表示通过jstat –gcutil *pid* 可以获取的值
```

参数的查看可以通过多种方法 本文中只随机列出一种。

| 描述                            | 最大值                                                       | 当前值                                                 | 报警值 |
| ------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------ | ------ |
| 堆内存                          | @Heap Configuration::MaxHeapSize *sum(eden+servivor+old)*    | *sum(eden+servivor+old)*                               | 自设   |
| 非堆内存                        | sum(perm+native)                                             |                                                        | 无     |
| Eden                            | @Eden Space::capacity                                        | @Eden Space::used                                      | 无     |
| Survivor0                       | @From Space::capacity                                        | @From Space::used                                      | 无     |
| Survivor1                       | @To Space::capacity                                          | @To Space::used                                        | 无     |
| New gen (注意区别于Xmn参数设置) | @New Generation::capacity *Eden + 1 Survivor Space*          | @New Generation::used                                  | 无     |
| Old gen                         | @concurrent mark-sweep generation::capacity (CMS是对old区的gc,所以此处即表示old gen) | @concurrent mark-sweep generation::capacity(CMS)::used | 自设   |
| Perm Gen                        | @Perm Generation::capacity                                   | @Perm Generation::used                                 | 自设   |

### 内存使用情况--config

### 内存使用情况—C heap

- top or ps aux

### 谁使用了内存

- Heap
  jmap –histo
  jmap –dump ,then mat
- C heap
  google perftools

### GC的状况

| 描述     | 收集次数 | 收集时间 | 应用暂停时间                                                 |
| -------- | -------- | -------- | ------------------------------------------------------------ |
| Full GC  | #FGC     | #FGCT    | [设置-XX:+PrintGCApplicationStoppedTime](http://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html)后在日志中查看 |
| Young GC | #YGC     | #YGCT    | 同上                                                         |

-XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC [-XX:+PrintGCApplicationStoppedTime](http://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html) -Xloggc:logs/gc.log

 

## 常用[工具](http://stl-www.htw-saarland.de/syst-lab/java/jdk-1_5_0/docs/tooldocs/share/)介绍:jinfo jmap jstack jstat

### [jinfo](http://stl-www.htw-saarland.de/syst-lab/java/jdk-1_5_0/docs/tooldocs/share/jinfo.html) 

- 可以从一个给定的java进程或core文件或远程debug服务器上获取java配置信息。包括java系统属性及JVM参数(command line flags)。注意在jvm启动参数中没有配置的参数也可使用jinfo –flag xxx pid输出默认值(很有用,但貌似一些简写的参数查不出来)。
- 可以修改运行时的java 进程的opts。
- 只有solaris和linux的JDK版本里有。
- 使用方式可使用jinfo –h 查询。

### [jmap](http://stl-www.htw-saarland.de/syst-lab/java/jdk-1_5_0/docs/tooldocs/share/jmap.html)

​    观察运行中的jvm物理内存的占用情况。

​    如果连用SHELL jmap -histo pid>a.log可以将其保存到文本中去，在一段时间后，使用文本对比工具，可以对比出GC回收了哪些对象。

​    参数很简单，直接查看jmap -h

​    举例：

```
jmap -heap pidjmap -dump:format=b,file=heap.hprof <pid>
```

 

dump文件可以通过MemoryAnalyzer分析查看.网址：http://www.eclipse.org/mat/，可以查看dump时对象数量，内存占用，线程情况等。

[jmap -dump:live为啥会触发Full GC](http://qa.taobao.com/?p=10010)

### [jstack](http://stl-www.htw-saarland.de/syst-lab/java/jdk-1_5_0/docs/tooldocs/share/jstack.html)

​    观察jvm中当前所有线程的运行情况和线程当前状态

​    如果java程序崩溃生成core文件，jstack工具可以用来获得core文件的java stack和native stack的信息，从而可以轻松地知道java程序是如何崩溃和在程序何处发生问题。另外，jstack工具还可以附属到正在运行的java程序中，看到当时运行的java程序的java stack和native stack的信息, 如果现在运行的java程序呈现hung的状态，jstack是非常有用的。目前只有在Solaris和Linux的JDK版本里面才有。

​    参数很简单，直接查看jstack -h

​    举例：

```
jstack pid
```

### [jstat](http://stl-www.htw-saarland.de/syst-lab/java/jdk-1_5_0/docs/tooldocs/share/jstat.html)

​    JVM监测工具(Java Virtual Machine Statistics Monitoring Tool)。利用了JVM内建的指令对Java应用程序的资源和性能进行实时的命令行的监控，包括各种堆和非堆的大小及其内存使用量、classloader、compiler、垃圾回收状况等。

举例:

```
jstat –printcompilation -h10 3024 250 600
```

   每250毫秒打印一次，一共打印600次 每隔10行显示一次head

语法结构:

```
Usage: jstat -help|-options       jstat -<option> [-t] [-h<lines>] <vmid> [<interval> [<count>]]
```

参数介绍：

- -h n 每隔几行输出标题
- vmid VM的进程号，即当前运行的java进程号
- -t 在第一列显示自JVM启动以来的时间戳
- -J 修改java进程的参数。类似jinfo -flag <name>=<value>。例如-J-Xms48m 设置初始堆为48M。[详见这里](http://stl-www.htw-saarland.de/syst-lab/java/jdk-1_5_0/docs/tooldocs/linux/java.html)。这个参数挺有用的，可以在运行中调整参数以方便测试、监测。
- -option option为要检测的参数。参数列表可通过jstat –options 获取。下面将分别介绍每个参数及输出字段的含义。

| [class](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#class_option) | 统计class loader行为信息                |
| ------------------------------------------------------------ | --------------------------------------- |
| [compiler](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#compiler_option) | 统计编译行为信息                        |
| [gc](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#gc_option) | 统计jdk gc时heap信息                    |
| [gccapacity](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#gccapacity_option) | 统计堆内存不同代的heap容量信息          |
| [gccause](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#gccause_option) | 统计gc的情况（同-gcutil）和引起gc的事件 |
| [gcnew](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#gcnew_option) | 统计gc时新生代的信息(相比gcutil更详细)  |
| [gcnewcapacity](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#gcnewcapacity_option) | 统计gc时新生代heap容量                  |
| [gcold](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#gcold_option) | 统计gc时，老年区的情况                  |
| [gcoldcapacity](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#gcoldcapacity_option) | 统计gc时，老年区heap容量                |
| [gcpermcapacity](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#gcpermcapacity_option) | 统计gc时，permanent区heap容量           |
| [gcutil](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#gcutil_option) | 统计gc时，heap情况                      |
| [printcompilation](https://www.cnblogs.com/redcreen/archive/2011/05/09/2040977.html#printcompilation_option) | 统计编译行为信息                        |

-class option:Class Loader Statistics

| Column | Description                                         |
| ---------- | ------------------------------------------------------- |
| Loaded     | Number of classes loaded.                               |
| Bytes      | Number of Kbytes loaded.                                |
| Unloaded   | Number of classes unloaded.                             |
| Bytes      | Number of Kbytes unloaded.                              |
| Time       | Time spent performing class load and unload operations. |

-compiler:HotSpot Just-In-Time Compiler Statistics

| Column   | Description                                        |
| ------------ | ------------------------------------------------------ |
| Compiled     | Number of compilation tasks performed.                 |
| Failed       | Number of compilation tasks that failed.               |
| Invalid      | Number of compilation tasks that were invalidated.     |
| Time         | Time spent performing compilation tasks.               |
| FailedType   | Compile type of the last failed compilation.           |
| FailedMethod | Class name and method for the last failed compilation. |

-gc Option:Garbage-collected heap statistics

| Column | Description                           |
| ---------- | ----------------------------------------- |
| S0C        | Current survivor space 0 capacity (KB).   |
| S1C        | Current survivor space 1 capacity (KB).   |
| S0U        | Survivor space 0 utilization (KB).        |
| S1U        | Survivor space 1 utilization (KB).        |
| EC         | Current eden space capacity (KB).         |
| EU         | Eden space utilization (KB).              |
| OC         | Current old space capacity (KB).          |
| OU         | Old space utilization (KB).               |
| PC         | Current permanent space capacity (KB).    |
| PU         | Permanent space utilization (KB).         |
| YGC        | Number of young generation GC Events.     |
| YGCT       | Young generation garbage collection time. |
| FGC        | Number of full GC events.                 |
| FGCT       | Full garbage collection time.             |
| GCT        | Total garbage collection time.            |

-gccapacity Option:Memory Pool Generation and Space Capacities

| Column | Description                             |
| ---------- | ------------------------------------------- |
| NGCMN      | Minimum new generation capacity (KB).       |
| NGCMX      | Maximum new generation capacity (KB).       |
| NGC        | Current new generation capacity (KB).       |
| S0C        | Current survivor space 0 capacity (KB).     |
| S1C        | Current survivor space 1 capacity (KB).     |
| EC         | Current eden space capacity (KB).           |
| OGCMN      | Minimum old generation capacity (KB).       |
| OGCMX      | Maximum old generation capacity (KB).       |
| OGC        | Current old generation capacity (KB).       |
| OC         | Current old space capacity (KB).            |
| PGCMN      | Minimum permanent generation capacity (KB). |
| PGCMX      | Maximum Permanent generation capacity (KB). |
| PGC        | Current Permanent generation capacity (KB). |
| PC         | Current Permanent space capacity (KB).      |
| YGC        | Number of Young generation GC Events.       |
| FGC        | Number of Full GC Events.                   |

-gccause Option:Garbage Collection Statistics, Including GC Events

| Column | Description                      |
| ---------- | ------------------------------------ |
| LGCC       | Cause of last Garbage Collection.    |
| GCC        | Cause of current Garbage Collection. |

​    前面的字段与gcutil相同.

-gcnew Option:New Generation Statistics

| Column | Description                           |
| ---------- | ----------------------------------------- |
| S0C        | Current survivor space 0 capacity (KB).   |
| S1C        | Current survivor space 1 capacity (KB).   |
| S0U        | Survivor space 0 utilization (KB).        |
| S1U        | Survivor space 1 utilization (KB).        |
| TT         | Tenuring threshold.                       |
| MTT        | Maximum tenuring threshold.               |
| DSS        | Desired survivor size (KB).               |
| EC         | Current eden space capacity (KB).         |
| EU         | Eden space utilization (KB).              |
| YGC        | Number of young generation GC events.     |
| YGCT       | Young generation garbage collection time. |

-gcnewcapacity Option:New Generation Space Size Statistics

| Column | Description                         |
| ---------- | --------------------------------------- |
| NGCMN      | Minimum new generation capacity (KB).   |
| NGCMX      | Maximum new generation capacity (KB).   |
| NGC        | Current new generation capacity (KB).   |
| S0CMX      | Maximum survivor space 0 capacity (KB). |
| S0C        | Current survivor space 0 capacity (KB). |
| S1CMX      | Maximum survivor space 1 capacity (KB). |
| S1C        | Current survivor space 1 capacity (KB). |
| ECMX       | Maximum eden space capacity (KB).       |
| EC         | Current eden space capacity (KB).       |
| YGC        | Number of young generation GC events.   |
| FGC        | Number of Full GC Events.               |

-gcold Option:Old and Permanent Generation Statistics

| Column | Description                        |
| ---------- | -------------------------------------- |
| PC         | Current permanent space capacity (KB). |
| PU         | Permanent space utilization (KB).      |
| OC         | Current old space capacity (KB).       |
| OU         | old space utilization (KB).            |
| YGC        | Number of young generation GC events.  |
| FGC        | Number of full GC events.              |
| FGCT       | Full garbage collection time.          |
| GCT        | Total garbage collection time.         |

-gcoldcapacity Option:Old Generation Statistics

| Column | Description                       |
| ---------- | ------------------------------------- |
| OGCMN      | Minimum old generation capacity (KB). |
| OGCMX      | Maximum old generation capacity (KB). |
| OGC        | Current old generation capacity (KB). |
| OC         | Current old space capacity (KB).      |
| YGC        | Number of young generation GC events. |
| FGC        | Number of full GC events.             |
| FGCT       | Full garbage collection time.         |
| GCT        | Total garbage collection time.        |

-gcpermcapacity Option: Permanent Generation Statistics

| Column | Description                             |
| ---------- | ------------------------------------------- |
| PGCMN      | Minimum permanent generation capacity (KB). |
| PGCMX      | Maximum permanent generation capacity (KB). |
| PGC        | Current permanent generation capacity (KB). |
| PC         | Current permanent space capacity (KB).      |
| YGC        | Number of young generation GC events.       |
| FGC        | Number of full GC events.                   |
| FGCT       | Full garbage collection time.               |
| GCT        | Total garbage collection time.              |

-gcutil Option:Summary of Garbage Collection Statistics

| Column | Description                                              |
| ---------- | ------------------------------------------------------------ |
| S0         | Survivor space 0 utilization as a percentage of the space's current capacity. |
| S1         | Survivor space 1 utilization as a percentage of the space's current capacity. |
| E          | Eden space utilization as a percentage of the space's current capacity. |
| O          | Old space utilization as a percentage of the space's current capacity. |
| P          | Permanent space utilization as a percentage of the space's current capacity. |
| YGC        | Number of young generation GC events.                        |
| YGCT       | Young generation garbage collection time.                    |
| FGC        | Number of full GC events.                                    |
| FGCT       | Full garbage collection time.                                |
| GCT        | Total garbage collection time.                               |

-printcompilation Option: HotSpot Compiler Method Statistics

| Column | Description                                              |
| ---------- | ------------------------------------------------------------ |
| Compiled   | Number of compilation tasks performed.                       |
| Size       | Number of bytes of bytecode for the method.                  |
| Type       | Compilation type.                                            |
| Method     | Class name and method name identifying the compiled method. Class name uses "/" instead of "." as namespace separator. Method name is the method within the given class. The format for these two fields is consistent with the HotSpot - XX:+PrintComplation option. |

 

## Java api方式监测

​    jre中提供了一些查看运行中的[jvm](http://www.huomo.cn/t/jvm/)内部信息的api，这些api包含在java.lang.management包中，此包中的接口是在jdk 5中引入的，所以只有在jdk 5及其以上版本中才能通过这种方式访问这些信息。下面简单介绍一下这包括哪些信息，以及如何访问。

​    可以通过此api访问到运行中的jvm的类加载的信息、jit编译器的信息、内存分配的情况、线程的相关信息以及运行jvm的操作系统的信息。java.lang.management包中提供了9个接口来访问这些信息，使用ManagementFactory的静态get方法可以获得相应接口的实例，可以通过这些实例来获取你需要的相关信息。

​    更详细的关于MBean的介绍参见[Java SE 6 新特性: JMX 与系统管理](http://www.ibm.com/developerworks/cn/java/j-lo-jse63/index.html)

​    demo1:查看一下当前运行的jvm中加载了多少个类。想详细了解如何使用这些api，可以参考java.lang.management包中的详细api文档。

```
public class ClassLoaderChecker {    public static void main( String[] args ) throws Exception {      ClassLoadingMXBean bean = ManagementFactory.getClassLoadingMXBean();      System.out.println( bean.getLoadedClassCount() );    }}
```

demo2:自定义Mbean Type,记录的数据可通过jconsole等工具或自写代码查看,

```
//工具方法public static ObjectName register(String name, Object mbean) {        try {            ObjectName objectName = new ObjectName(name);            MBeanServer mbeanServer = ManagementFactory                    .getPlatformMBeanServer();            try {                mbeanServer.registerMBean(mbean, objectName);            } catch (InstanceAlreadyExistsException ex) {                mbeanServer.unregisterMBean(objectName);                mbeanServer.registerMBean(mbean, objectName);            }            return objectName;        } catch (JMException e) {            throw new IllegalArgumentException(name, e);        }}//步骤一:定义Mbean接口://随便定义public interface DemoMBean {      public AtomicLong getInvokeCount();}//步骤二:实现接口,并注册:public class DemoImpl implements DemoMBean{      public final static String DEFAULT_OBJECT_NAME_PREFIX = "com.redcreen.demo:type=demo";      register("com.redcreen.demo:type=demo",DemoImpl.instance);}//可以通过jconsole中查看数据了
```

 

## 参考

1. [在 Java SE 6 中监视和诊断性能问题](http://www.ibm.com/developerworks/cn/java/j-java6perfmon/index.html)

2. http://www.51testing.com/?uid-183198-action-viewspace-itemid-185174

3. [JVM监控工具介绍jstack, jconsole, jinfo, jmap, jdb, jstat](http://blog.csdn.net/kelly859/archive/2010/08/20/5827363.aspx)

4. http://stl-www.htw-saarland.de/syst-lab/java/jdk-1_5_0/docs/tooldocs/share/jinfo.html

5. http://qa.taobao.com/?p=10010

6. [运用Jconsole监控JVM](http://blog.csdn.net/lengyuhong/archive/2011/02/22/6200355.aspx)

7. http://www.coderanch.com/t/329407/java/java/find-all-loaded-classes-classloaders