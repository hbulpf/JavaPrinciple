# 你真的会使用BeanUtils完成Bean拷贝吗

> 项目中经常要用到Bean之间的属性复制，本文主要介绍了几种常见的Bean拷贝工具以及几种在Bean拷贝过程中根据需要实现Bean字段类型转换的方法。​

项目中经常要用到Bean之间的属性复制，如果自己使用 set 方法逐个设值，不仅运行效率低，如果用到的地方很多，代码维护也很困难。实际上，已经有很多可以完成 Bean 属性复制的工具。本文就来重点讨论一下。本文主要包括以下内容:
* 选择哪个框架的Bean工具效率最高
* 封装springBean工具自定义copyBeanProperties实现Bean拷贝
* apacheBean工具实现时间类字段向字符串字段拷贝
* apacheBean工具自定义Converter实现Bean拷贝

## 哪个BeanUtils效率最高

当前有很多用于Bean拷贝的BeanBeanUtisl工具类，常见的主要有

1. Spring BeanUtils
2. Cglib BeanCopier
3. Apache BeanUtils
4. Apache PropertyUtils


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

结论:

从测试结果看，如果效率上考虑，应尽量使用 springBeanUtils 和 cglibBeanCopier, 避免使用 apachePropertyUtils 和 apacheBeanUtils。


## 封装springBean工具自定义copyBeanProperties实现Bean拷贝

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

测试代码

```
/**
    * 基于 springBeanUtils 改造的 Bean 工具
    *
    * @throws ReflectiveOperationException
    */
private static void surperSpringBeanUtilTest() throws ReflectiveOperationException {
    //springBeanUtils
    Address1 addr3 = new Address1("beijing", new Date(1596470797498L));
    Address2 addr4 = new Address2();
    BeanUtil.copyBeanProperties(addr3, addr4);//addr3 是 target , addr2 是source
    System.out.println(addr3);
    System.out.println(addr4);
}
```

结果

```
Person{id='12', name='WangZhi', age=19, dept='TEG', birthday='20010804130907'}
PersonCopy{id='12', name='WangZhi', age=19, dept='TEG', birthday=Sat Aug 04 13:09:07 CST 2001}
```


## apacheBean工具实现时间类字段向字符串字段拷贝

`org.springframework.beans.copyProperties` 使用 `SqlDateConverter` 实现时间类字段向字符串字段拷贝

测试代码
```
/**
    * 基于 apacheBean 工具从 Date 字段的Bean复制到 String 字段的Bean
    *
    * @throws InvocationTargetException
    * @throws IllegalAccessException
    */
private static void apacheBeanCopyFromDate2String() throws InvocationTargetException, IllegalAccessException {
    // apacheBeanUtils
    Address1 addr1 = new Address1("wuhan", new Date(1596470787498L));
    Address2 addr2 = new Address2();
    ConvertUtils.register(new SqlDateConverter(), String.class);
    //addr1 是 source , addr2 是 target
    org.apache.commons.beanutils.BeanUtils.copyProperties(addr2, addr1);
    System.out.println(addr1);
    System.out.println(addr2);

    //springBeanUtils
    Address1 addr3 = new Address1("beijing", new Date(1596470797498L));
    Address2 addr4 = new Address2();
    //addr3 是 target , addr2 是source
    org.springframework.beans.BeanUtils.copyProperties(addr3, addr4);
    System.out.println(addr3);
    System.out.println(addr4);
}
```

结果

```
Address1{city='wuhan', createAt=2020-08-04}
Address2{city='wuhan', createAt='2020-08-04'}
Address1{city='beijing', createAt=2020-08-04}
Address2{city='beijing', createAt='null'}
```

## apacheBean工具自定义Converter实现Bean拷贝

当遇到 springframework Bean工具实现的功能不满足需求时，使用反射方式在 springframework Bean工具外面封装了一层，而对 apacheBean工具可以直接扩展其功能，使用起来更方便。

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
/**
    * 基于 apacheBean 工具从 String 字段的Bean复制到 Date 字段的Bean
    * @throws InvocationTargetException
    * @throws IllegalAccessException
    */
private static void apacheBeanCopyFromString2Date() throws InvocationTargetException, IllegalAccessException {
    Person p1 = new Person("12", "WangZhi", 19, "TEG", "20010804130907");
    PersonCopy p2 = new PersonCopy();
    CustomerDateConverter dateConverter = new CustomerDateConverter();
    ConvertUtils.register(dateConverter, java.util.Date.class);
    org.apache.commons.beanutils.BeanUtils.copyProperties(p2, p1);
    System.out.println(p1);
    System.out.println(p2);
}
```

结果

```
Person{id='12', name='WangZhi', age=19, dept='TEG', birthday='20010804130907'}
PersonCopy{id='12', name='WangZhi', age=19, dept='TEG', birthday=Sat Aug 04 13:09:07 CST 2001}
```


## 总结

本文主要介绍了几种常见的Bean拷贝工具: Spring BeanUtils、Cglib BeanCopier、Apache BeanUtils、Apache PropertyUtils。
通过示例实验发现，springBeanUtils 和 cglibBeanCopier 的效率更高，因此如果要拷贝的Bean数量较多，应首先考虑这两个工具。

另外本文也介绍了几种在Bean拷贝过程中根据需要实现Bean字段类型转换的方法，尤其是封装springBean工具自定义copyBeanProperties实现Bean拷贝 和 使用apacheBean工具自定义Converter实现Bean拷贝两种方法在实际开发中十分有用。

本文代码: https://gitee.com/sifangcloud/mavensample/blob/master/src/main/java/dev/utils/demos/BeanUtilsTestDemo.java

# 参考
1. [`org.apache.commons.beanutils.BeanUtils` 使用自定义的Converter类进行类型转换](https://blog.csdn.net/imonHu/article/details/77772745)
2. [`org.springframework.beans.BeanUtils` 与 `org.apache.commons.beanutils.BeanUtils` 使用对比](http://www.manongzj.com/blog/3-ouxecnltwenfrgs.html)