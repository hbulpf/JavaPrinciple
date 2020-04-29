
package dev.demo.java8.lambda;

import java.util.ArrayList;

import dev.demo.entity.Item;

/**
 * Lambda Demo
 */
public class LambdaDemo {
    public static void main(String[] args) {
        System.out.println("------test01");
        test01();

        System.out.println("------test02");
        test02();

        System.out.println("------testThread");
        testThread();
    }

    /**
     * 语法形式为 () -> {}，其中 () 用来描述参数列表，{} 用来描述方法体，-> 为 lambda运算符 ，读作(goes to)。
     */
    public static void test01() {
        // 无参无返回
        NoReturnNoParam noReturnNoParam = () -> {
            System.out.println("NoReturnNoParam");
        };
        noReturnNoParam.method();

        // 一个参数无返回
        NoReturnOneParam noReturnOneParam = (int a) -> {
            System.out.println("NoReturnOneParam param:" + a);
        };
        noReturnOneParam.method(6);

        // 多个参数无返回
        NoReturnMultiParam noReturnMultiParam = (int a, int b) -> {
            System.out.println("NoReturnMultiParam param:" + "{" + a + "," + +b + "}");
        };
        noReturnMultiParam.method(6, 8);

        // 无参有返回值
        ReturnNoParam returnNoParam = () -> {
            System.out.print("ReturnNoParam");
            return 1;
        };
        int res = returnNoParam.method();
        System.out.println("return:" + res);

        // 一个参数有返回值
        ReturnOneParam returnOneParam = (int a) -> {
            System.out.println("ReturnOneParam param:" + a);
            return 1;
        };
        int res2 = returnOneParam.method(6);
        System.out.println("return:" + res2);

        // 多个参数有返回值
        ReturnMultiParam returnMultiParam = (int a, int b) -> {
            System.out.println("ReturnMultiParam param:" + "{" + a + "," + b + "}");
            return 1;
        };
        int res3 = returnMultiParam.method(6, 8);
        System.out.println("return:" + res3);
    }

    public static void test02() {
        // 1.简化参数类型，可以不写参数类型，但是必须所有参数都不写
        NoReturnMultiParam lamdba1 = (a, b) -> {
            System.out.println("简化参数类型");
        };
        lamdba1.method(1, 2);

        // 2.简化参数小括号，如果只有一个参数则可以省略参数小括号
        NoReturnOneParam lambda2 = a -> {
            System.out.println("简化参数小括号");
        };
        lambda2.method(1);

        // 3.简化方法体大括号，如果方法条只有一条语句，则可以胜率方法体大括号
        NoReturnNoParam lambda3 = () -> System.out.println("简化方法体大括号");
        lambda3.method();

        // 4.如果方法体只有一条语句，并且是 return 语句，则可以省略方法体大括号
        ReturnOneParam lambda4 = a -> a + 3;
        System.out.println(lambda4.method(5));

        ReturnMultiParam lambda5 = (a, b) -> a + b;
        System.out.println(lambda5.method(1, 1));
    }

    public static void testThread() {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(2 + ":" + i);
            }
        });
        t.start();
    }

    public static void testList() {
            ArrayList<Item> items = new ArrayList<>();
            items.add(new Item(11, "小牙刷", 12.05 ));
            items.add(new Item(5, "日本马桶盖", 999.05 ));
            items.add(new Item(7, "格力空调", 888.88 ));
            items.add(new Item(17, "肥皂", 2.00 ));
            items.add(new Item(9, "冰箱", 4200.00 ));

            items.removeIf(ele -> ele.getId() == 7);

            //通过 foreach 遍历，查看是否已经删除
            items.forEach(System.out::println);

        }
}
