
package dev.demo.basic.exception;

import java.util.Scanner;

/**
 * 打印堆栈
 * 堆栈分析: 获取文件名和当前执行代码的行号
 */
public class Factorial {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter n:");
        int n = in.nextInt();
        factorial(n);
    }

    /**
     * 递归方法求阶乘
     *
     * @param n the n
     * @return the int
     */
    public static int factorial(int n) {
        // 堆栈分析: 获取文件名和当前执行代码的行号
        System.out.println("factorial(" + n + ")");
        printStack();
        int r;
        if (n <= 1) {
            return n;
        } else {
            r = n * factorial(n - 1);
        }
        System.out.println(r);
        return r;
    }

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
}
