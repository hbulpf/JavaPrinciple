# JVM工具

## JDK内置工具使用

- **jps**(Java Virtual Machine Process Status Tool)
    查看所有的jvm进程，包括进程ID，进程启动的路径等等。
- **jstack**(Java Stack Trace)
    ① 观察jvm中当前所有线程的运行情况和线程当前状态。
    ② 系统崩溃了？如果java程序崩溃生成core文件，jstack工具可以用来获得core文件的java stack和native stack的信息，从而可以轻松地知道java程序是如何崩溃和在程序何处发生问题。
    ③ 系统hung住了？jstack工具还可以附属到正在运行的java程序中，看到当时运行的java程序的java stack和native stack的信息, 如果现在运行的java程序呈现hung的状态，jstack是非常有用的。
- **jstat**(Java Virtual Machine Statistics Monitoring Tool)
    ① jstat利用JVM内建的指令对Java应用程序的资源和性能进行实时的命令行的监控，包括了对进程的classloader，compiler，gc情况；
    ②监视VM内存内的各种堆和非堆的大小及其内存使用量，以及加载类的数量。
- **jmap**(Java Memory Map)
    监视进程运行中的jvm物理内存的占用情况，该进程内存内，所有对象的情况，例如产生了哪些对象，对象数量；
- **jinfo**(Java Configuration Info)
    观察进程运行环境参数，包括Java System属性和JVM命令行参数
- **jconsole**
- **jvisualvm**

#### jstat

  generalOption

  ```
  -help 显示帮助信息。
  -version 显示版本信息
  -options 显示统计选项列表。
  ```

  outputOptions 


  ```
  参数：
      -class：统计类装载器的行为
      -compiler：统计HotSpot Just-in-Time编译器的行为
      -gc：统计堆各个分区的使用情况
      -gccapacity：统计新生区，老年区，permanent区的heap容量情况 
      -gccause：统计最后一次gc和当前gc的原因
      -gcnew：统计gc时，新生代的情况 
      -gcnewcapacity：统计新生代大小和空间
      -gcold：统计老年代和永久代的行为
      -gcoldcapacity：统计老年代大小 
      -gcpermcapacity：统计永久代大小 
      -gcutil：统计gc时，heap情况 
      -printcompilation：HotSpot编译方法统计
  ```


  -class：


  ```
  #每隔1秒监控一次，一共做10次
    jstat -class 17970 1000 10
  ##########################################
  [root@lq225 conf]# jstat -class 2058 1000 10
  Loaded  Bytes  Unloaded  Bytes     Time   
    1697  3349.5        0     0.0       1.79
    1697  3349.5        0     0.0       1.79
    1697  3349.5        0     0.0       1.79
    1697  3349.5        0     0.0       1.79
    ...................................................
  ######################## 术语分隔符 ########################
  #Loaded 类加载数量
  #Bytes  加载的大小（k） 
  #Unloaded 类卸载的数量 
  #Bytes 卸载的大小（k） 
  #Time 时间花费在执行类加载和卸载操作
  ```


  -compiler


  ```
  Compiled Failed Invalid   Time   FailedType FailedMethod
       302      0       0     1.27          0
  .....................................................
  ######################## 术语分隔符 ########################
  #Compiled 编译任务的执行次数
  #Failed   编译任务的失败次数 
  #Invalid  编译任务无效的次数
  #Time     编译任务花费的时间
  #FailedType 最后一次编译错误的类型
  #FailedMethod 最后一次编译错误的类名和方法
  ```


  -gc：


  ```
  #每隔2秒监控一次，共20次
    jstat -gc 2058 2000 20
  ##############################
   S0C    S1C    S0U    S1U      EC       EU        OC         OU       PC     PU    YGC     YGCT    FGC    FGCT     GCT   
  8704.0 8704.0 805.5   0.0   69952.0  64174.5   174784.0    2644.5   16384.0 10426.7      2    0.034   0      0.000    0.034
  8704.0 8704.0 805.5   0.0   69952.0  64174.5   174784.0    2644.5   16384.0 10426.7      2    0.034   0      0.000    0.034
  8704.0 8704.0 805.5   0.0   69952.0  64174.5   174784.0    2644.5   16384.0 10426.7      2    0.034   0      0.000    0.034
  .............................................
  ######################## 术语分隔符 ########################
  #S0C 生还者区0 容量(KB)
  #S1C 生还者区1 容量(KB)
  #S0U 生还者区0 使用量(KB)
  #S1U 生还者区1 使用量(KB)
  #EC 伊甸园区容量(KB)
  #EU 伊甸园区使用量(KB) 
  #OC 老年区容量(KB)
  #OU 老年区使用量(KB)
  #PC 永久区容量(KB) 
  #PU 永久区使用量(KB)
  #YGC 新生代GC次数
  #YGCT 新生代GC时间
  #FGC full GC 事件的次数
  #FGCT full GC的时间 
  #GCT 总GC时间
  ```

  -gccapacity


  ```
  NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC      PGCMN    PGCMX     PGC       PC     YGC    FGC
  131072.0 131072.0 131072.0 13056.0 13056.0 104960.0   393216.0   393216.0   393216.0   393216.0  65536.0  65536.0  65536.0  65536.0      1     0
  ..........................................................................................................
  ######################## 术语分隔符 ########################
  #NGCMN 最小新生代容量(KB)
  #NGCMX 最大新生代容量(KB)
  #NGC 当前新生代容量(KB)
  #S0C 当前生存者0区容量(KB)
  #S1C 当前生存者1区容量(KB)
  #OGCMN 老年代最小容量(KB)
  #OGCMX 老年代最大容量(KB)
  #OGC 当前老年代容量(KB). 
  #OC 当前老年代？Current old space capacity (KB). 
  #PGCMN 永久区最小容量(KB)
  #PGCMX 永久区最大容量(KB)
  #PGC 当前永久区容量(KB). 
  #PC 当前永久区？Current Permanent space capacity (KB). 
  #YGC young GC事件的次数 
  #FGC Full GC次数
  ```


  -gccause


  ```
  S0     S1     E      O      P     YGC     YGCT    FGC    FGCT     GCT    LGCC                 GCC
    0.00  99.84  12.76   0.92  46.23      1    0.016     0    0.000    0.016 unknown GCCause      No GC
  ................................................
  ######################## 术语分隔符 ########################
  #S0 年轻代中第一个survivor（幸存区）已使用的占当前容量百分比
  #S1 年轻代中第二个survivor（幸存区）已使用的占当前容量百分比
  #E 年轻代中Eden（伊甸园）已使用的占当前容量百分比
  #O old代已使用的占当前容量百分比
  #P perm代已使用的占当前容量百分比
  #YGC 从应用程序启动到采样时年轻代中gc次数
  #FGC 从应用程序启动到采样时old代(全gc)gc次数
  #FGCT 从应用程序启动到采样时old代(全gc)gc所用时间(s)
  #GCT 从应用程序启动到采样时gc用的总时间(s)
  #LGCC 最后一次GC的原因
  #GCC 当前GC的原因
  ```


  例如 -gcutil ：    


  ```
  #每隔1秒监控一次，共10次
  jstat -gcutil 2058 1000 10
  ################################
  [root@lq225 conf]# jstat -gcutil 2058 1000 10
    S0     S1     E      O      P     YGC     YGCT    FGC    FGCT     GCT   
    9.25   0.00  96.73   1.51  63.64      2    0.034     0    0.000    0.034
    9.25   0.00  96.73   1.51  63.64      2    0.034     0    0.000    0.034
    9.25   0.00  96.73   1.51  63.64      2    0.034     0    0.000    0.034
    9.25   0.00  96.73   1.51  63.64      2    0.034     0    0.000    0.034
  ```

#### jmap


  ```
  #参数
      -dump:[live,]format=b,file=<filename> 使用hprof二进制形式,输出jvm的heap内容到文件=. live子选项是可选的，假如指定live选项,那么只输出活的对象到文件. 
      -finalizerinfo 打印正等候回收的对象的信息.
      -heap 打印heap的概要信息，GC使用的算法，heap的配置及wise heap的使用情况.
      -histo[:live] 打印每个class的实例数目,内存占用,类全名信息. VM的内部类名字开头会加上前缀”*”. 如果live子参数加上后,只统计活的对象数量. 
      -permstat 打印classload和jvm heap长久层的信息. 包含每个classloader的名字,活泼性,地址,父classloader和加载的class数量. 另外,内部String的数量和占用内存数也会打印出来. 
      -F 强迫.在pid没有相应的时候使用-dump或者-histo参数. 在这个模式下,live子参数无效. 
      -h | -help 打印辅助信息 
      -J 传递参数给jmap启动的jvm. 
      pid 需要被打印配相信息的java进程id.
  ```


  例如 -histo ：   

  ```
  jmap -histo  2058
  ############################
   num     #instances         #bytes  class name
  ----------------------------------------------
     1:           206        3585312  [I
     2:         19621        2791880  <constMethodKlass>
     3:         19621        2520048  <methodKlass>
     4:         21010        2251616  [C
  ............................................................
  ```

  例如 -dump：

  ```
  #生成的文件可以使用jhat工具进行分析，在OOM（内存溢出）时，分析大对象，非常有用
  jmap -dump:live,format=b,file=data.hprof 2058
  
  #通过使用如下参数启动JVM，也可以获取到dump文件：
   -XX:+HeapDumpOnOutOfMemoryError
   -XX:HeapDumpPath=./java_pid<pid>.hprof
  
  #如果在虚拟机中导出的heap信息文件可以拿到WINDOWS上进行分析，可以查找诸如内存方面的问题，可以这么做：
  jhat data.hprof  
  #执行成功后，访问http://localhost:7000即可查看内存信息。（首先把7000端口打开）
  ```

#### jinfo

  

  ```
  #查看java进程的配置信息
  jinfo 2058
  #####################
  Attaching to process ID 2058, please wait...
  Debugger attached successfully.
  Server compiler detected.
  JVM version is 24.0-b56
  Java System Properties:
  
  java.runtime.name = Java(TM) SE Runtime Environment
  project.name = Amoeba-MySQL
  java.vm.version = 24.0-b56
  sun.boot.library.path = /usr/local/java/jdk1.7/jre/lib/amd64
  ................................................
  
  # 查看2058的MaxPerm大小可以用
   jinfo -flag MaxPermSize 2058
  ############################
  -XX:MaxPermSize=100663296
  ```

  

#### jps

  ```
  #列出系统中所有的java进程
    jps
  #######################
  2306 Bootstrap
  3370 Jps 2058 xxxxxxxxx
  ```

  一些术语的中文解释

  ```
  S0C：年轻代中第一个survivor（幸存区）的容量 (字节)
  S1C：年轻代中第二个survivor（幸存区）的容量 (字节)
  S0U：年轻代中第一个survivor（幸存区）目前已使用空间 (字节)
  S1U：年轻代中第二个survivor（幸存区）目前已使用空间 (字节)
  EC：年轻代中Eden（伊甸园）的容量 (字节)
  EU：年轻代中Eden（伊甸园）目前已使用空间 (字节)
  OC：Old代的容量 (字节)
  OU：Old代目前已使用空间 (字节)
  PC：Perm(持久代)的容量 (字节)
  PU：Perm(持久代)目前已使用空间 (字节)
  YGC：从应用程序启动到采样时年轻代中gc次数
  YGCT：从应用程序启动到采样时年轻代中gc所用时间(s)
  FGC：从应用程序启动到采样时old代(全gc)gc次数
  FGCT：从应用程序启动到采样时old代(全gc)gc所用时间(s)
  GCT：从应用程序启动到采样时gc用的总时间(s)
  NGCMN：年轻代(young)中初始化(最小)的大小 (字节)
  NGCMX：年轻代(young)的最大容量 (字节)
  NGC：年轻代(young)中当前的容量 (字节)
  OGCMN：old代中初始化(最小)的大小 (字节) 
  OGCMX：old代的最大容量 (字节)
  OGC：old代当前新生成的容量 (字节)
  PGCMN：perm代中初始化(最小)的大小 (字节) 
  PGCMX：perm代的最大容量 (字节)   
  PGC：perm代当前新生成的容量 (字节)
  S0：年轻代中第一个survivor（幸存区）已使用的占当前容量百分比
  S1：年轻代中第二个survivor（幸存区）已使用的占当前容量百分比
  E：年轻代中Eden（伊甸园）已使用的占当前容量百分比
  O：old代已使用的占当前容量百分比
  P：perm代已使用的占当前容量百分比
  S0CMX：年轻代中第一个survivor（幸存区）的最大容量 (字节)
  S1CMX：年轻代中第二个survivor（幸存区）的最大容量 (字节)
  ECMX：年轻代中Eden（伊甸园）的最大容量 (字节)
  DSS：当前需要survivor（幸存区）的容量 (字节)（Eden区已满）
  TT： 持有次数限制
  MTT ： 最大持有次数限制
  ```

  

#### 使用visualvm监控tomcat

- 修改catalina.sh，添加下面一行：

  ```
  CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote=true -Djava.rmi.server.hostname=192.168.55.255  -Dcom.sun.management.jmxremote.port=8086 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
  
  #注意点：
  #1、用hostname -i 查看是否为127.0.01，如果是，则必须配置-Djava.rmi.server.hostname为本机IP。
  #2、检查防火墙（iptables）是否开启，以及是否开放jmxremote.port所指定的端口。
  ```

  

#### jconsole或jvisualvm

这两个工具也是JDK自带的内容，jvisualvm可能需要在线下载一下，不过两者都是通过jmx来访问JVM然后进行统计的，在启动JVM的时候，要指定jmx的内容。

方式一：指定密码，增加安全性



```ruby
java  -Dcom.sun.management.jmxremote.port=5000  -Dcom.sun.management.jmxremote.password.file=/Users/aihe/Documents/jmxremote.password   -Dcom.sun.management.jmxremote.authenticate=true -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.access.file=/Users/aihe/Documents/jmxremote.access -jar target/jmxdemo-0.0.1-SNAPSHOT.jar
```

![img](https:////upload-images.jianshu.io/upload_images/426671-c68fda6c5b2a9f53.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/886/format/webp)



![img](https:////upload-images.jianshu.io/upload_images/426671-7045f91c4e197c20.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)



![img](https:////upload-images.jianshu.io/upload_images/426671-ab25c0a0c604846d.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

## arthas

阿里开源的一款好用的jvm监控工具，有点像是把jdk中自带的命令行工具做了集合。

1 安装

```php
# 安装方式一
curl -L https://alibaba.github.io/arthas/install.sh | sh
# 安装方式二
java -jar arthas-boot.jar --repo-mirror aliyun --use-http
```

2 使用

```css
java -jar arthas-boot.jar
```

![img](https:////upload-images.jianshu.io/upload_images/426671-34a1e4be7946861c.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)


![img](https:////upload-images.jianshu.io/upload_images/426671-bf62df693a748078.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)


# 参考

- [JDK内置工具使用](https://www.cnblogs.com/zengweiming/p/8946195.html)

- [JVM监控工具](https://www.jianshu.com/p/ccb7edb71c79)

