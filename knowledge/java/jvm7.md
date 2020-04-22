# classLoader的卸载机制

以下的相关介绍都是在未使用dcevm的情况

**classLoader的卸载机制**

   jvm中没有提供class及classloader的unload方法.那热部署及osgi中是通过什么机制来实现的呢?实现思路主要是通过更换classLoader进行重新加载.之前的classloader及加载的class类在没有实例引用的情况下,在perm区gc的情况下会被回收掉.

   perm区gc时回收掉没有引用的class是一个怎样的过程呢?

- perm区达到回收条件后,对class进行引用计算,对于没有引用的class进行回收
- classLoader实例什么时候被回收呢?(很有可能会进入old gen).perm区回收一般情况下触发full gc是否目的就是清除没有实例引用此classloader?

**内存问题**

- 如果有实例类有对classloader的引用,perm区class将无法卸载,导致perm区内存一直增加,进而导致perm space error

```
public static Map pool = new HashMap();    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException{        for (int i=0;i<10000000;i++){            test(args);        }    }    public static void test(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {        ClassLoader cl = new MyLoader(Main.class.getClassLoader());        String className = "RealPerson";        @SuppressWarnings("unchecked")        Class<Person> clazz = (Class<Person>) cl.loadClass(className);        Person p = clazz.newInstance();        p.setName("qiang");        pool.put(System.nanoTime(), p);        cl = p.getClass().getClassLoader();    }
```



 

**推测:**

   osgi的bundle进行热部署时有个条件:export class 必须是兼容的.否则需要重启整个应用才会生效,为什么呢?

> osgi的export class是被bundle的parent classloader加载的,bundle内部其他类是bundle的classloader加载的,bundle更换后,重新创建classloader,并对bundle进行加载,之前的加载靠jmv gc回收掉.   

   那osgi 中explort class如果有实例引用的话,是否会导致class无法被gc掉?

> 如果osgi中没有做过处理,应该会出现此问题.具体osgi的实现还需要深入研究下.
>
> 也许osgi中关于此部分的实现使用了jvm内部的JMTI的相关接口,来对内存的引用关系进行了修改

 

[完整实例下载](http://files.cnblogs.com/redcreen/classLoader.rar)