# List和Array用法


## Arrays 转换相关


1、List 转换为 String[]

```
List<String> list = new ArrayList();
list.add("aaa");
list.add("bbb");
// 方法1：得到 Object[] 数组，对其逐个转化
Object[] objects = list.toArray();
for (Object o : objects) {
    System.out.printf("%s\t", o.toString());
}

System.out.println();
// 方法2:转换成数组
String[] arrString = list.toArray(new String[list.size()]);
System.out.println(arrString.toString());
```

2、Array 转成 List

```
String[] array = new String[] {"aaa", "bbb"};
System.out.println(Arrays.toString(array));
List list = Arrays.asList(array);
System.out.println(list.toString());
// list.add("hiss");
// 上面语句会报异常，解决方法是新建一个List对象,
List listNew = new ArrayList(list);
listNew.add("hisxs");
System.out.println(listNew.toString());
```

3、List 转换为 String 

```
List list = new ArrayList();
list.add("aaa");
list.add("bbb");
// 转换成数组
String str = String.join(",", list);
System.out.println(str);
```

4、String 转换为 List ：

```
List list = new ArrayList();
list.add("aaa");
list.add("bbb");
// 转换成数组
String str = "1,2,3,4,5";
ArrayList arrayList = new ArrayList(Collections.singleton(str.split(",")));
System.out.println(arrayList.toString());
```

##### 参考

1. [Java中List与数组互相转换](https://www.jianshu.com/p/7eee157f74fc)

## ArrayList 类

集合ArrayList包含的元素可以数字，字符串，object等等，ArrayList 的容量是 ArrayList 可以保存的元素数。

随着向 ArrayList 中添加元素，容量通过重新分配按需自动增加。可通过调用 trimToSize 或通过显式设置 Capacity 属性减少容量。

使用整数索引可以访问此集合中的元素。 此集合中的索引从零开始。 ArrayList 接受 null 作为有效值并且允许重复的元素。

1、泛型转换,调用ArrayList的addAll方法

```
List<Student> list = new ArrayList<Student>();
ArrayList arrayList = new ArrayList();
arrayList.addAll(list);
```
 
 

2、非泛型转换,需要遍历List循环加入到ArrayList。

```
List<Student> list = new ArrayList<Student>();
list.add(new Student("张红",23,223));
ArrayList arrayList = new ArrayList();
arrayList.add(list.get(0).getAge()) ;
```
 
这里的ArrayList仅仅是个int数组，保存list里面的id字段的数据。
