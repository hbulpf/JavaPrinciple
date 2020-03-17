
`BeanUtils.copyProperties` 的的使用

 `copyProperties` 在 `org.springframework.beans.BeanUtils` 与 `org.apache.commons.beanutils.BeanUtils` 两个类中有[较大不同](http://www.manongzj.com/blog/3-ouxecnltwenfrgs.html)



## `org.springframework.beans.copyProperties` 使用 `SqlDateConverter` 实现 时间类向字符串转换

测试代码
```
Address1 addr1=new Address1(); //Address1中的date是String
Address2 addr2=new Address2(); //Address1中的date是java.util.Date
Addr1.setDate("20130224201210");
 ConvertUtils.register(new SqlDateConverter(),String.class);
org.springframework.beans.copyProperties(addr2, addr1);//进行复制,注意 addr1 是 target , addr2 是source
```


## `org.springframework.beans.copyProperties` 自定义实现`java.sql.Timestamp`向字符串转换


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


## `org.apache.commons.beanutils.BeanUtils.copyProperties` 使用自定义的Converter类进行类型转换

CustomerDateConverter.java 

```
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.beanutils.Converter;
 
public class CustomerDateConverter implements Converter {
    private final static SimpleDateFormat DATE_FORMATE_SHOW = new SimpleDateFormat("yyyyMMddHHmmss");//根据传来的时间字符串格式：例如：20130224201210
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
Address2 addr2=new Address2(); //Address1中的date是java.util.Date
Addr1.setDate("20130224201210");
CustomerDateConverter dateConverter = new CustomerDateConverter (); 
ConvertUtils.register(dateConverter,Date.class);
org.apache.commons.beanutils.BeanUtils.copyProperties(addr2, addr1);//进行复制,注意 addr2 是 target , addr1 是source
```


# 参考
1. [`org.apache.commons.beanutils.BeanUtils` 使用自定义的Converter类进行类型转换](https://blog.csdn.net/imonHu/article/details/77772745)
2. [`org.springframework.beans.BeanUtils` 与 `org.apache.commons.beanutils.BeanUtils` 使用对比](http://www.manongzj.com/blog/3-ouxecnltwenfrgs.html)
