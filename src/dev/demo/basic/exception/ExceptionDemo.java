
package dev.demo.basic.exception;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * ExceptionDemo
 */
public class ExceptionDemo {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        test01();
        System.out.println("====================");
        test02();
    }

    /**
     * Exception 测试
     */
    public static void test01() {
        String str = "ssa";
        Double d = null;
        try {
            d = DivideUtil.divide(1f, 0);
            System.out.println("result:" + d);
        } catch (MyException e) {
            if (d == null) {
                System.out.println("default value:" + null);
            }
            System.out.println("error:" + e.getMessage());
        } finally {
            System.out.println("finally");
        }
    }

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
}
