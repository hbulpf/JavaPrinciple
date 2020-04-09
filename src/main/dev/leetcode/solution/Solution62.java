package dev.leetcode.solution;

import java.math.BigDecimal;

/**
 * @program: JavaPrinciple
 * @description: 不同的路径
 * @author: RunAtWorld
 * @create: 2020-04-05 23:35
 **/
public class Solution62 {

    public static void main(String[] args) {
        System.out.println(new Solution62().uniquePaths(3, 2));
        System.out.println(new Solution62().uniquePaths(7, 3));
        System.out.println(new Solution62().uniquePaths(10, 10));
        System.out.println(new Solution62().uniquePaths(23, 12));
    }


    public int uniquePaths(int m, int n) {
        return uniquePaths3(m,n);
    }

    /**
     * 必须使用BigDecimal数据类型，否则内存会溢出
     * @param m
     * @param n
     * @return
     */
    public int uniquePaths3(int m, int n) {
        int a = m + n - 2;
        int b = m - 1;
        BigDecimal res1 = BigDecimal.valueOf(1);
        BigDecimal res2 = BigDecimal.valueOf(1);
        for (int i = b; i >= 1; i--) {
            res1 = res1.multiply(BigDecimal.valueOf(a));
            a--;
            res2 = res2.multiply(BigDecimal.valueOf(i));
        }
        return res1.divide(res2).intValue();
    }

    /**
     * 直接使用阶乘计算，内存会溢出
     * 因此直接计算 C ,(m-1) ,(m+n-2)
     *
     * @param m
     * @param n
     * @return
     */
    public int uniquePaths2(int m, int n) {
        return (int) (factorial(m + n - 2) / factorial(m - 1) / factorial(n - 1));
    }

    /**
     * 求 num 的阶乘
     *
     * @param num
     * @return
     */
    public static long factorial(int num) {
        long res = 1;
        for (int i = 1; i <= num; i++) {
            res *= i;
        }
        System.out.printf("num=%d,res=%d \t", num, res);
        return res;
    }
}

