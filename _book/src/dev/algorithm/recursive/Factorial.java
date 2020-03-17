
package dev.algorithm.recursive;

import java.util.Scanner;

/**
 * 递归求阶乘
 *
 */
public class Factorial {
    public static int factorial(int n) {
        int r;
        if (n <= 1) {
            return n;
        } else {
            r = n * factorial(n - 1);
        }
        System.out.println(r);
        return r;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter n:");
        int n = in.nextInt();
        factorial(n);
    }
}
