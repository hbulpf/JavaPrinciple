# BeanUtils的使用

项目中经常要用到Bean之间的属性复制，如果自己使用 set 方法逐个设值，不仅运行效率低，如果用到的地方很多，代码维护也很困难。实际上，已经有很多可以完成 Bean 属性复制的工具。本文就来重点讨论一下。本文主要包括以下内容:
* 使用 BeanUtils.copyProperties 完成Bean的属性复制
* 选择哪个框架的Bean工具效率最高
* 


## 使用 BeanUtils.copyProperties 完成Bean的属性复制

## 哪个框架 BeanUtils效率最高

很多BeanUtisl工具类，比较常用的有

1. Spring BeanUtils
2. Cglib BeanCopier
3. Apache BeanUtils
4. Apache PropertyUtils
5. Dozer


```
class BeanUtilsTestDemo {
    public static void main(String[] args)
        throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Person person = Person.builder().id("123456789").name("zhang san").age(18).dept("cloud bu").build();

        StringUtils.printHr();
        cglibBeanCopier(person, 100);
        cglibBeanCopier(person, 1000);
        cglibBeanCopier(person, 10000);
        cglibBeanCopier(person, 100000);
        cglibBeanCopier(person, 1000000);

        StringUtils.printHr();
        apachePropertyUtils(person, 100);
        apachePropertyUtils(person, 1000);
        apachePropertyUtils(person, 10000);
        apachePropertyUtils(person, 100000);
        apachePropertyUtils(person, 1000000);

        StringUtils.printHr();
        apacheBeanUtils(person, 100);
        apacheBeanUtils(person, 1000);
        apacheBeanUtils(person, 10000);
        apacheBeanUtils(person, 100000);
        apacheBeanUtils(person, 1000000);

        StringUtils.printHr();
        springBeanUtils(person, 100);
        springBeanUtils(person, 1000);
        springBeanUtils(person, 10000);
        springBeanUtils(person, 100000);
        springBeanUtils(person, 1000000);
    }

    public static void cglibBeanCopier(Person person, int times) {
        StopTimer stopwatch = new StopTimer();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonCopy personCopy = new PersonCopy();
            BeanCopier copier = BeanCopier.create(Person.class, PersonCopy.class, false);
            copier.copy(person, personCopy, null);
        }
        stopwatch.stop();
        System.out.println("cglibBeanCopier cost :" + stopwatch.getTotalTimeMillis());
    }

    public static void apachePropertyUtils(Person person, int times)
        throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        StopTimer stopwatch = new StopTimer();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonCopy personCopy = new PersonCopy();
            PropertyUtils.copyProperties(personCopy, person);
        }
        stopwatch.stop();
        System.out.println("apachePropertyUtils cost :" + stopwatch.getTotalTimeMillis());
    }

    public static void apacheBeanUtils(Person person, int times)
        throws InvocationTargetException, IllegalAccessException {
        StopTimer stopwatch = new StopTimer();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonCopy personCopy = new PersonCopy();
            BeanUtils.copyProperties(personCopy, person);
        }
        stopwatch.stop();
        System.out.println("apacheBeanUtils cost :" + stopwatch.getTotalTimeMillis());
    }

    public static void springBeanUtils(Person person, int times) {
        StopTimer stopwatch = new StopTimer();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonCopy personCopy = new PersonCopy();
            org.springframework.beans.BeanUtils.copyProperties(person, personCopy);
        }
        stopwatch.stop();
        System.out.println("springBeanUtils cost :" + stopwatch.getTotalTimeMillis());
    }
}
```

2c4g配置下，对 bean 做 100,1000,10000,100000,,1000000 次复制，耗费时间如下
```
---------------------
cglibBeanCopier cost :90
cglibBeanCopier cost :5
cglibBeanCopier cost :15
cglibBeanCopier cost :48
cglibBeanCopier cost :51
---------------------
apachePropertyUtils cost :414
apachePropertyUtils cost :88
apachePropertyUtils cost :431
apachePropertyUtils cost :2719
apachePropertyUtils cost :27991
---------------------
apacheBeanUtils cost :3
apacheBeanUtils cost :27
apacheBeanUtils cost :268
apacheBeanUtils cost :2698
apacheBeanUtils cost :28819
---------------------
springBeanUtils cost :47
springBeanUtils cost :0
springBeanUtils cost :0
springBeanUtils cost :15
springBeanUtils cost :47
```

从测试结果看，如果效率上考虑，应尽量使用 springBeanUtils 和 cglibBeanCopier, 避免使用 apachePropertyUtils 和 apacheBeanUtils。


## springframework Bean工具实现时间类向字符串转换

`org.springframework.beans.copyProperties` 使用 `SqlDateConverter` 实现时间类向字符串转换

测试代码
```
Address1 addr1=new Address1(); //Address1中的date是String
Address2 addr2=new Address2(); //Address2中的date是java.util.Date
addr1.setDate("1596470787498");
ConvertUtils.register(new SqlDateConverter(),String.class);
org.springframework.beans.copyProperties(addr2, addr1);//进行复制,注意 addr1 是 target , addr2 是source
```

## 对于springframework Bean工具未实现的功能可以自定义copyBeanProperties方法完成属性赋值

对于 `org.springframework.beans.copyProperties` 未实现的功能可以自定义copyBeanProperties方法完成属性赋值，如使用自定义方式完成 `java.sql.Timestamp`向字符串转换.

```
/**
    * Bean 复制属性值
    *
    * @param source 源对象：属性值的来源
    * @param target 目标对象：被赋值的对象
    * @throws ReflectiveOperationException 反射异常
    */
public static void copyBeanProperties(Object source, Object target) throws ReflectiveOperationException {
    BeanUtils.copyProperties(source, target);
    Class classtyType = source.getClass();
    Field[] fields = classtyType.getDeclaredFields();
    for (Field field : fields) {
        if (field.getType().equals(Timestamp.class)) {
            field.setAccessible(true);
            Object value = field.get(source);
            if (value != null) {
                long stampValue = ((Timestamp) value).getTime();
                Field f = target.getClass().getDeclaredField(field.getName());
                f.setAccessible(true);
                f.set(target, String.valueOf(stampValue));
            }
        }
    }
}
```


## 对 apacheBean工具的自定义功能

当遇到 springframework Bean工具实现的功能不满足需求时，以上方式是在 springframework Bean工具外面封装了一层，而对 apacheBean工具可以直接扩展其功能，使用起来更方便。

具体方式为 使用 `org.apache.commons.beanutils.BeanUtils.copyProperties` 自定义的Converter类进行类型转换。

CustomerDateConverter.java 

```
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.beanutils.Converter;
 
public class CustomerDateConverter implements Converter {
    //根据传来的时间字符串格式：例如：20130224201210
    private final static SimpleDateFormat DATE_FORMATE_SHOW = new SimpleDateFormat("yyyyMMddHHmmss");
    public Object convert(Class type, Object value){
       if (type.equals(java.util.Date.class) ) {
              try {
                  return DATE_FORMATE_SHOW.parse(value.toString());
              } catch (ParseException e) {
                  e.printStackTrace();
              }
       }
       return null;
    }
}
```

测试代码
```
Address1 addr1=new Address1(); //Address1中的date是String
Address2 addr2=new Address2(); //Address2中的date是java.util.Date
addr1.setDate("20130224201210");
CustomerDateConverter dateConverter = new CustomerDateConverter (); 
ConvertUtils.register(dateConverter,Date.class);
org.apache.commons.beanutils.BeanUtils.copyProperties(addr2, addr1);//进行复制,注意 addr2 是 target , addr1 是source
```


# 参考
1. [`org.apache.commons.beanutils.BeanUtils` 使用自定义的Converter类进行类型转换](https://blog.csdn.net/imonHu/article/details/77772745)
2. [`org.springframework.beans.BeanUtils` 与 `org.apache.commons.beanutils.BeanUtils` 使用对比](http://www.manongzj.com/blog/3-ouxecnltwenfrgs.html)