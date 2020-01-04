开发细节

###### 1. [打印堆栈](src/dev/lpf/demo/basic/exception/Factorial.java)

方法1:
```java
/**
 * 打印堆栈 : 获取文件名和当前执行代码的行号
 */
public static void printStack() {
    final Throwable t = new Throwable();
    StackTraceElement[] frames = t.getStackTrace();
    for (StackTraceElement frame : frames) {
        System.out.println(frame);
    }
}
```

方法2:
```java
Thread.dumpStack();
```

方法3:
```java
/**
 * 获取当前行堆栈调用信息
 */
public static String getStackString() {
    StringWriter out = new StringWriter();
    new Throwable().printStackTrace(new PrintWriter(out));
    String description = out.toString();
    System.out.println(description);
    return description;
}
```

###### 2. [带资源的try语句自动close资源](src/dev/lpf/demo/basic/exception/ExceptionDemo.java)

```java
/**
 * 带资源的 try 语句
 */
public static void test02() {
    //用这种方式，无论如何，in 和 out 都会关闭
    try (Scanner in = new Scanner(new FileInputStream("words"), "utf-8");
        PrintWriter out = new PrintWriter("out")) {
        while (in.hasNext()) {
            out.println(in.next().toUpperCase());
        }
    } catch (FileNotFoundException e) {
        System.out.println(e.getMessage());
    }
}
```
