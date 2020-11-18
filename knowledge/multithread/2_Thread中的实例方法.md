# Java多线程2：Thread中的实例方法

**Thread类中的方法调用方式：**

学习Thread类中的方法是学习多线程的第一步。在学习多线程之前特别提出一点，调用Thread中的方法的时候，在线程类中，有两种方式，一定要理解这两种方式的区别：

（1）this.XXX()

这种调用方式表示的线程是**线程实例本身**

（2）Thread.currentThread.XXX()或Thread.XXX()

上面两种写法是一样的意思。这种调用方式表示的线程是**正在执行Thread.currentThread.XXX()所在代码块的线程**

当然，这么说，肯定有人不理解两者之间的差别。没有关系，之后会讲清楚，尤其是在讲Thread构造函数这块。讲解后，再回过头来看上面2点，会加深理解。

 

**Thread类中的实例方法**

从Thread类中的实例方法和类方法的角度讲解Thread中的方法，这种区分的角度也有助于理解多线程中的方法。实例方法，只和实例线程（也就是new出来的线程）本身挂钩，和当前运行的是哪个线程无关。看下Thread类中的实例方法：

1、start()

start()方法的作用讲得直白点就是通知"线程规划器"，此线程可以运行了，正在等待CPU调用线程对象得run()方法，产生一个**异步执行**的效果。通过start()方法产生得到结论，先看下代码：



```
public class MyThread02 extends Thread
{
    public void run()
    {
        try
        {
            for (int i = 0; i < 3; i++)
            {
                Thread.sleep((int)(Math.random() * 1000));
                System.out.println("run = " + Thread.currentThread().getName());
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
```





```
public static void main(String[] args)
{
    MyThread02 mt = new MyThread02();
    mt.start();
       
    try
    {
        for (int i = 0; i < 3; i++)
        {
            Thread.sleep((int)(Math.random() * 1000));
            System.out.println("run = " + Thread.currentThread().getName());
        }
    }
    catch (InterruptedException e)
    {
        e.printStackTrace();
    }
}
```



看下运行结果：

```
run = Thread-0
run = main
run = main
run = main
run = Thread-0
run = Thread-0
```

结果表明了：**CPU执行哪个线程的代码具有不确定性**。再看另外一个例子：



```
public class MyThread03 extends Thread
{
    public void run()
    {
        System.out.println(Thread.currentThread().getName());
    }
}
```





```
public static void main(String[] args)
{
    MyThread03 mt0 = new MyThread03();
    MyThread03 mt1 = new MyThread03();
    MyThread03 mt2 = new MyThread03();
        
    mt0.start();
    mt1.start();
    mt2.start();
}
```



看下运行结果：

```
Thread-1
Thread-2
Thread-0
```

尽管启动线程是按照mt0、mt1、mt2，但是实际的启动顺序却是Thread-1、Thread-2、Thread-0。这个例子说明了：**调用start()方法的顺序不代表线程启动的顺序，线程启动顺序具有不确定性**。

2、run()

线程开始执行，虚拟机调用的是线程run()方法中的内容。稍微改一下之前的例子看一下：



```
public static void main(String[] args)
{
    MyThread02 mt = new MyThread02();
    mt.run();
        
    try
    {
        for (int i = 0; i < 3; i++)
        {
            Thread.sleep((int)(Math.random() * 1000));
            System.out.println("run = " + Thread.currentThread().getName());
        }
    }
    catch (InterruptedException e)
    {
        e.printStackTrace();
    }
}
```



MyThread02的代码不变，看下运行结果：

```
run = main
run = main
run = main
run = main
run = main
run = main
```

看到打印了6次的"run = main"，说明如果只有run()没有start()，Thread实例run()方法里面的内容是没有任何异步效果的，全部被main函数执行。换句话说，只有run()而不调用start()启动线程是没有任何意义的。

3、isAlive()

测试线程是否处于活动状态，只要线程启动且没有终止，方法返回的就是true。看一下例子：



```
public class MyThread06 extends Thread
{
    public void run()
    {
        System.out.println("run = " + this.isAlive());
    }
}
```





```
public static void main(String[] args) throws Exception
{
    MyThread06 mt = new MyThread06();
    System.out.println("begin == " + mt.isAlive());
    mt.start();
    Thread.sleep(100);
    System.out.println("end == " + mt.isAlive());
}
```



看下运行结果：

```
begin == false
run = true
end == false
```

看到在start()之前，线程的isAlive是false，start()之后就是true了。main函数中加上Thread.sleep(100)的原因是为了确保Thread06的run()方法中的代码执行完，否则有可能end这里打印出来的是true，有兴趣可以自己试验一下。

4、getId()

这个方法比较简单，就不写例子了。在一个Java应用中，有一个long型的全局唯一的线程ID生成器threadSeqNumber，每new出来一个线程都会把这个自增一次，并赋予线程的tid属性，这个是Thread自己做的，用户无法执行一个线程的Id。

5、getName()

这个方法也比较简单，也不写例子了。我们new一个线程的时候，可以指定该线程的名字，也可以不指定。如果指定，那么线程的名字就是我们自己指定的，getName()返回的也是开发者指定的线程的名字；如果不指定，那么Thread中有一个int型全局唯一的线程初始号生成器threadInitNum，Java先把threadInitNum自增，然后以"Thread-threadInitNum"的方式来命名新生成的线程

6、getPriority()和setPriority(int newPriority)

这两个方法用于获取和设置线程的优先级，优先级高的CPU得到的CPU资源比较多，设置优先级有助于帮"线程规划器"确定下一次选择哪一个线程优先执行。换句话说，**两个在等待CPU的线程，优先级高的线程越容易被CU选择执行**。下面来看一下例子，并得出几个结论：



```
public class MyThread09_0 extends Thread
{
    public void run()
    {
        System.out.println("MyThread9_0 run priority = " + 
                this.getPriority());
    }
}
```





```
public class MyThread09_1 extends Thread
{
    public void run()
    {
        System.out.println("MyThread9_1 run priority = " + 
                this.getPriority());
        MyThread09_0 thread = new MyThread09_0();
        thread.start();
    }
}
```





```
public static void main(String[] args)
{
    System.out.println("main thread begin, priority = " + 
            Thread.currentThread().getPriority());
    System.out.println("main thread end, priority = " + 
            Thread.currentThread().getPriority());
    MyThread09_1 thread = new MyThread09_1();
    thread.start();
}
```



看一下运行结果：

```
main thread begin, priority = 5
main thread end, priority = 5
MyThread9_1 run priority = 5
MyThread9_0 run priority = 5
```

从这个例子我们得出结论：**线程默认优先级为5，如果不手动指定，那么线程优先级具有继承性，比如线程A启动线程B，那么线程B的优先级和线程A的优先级相同**。下面的例子演示了设置线程优先级带来的效果：



```
public class MyThread10_0 extends Thread
{
    public void run()
    {
        long beginTime = System.currentTimeMillis();
        for (int j = 0; j < 100000; j++){}
        long endTime = System.currentTimeMillis();
        System.out.println("◆◆◆◆◆ thread0 use time = " + 
                (endTime - beginTime));
    }
}
```





```
public class MyThread10_1 extends Thread
{
    public void run()
    {
        long beginTime = System.currentTimeMillis();
        for (int j = 0; j < 100000; j++){}
        long endTime = System.currentTimeMillis();
        System.out.println("◇◇◇◇◇ thread1 use time = " + 
                (endTime - beginTime));
    }
}
```





```
public static void main(String[] args)
{
    for (int i = 0; i < 5; i++)
    {
        MyThread10_0 mt0 = new MyThread10_0();
        mt0.setPriority(5);
        mt0.start();
        MyThread10_1 mt1 = new MyThread10_1();
        mt1.setPriority(4);
        mt1.start();
    }
}
```



看下运行结果：



```
◆◆◆◆◆ thread0 use time = 0
◆◆◆◆◆ thread0 use time = 0
◆◆◆◆◆ thread0 use time = 1
◆◆◆◆◆ thread0 use time = 2
◆◆◆◆◆ thread0 use time = 2
◇◇◇◇◇ thread1 use time = 0
◇◇◇◇◇ thread1 use time = 1
◇◇◇◇◇ thread1 use time = 5
◇◇◇◇◇ thread1 use time = 2
◇◇◇◇◇ thread1 use time = 0
```



看到黑色菱形（线程优先级高的）先打印完，再多试几次也是一样的。为了产生一个对比效果，把yMyThread10_0的优先级设置为4，看下运行结果：



```
◆◆◆◆◆ thread0 use time = 0
◇◇◇◇◇ thread1 use time = 1
◇◇◇◇◇ thread1 use time = 1
◆◆◆◆◆ thread0 use time = 0
◇◇◇◇◇ thread1 use time = 0
◆◆◆◆◆ thread0 use time = 1
◆◆◆◆◆ thread0 use time = 1
◇◇◇◇◇ thread1 use time = 2
◇◇◇◇◇ thread1 use time = 1
◆◆◆◆◆ thread0 use time = 0
```



看到，马上差别就出来了。从这个例子我们得出结论：**CPU会尽量将执行资源让给优先级比较高的线程**。

7、isDaeMon、setDaemon(boolean on)

讲解两个方法前，首先要知道理解一个概念。Java中有两种线程，一种是用户线程，一种是守护线程。守护线程是一种特殊的线程，它的作用是为其他线程的运行提供便利的服务，最典型的应用便是GC线程。如果进程中不存在非守护线程了，那么守护线程自动销毁，因为没有存在的必要，为别人服务，结果服务的对象都没了，当然就销毁了。理解了这个概念后，看一下例子：



```
public class MyThread11 extends Thread
{
    private int i = 0;
    
    public void run()
    {
        try
        {
            while (true)
            {
                i++;
                System.out.println("i = " + i);
                Thread.sleep(1000);
            }
        } 
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
```





```
public static void main(String[] args)
    {
        try
        {
            MyThread11 mt = new MyThread11();
            mt.setDaemon(true);
            mt.start();
            Thread.sleep(5000);
            System.out.println("我离开thread对象再也不打印了，我停止了！");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
```



看一下运行结果：



```
 1 i = 1
 2 i = 2
 3 i = 3
 4 i = 4
 5 i = 5
 6 我离开thread对象再也不打印了，我停止了！
 7 i = 6
```



要解释一下。我们将MyThread11线程设置为守护线程，看到第6行的那句话，而i停在6不会再运行了。这说明，main线程运行了5秒多结束，而i每隔1秒累加一次，5秒后main线程执行完结束了，MyThread11作为守护线程，main函数都运行完了，自然也没有存在的必要了，就自动销毁了，因此也就没有再往下打印数字。

关于守护线程，有一个细节注意下，setDaemon(true)必须在线程start()之前

8、interrupt()

这是一个有点误导性的名字，实际上Thread类的interrupt()方法无法中断线程。看一下例子：



```
public class TestMain12_0
{
    public static void main(String[] args)
    {
        try
        {
            MyThread12 mt = new MyThread12();
            mt.start();
            Thread.sleep(2000);
            mt.interrupt();
        } 
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
```





```
public void run()
{
    for (int i = 0; i < 500000; i++)
    {
        System.out.println("i = " + (i + 1));
    }
}
```



看下运行结果：



```
...i = 499995
i = 499996
i = 499997
i = 499998
i = 499999
i = 500000
```



看结果还是打印到了50000。也就是说，尽管调用了interrupt()方法，但是线程并没有停止。interrupt()方法的作用实际上是：**在线程受到阻塞时抛出一个中断信号，这样线程就得以退出阻塞状态**。换句话说，没有被阻塞的线程，调用interrupt()方法是不起作用的。关于这个会在之后讲中断机制的时候，专门写一篇文章讲解。

9、isInterrupted()

测试线程是否已经中断，但不清除状态标识。这个和interrupt()方法一样，在后面讲中断机制的文章中专门会讲到。

10、join()

讲解join()方法之前请确保对于http://www.cnblogs.com/xrq730/p/4853932.html一文，即wait()/notify()/notifyAll()机制已熟练掌握。

join()方法的作用是等待线程销毁。join()方法反应的是一个很现实的问题，比如main线程的执行时间是1s，子线程的执行时间是10s，但是主线程依赖子线程执行完的结果，这时怎么办？可以像生产者/消费者模型一样，搞一个缓冲区，子线程执行完把数据放在缓冲区中，通知main线程，main线程去拿，这样就不会浪费main线程的时间了。另外一种方法，就是join()了。先看一下例子：



```
public class MyThread36 extends Thread
{
    public void run()
    {
        try
        {
            int secondValue = (int)(Math.random() * 10000);
            System.out.println(secondValue);
            Thread.sleep(secondValue);
        } 
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
```





```
public static void main(String[] args) throws Exception
{
    MyThread36 mt = new MyThread36();
    mt.start();
    mt.join();
    System.out.println("我想当mt对象执行完毕之后我再执行，我做到了");
}
```



看一下运行结果：

```
3111
我想当mt对象执行完毕之后我再执行，我做到了
```

意思是，join()方法会使调用join()方法的线程（也就是mt线程）所在的线程（也就是main线程）无限阻塞，直到调用join()方法的线程销毁为止，此例中main线程就会无限期阻塞直到mt的run()方法执行完毕。

join()方法的一个重点是要区分出和sleep()方法的区别。join(2000)也是可以的，表示调用join()方法所在的线程最多等待2000ms，两者的区别在于：

**sleep(2000)不释放锁，join(2000)释放锁**，因为join()方法内部使用的是wait()，因此会释放锁。看一下join(2000)的源码就知道了，join()其实和join(2000)一样，无非是join(0)而已：



```
 1 public final synchronized void join(long millis) 
 2     throws InterruptedException {
 3     long base = System.currentTimeMillis();
 4     long now = 0;
 5 
 6     if (millis < 0) {
 7             throw new IllegalArgumentException("timeout value is negative");
 8     }
 9 
10     if (millis == 0) {
11         while (isAlive()) {
12         wait(0);
13         }
14     } else {
15         while (isAlive()) {
16         long delay = millis - now;
17         if (delay <= 0) {
18             break;
19         }
20         wait(delay);
21         now = System.currentTimeMillis() - base;
22         }
23     }
24     }
```



第12行、第20行应该已经很清楚了

# 参考

1. [Java多线程2：Thread中的实例方法](https://www.cnblogs.com/xrq730/p/4851233.html)