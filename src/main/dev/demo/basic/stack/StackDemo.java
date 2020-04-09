package dev.demo.basic.stack;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * 栈的使用
 *
 * @Author: RunAtWorld
 * @Date: 2020/4/3 22:40
 */
public class StackDemo {

    public static void main(String[] args) {
        basicTest();
        basicTest2();
    }

    public static void basicTest() {
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(1);
        stack.push(3);
        stack.push(7);
        stack.push(2);
        int len = stack.size();
        for (int i = 0; i < len; i++) {
            System.out.printf("%4d",stack.pop());
        }
    }

    static void showpush(Stack<Integer> st, int a) {
        st.push(new Integer(a));
        System.out.println("push(" + a + ")");
        System.out.println("stack: " + st);
    }

    static void showpop(Stack<Integer> st) {
        System.out.print("pop -> ");
        Integer a = (Integer) st.pop();
        System.out.println(a);
        System.out.println("stack: " + st);
    }

    public static void basicTest2() {
        Stack<Integer> st = new Stack<Integer>();
        System.out.println("stack: " + st);
        showpush(st, 42);
        showpush(st, 66);
        showpush(st, 99);
        showpop(st);
        showpop(st);
        showpop(st);
        try {
            showpop(st);
        } catch (EmptyStackException e) {
            System.out.println("empty stack");
        }
    }
}
