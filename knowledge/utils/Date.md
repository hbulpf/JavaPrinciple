# Date

## 一、 Date类

 Date类对象的创建：

1、创建一个当前时间的Date对象

```
//创建一个代表系统当前日期的Date对象
Date d = new Date();
```

2、创建一个我们指定的时间的Date对象：

使用带参数的构造方法 `Date(int year, int month, int day)` ，可以构造指定日期的Date类对象，Date类中年份的参数应该是实际需要代表的年份减去1900，实际需要代表的月份减去1以后的值。

```
//创建一个代表2014年6月12号的Date对象
Date d1 = new Date(2014-1900, 6-1, 12); （注意参数的设置）
```

3、正确获得一个date对象所包含的信息

如：

```
Date d2 =  new Date(2014-1900, 6-1, 12);

//获得年份 （注意年份要加上1900，这样才是日期对象d2所代表的年份）
int year = d2.getYear() + 1900;

//获得月份  （注意月份要加1，这样才是日期对象d2所代表的月份）
int month = d2.getMonth() + 1;

//获得日期
int date = d2.getDate();

//获得小时
int hour = d2.getHours();//不设置默认为0

//获得分钟
int minute = d2.getMinutes();

//获得秒
int second = d2.getSeconds();

//获得星期 （注意：0代表星期日、1代表星期1、2代表星期2，其他的一次类推了）
int day = d2.getDay();
```

## 二、Calendar类

Calendar类的功能要比Date类强大很多，可以方便的进行日期的计算,获取日期中的信息时考虑了时区等问题。而且在实现方式上也比Date类要复杂一些

###### 1、Calendar类对象的创建

 Calendar类是一个抽象类，由于Calendar类是抽象类，且Calendar类的构造方法是protected的，所以无法使用Calendar类的构造方法来创建对象，API中提供了getInstance方法用来创建对象。

###### 2、创建一个代表系统当前日期的Calendar对象

```
Calendar c = Calendar.getInstance();//默认是当前日期
```

###### 3、创建一个指定日期的Calendar对象

使用Calendar类代表特定的时间，需要首先创建一个Calendar的对象，然后再设定该对象中的年月日参数来完成。

```
//创建一个代表2014年5月9日的Calendar对象
Calendar c1 = Calendar.getInstance();
c1.set(2014, 5 - 1, 9)；//调用：public final void set(int year,int month,int date)
```

###### 4、Calendar类对象信息的设置与获得

 1）Calendar类对象信息的设置

A、Set设置

如： `Calendar c1 = Calendar.getInstance();`
调用：`public final void set(int year,int month,int date)`

```
c1.set(2014, 6- 1, 9);//把Calendar对象c1的年月日分别设这为：2014、6、9
```
B、利用字段类型设置

  如果只设定某个字段，例如日期的值，则可以使用public void set(int field,int value)

```
//把 c1对象代表的日期设置为10号，其它所有的数值会被重新计算
c1.set(Calendar.DATE,10);
//把c1对象代表的年份设置为2014年，其他的所有数值会被重新计算
c1.set(Calendar.YEAR,2015);
```

 其他字段属性set的意义以此类推

Calendar类中用一下这些常量表示不同的意义，jdk内的很多类其实都是采用的这种思想

```
Calendar.YEAR——年份
Calendar.MONTH——月份
Calendar.DATE——日期
Calendar.DAY_OF_MONTH——日期，和上面的字段意义相同
Calendar.HOUR——12小时制的小时
Calendar.HOUR_OF_DAY——24小时制的小时
Calendar.MINUTE——分钟
Calendar.SECOND——秒
Calendar.DAY_OF_WEEK——星期几
``` 
    
C、Add设置(可用与计算时间)

```
Calendar c1 = Calendar.getInstance();
//把c1对象的日期加上10，也就是c1所表的日期的10天后的日期，其它所有的数值会被重新计算
c1.add(Calendar.DATE, 10);
//把c1对象的日期加上-10，也就是c1所表的日期的10天前的日期，其它所有的数值会被重新计算
c1.add(Calendar.DATE, -10);
```

 其他字段属性的add的意义以此类推

2）、Calendar类对象信息的获得(使用get（）)

```
Calendar c1 = Calendar.getInstance();
// 获得年份
int year = c1.get(Calendar.YEAR);

// 获得月份
int month = c1.get(Calendar.MONTH) + 1;（MONTH+1）

// 获得日期
int date = c1.get(Calendar.DATE);

// 获得小时
int hour = c1.get(Calendar.HOUR_OF_DAY);

// 获得分钟
int minute = c1.get(Calendar.MINUTE);

// 获得秒
int second = c1.get(Calendar.SECOND);

// 获得星期几（注意（这个与Date类是不同的）：1代表星期日、2代表星期1、3代表星期二，以此类推）
int day = c1.get(Calendar.DAY_OF_WEEK);
```

## 三、 GregorianCalendar类

GregorianCalendar 是 Calendar 的一个具体子类，提供了世界上大多数国家使用的标准日历系统。

###### 1、GregorianCalendar类对象的创建

GregorianCalendar有自己的构造方法，而其父类Calendar没有公开的构造方法。

GregorianCalendar() 在具有默认语言环境的默认时区内使用当前时间构造一个默认的 GregorianCalendar。


在具有默认语言环境的默认时区内构造一个带有给定日期设置的 GregorianCalendar。

```
GregorianCalendar(int year, int month, int dayOfMonth) 
GregorianCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute) 。
GregorianCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second)
```
 

###### 2、创建一个代表当前日期的GregorianCalendar对象

```
GregorianCalendar gc = new GregorianCalendar();
//创建一个代表2014年6月19日的GregorianCalendar对象(注意参数设置，与其父类是一样，月份要减去1)
GregorianCalendar gc = new GregorianCalendar(2014,6-1,19);
```

###### 3、另外：GregorianCalendar有一个方法：boolean isLeapYear(int year) 确定给定的年份是否为闰年


## 四、DateFormat类和SimpleDateFormat类

`public class SimpleDateFormat extends DateFormat` 是一个以与语言环境相关的方式来格式化和分析日期的具体类。(日期时间输出的工具类)

它允许进行日期格式化（日期 -> 文本）、分析（文本 -> 日期）和规范化。所以SimpleDateFormat类可以实现：String 到 Date，Date到String的互转


## 五、日期类对象之间的互转

###### Date类对象与long型时间的互转

```
//1.将Date类的对象转换为long型时间
Date d= new Date();
//使用对象的getTime（）方法完成
long dLong = d.getTime();
//2.将long型时间转换为Date类的对象
long time = 1290876532190L;
//使用Date的构造方法完成
Date d2 = new Date(time);
```
 

###### Calendar对象和long型时间之间的互转

```
// 将Calendar对象转换为相对时间
Calendar c = Calendar.getInstance();
long t1 = c.getTimeInMillis(); 
// 将long转换为Calendar对象
Calendar c1 = Calendar.getInstance();
long t = 1252785271098L;
c1.setTimeInMillis(t1);
```
 

###### Calendar对象和Date对象之间的互转

```
//1 .将Calendar对象转换为Date(c.getTime())
Calendar c = Calendar.getInstance();
Date d = c.getTime();
// 2.将Date转换为Calendar对象(s.setTime(date))
Calendar c1 = Calendar.getInstance();
Date d1 = new Date();
//通过setTime（）方法后，日历c1所表示的日期就d1的日期
c1.setTime(d1);
```
 

###### String 、 Date之间的转换：

```
//1.Date 转化String
SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
String dateStr=sdf.format(new Date());
//2.String 转化Date
String str="2010-5-27";
SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
Date birthday = sdf.parse(str);
``` 

###### Date 、Calendar对象之间的转换

```
//1.Date 转化为Calendar
Calendar calendar = Calendar.getInstance();
calendar.setTime(new Date());
//2.Calenda转换为Date
Calendar calendar = Calendar.getInstance();
Date date =calendar.getTime();
```
 

###### Date和String对象之间的转换

```
//1.Calendar 转化 String
Calendar calendat = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")0;
String dateStr = sdf.format(calendar.getTime());
//2.String 转化Calendar
String str="2010-5-27";
SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
Date date =sdf.parse(str);
Calendar calendar = Calendar.getInstance();
calendar.setTime(date);
``` 

## 六、利用以上各个类的功能，我们可以很简单的实现一些时间计算的功能，下面看几个方法：

l、判断是否是闰年
```
/**
* 给定一个年份判断该年份是否为闰年
*/
public static boolean isLeapYear(int year) {
    GregorianCalendar calendar = new GregorianCalendar();
    return calendar.isLeapYear(year);
}
```

2、获取当前日期的字符串表示形式
```
/**
    * 利用SimpleDateFormat获取当前日期的字符串表示形式 格式：2009-5-05
    */
public static String getCurrentDate() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(System.currentTimeMillis());
}

```

3、给出任意一个年月日得到该天是星期几
```
/**
    * 给出任意一个年月日得到该天是星期几
    */
public static int getWeek(String date) {
    // 注意参数的大小写格式
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    try {
        Date d = dateFormat.parse(date);
        c.setTime(d);
    } catch (ParseException e) {
    }
    return c.get(Calendar.DAY_OF_WEEK)-1;
}
```

4、 获得今天n天以后或者n天以前是那一天

```
/**
    * 获得距离今天n天的那一天的日期
    */
public static String getDistanceDay(int day) {
    Calendar calen = Calendar.getInstance();
    calen.add(Calendar.DAY_OF_MONTH, day);
    Date date = calen.getTime();
    // 这里也个用SimpleDateFormat的format（）进行格式化，然后以字符串形式返回格式化后的date
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(date);
}
```

5、 获得距离指定日期n天的那一天的日期
```
/**
    * 获得距离指定日期n天的那一天的日期
    */
public static String getDistanceDay(String date, int day) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    Date d;
    Calendar c =Calendar.getInstance();
    try {
        d = dateFormat.parse(date);
        c.setTime(d);
        c.add(Calendar.DATE, day);

    } catch (ParseException e) {
        e.printStackTrace();
    }
    return dateFormat.format(c.getTime());
}
```

6、获得给定两个日期相差的天数
```
/**
* 获得给定两个日期相差的天数
*
*/
public static long getGapDays(String date1, String date2) {
    String[] d1 = date1.split("-");
    String[] d2 = date2.split("-");
    Calendar c = Calendar.getInstance();
    c.set(Integer.parseInt(d1[0]), Integer.parseInt(d1[1]), Integer
            .parseInt(d1[2]), 0, 0, 0);
    long l1 = c.getTimeInMillis();
    c.set(Integer.parseInt(d2[0]), Integer.parseInt(d2[1]), Integer
            .parseInt(d2[2]), 0, 0, 0);
    long l2 = c.getTimeInMillis();
    return (Math.abs(l1 - l2) / (24 * 60 * 60 * 1000));
}
```
