package test.java8test;

import java.util.Optional;

/**
 * Java8 Optional 测试
 * Optional 一个可以为 null 的容器
 *
 */
public class OptionalTester {
    public static void main(String[] args) {

        System.out.println("---of(),isPresent(),get() ");
        //of() 为非 null 的值创建一个 Optional 实例
        //isPresent() 如果值存在，返回 true，否则返回 false
        //get() 返回该对象，有可能返回 null
        Optional<String> op1 = Optional.of("Hello");
        System.out.println(op1.isPresent()); // 输出 true
        System.out.println(op1.get()); // 输出 Hello
        System.out.println(Optional.ofNullable(op1)); // 输出 Optional 实例包装的Hello
        try {
            Optional<String> op2 = Optional.of(null); // 抛出null异常
        } catch (Exception e) {
            System.out.println("错误:" + e.getMessage());
        }

        System.out.println("---OfNullable()");
        //OfNullable() 如果指定的值为 null，返回一个空的 Optional
        Optional<String> op3 = Optional.ofNullable(null);
        System.out.println(op3.isPresent()); // 输出 false

        System.out.println("---orElse(obj)");
        //orElse(obj) 如果实例非空，返回该实例，否则返回 obj
        Optional<String> op6 = Optional.of("Hello");
        System.out.println(op6.orElse("World")); // 输出 Hello
        Optional<String> op10 = Optional.ofNullable("Hello");
        System.out.println(op10.orElse("World")); // 输出 Hello
        Optional<String> op7 = Optional.ofNullable(null);
        System.out.println(op7.orElse("World")); // 输出 World

        System.out.println("---orElseGet()");
        //orElseGet(Supplier<? extends T> other) 如果实例非空，返回该实例，否则返回 Supplier
        Optional<String> op8 = Optional.of("Hello");
        System.out.println(op8.orElseGet(() -> {return new String("World");})); // 输出 Hello
        Optional<String> op9 = Optional.ofNullable(null);
        System.out.println(op9.orElseGet(() -> {return new String("World");})); // 输出 World

        System.out.println("---map()");
        //map() 如果实例非空,进行map操作处理
        Optional<String> op4 = Optional.of("Hello");
        Optional<String> op5 = op4.map((s) -> s.toUpperCase());
        op5.ifPresent((s) -> {
            System.out.println(s); // 输出 HELLO
        });

    }
}
