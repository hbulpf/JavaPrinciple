package dev.leetcode.solution;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * 62. 不同路径
 * https://leetcode-cn.com/problems/unique-paths/
 * 方法:
 * 1. 排列组合法
 * 2. dp算法: dp[][] 二维矩阵,dp[] 一维矩阵两种方法
 *
 * @program: JavaPrinciple
 * @description: 不同的路径
 * @author: RunAtWorld
 * @create: 2020-04-05 23:35
 **/
public class Solution062 {

    public static void main(String[] args) {
        System.out.println(new Solution062().uniquePaths(3, 2));
        System.out.println(new Solution062().uniquePaths(7, 3));
        System.out.println(new Solution062().uniquePaths(10, 10));
        System.out.println(new Solution062().uniquePaths(23, 12));
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

    public int uniquePaths(int m, int n) {
        return uniquePaths2(m, n);
    }

    /**
     * dp 算法
     * 使用一个 dp[][]  二维矩阵
     * T= m*n
     * S= m*n
     *
     * @param m
     * @param n
     * @return
     */
    public int uniquePaths1(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i][j - 1] + dp[i - 1][j];
            }
        }
        return dp[m - 1][n - 1];
    }

    /**
     * dp算法优化1
     * 将 dp[][]  二维矩阵换成 pre[] 和 cur[]
     * T= O(m*n)
     * S= 2*n
     *
     * @param m
     * @param n
     * @return
     */
    public int uniquePaths2(int m, int n) {
        int[] pre = new int[n];
        int[] cur = new int[n];
        Arrays.fill(pre, 1);
        Arrays.fill(cur, 1);
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                cur[j] = pre[j] + cur[j - 1];
            }
            pre = cur.clone();
        }
        return cur[n - 1];
    }

    /**
     * dp 算法优化2
     * 使用一个 dp[][]  二维矩阵
     * T= O(m*n)
     * S= n
     *
     * @param m
     * @param n
     * @return
     */
    public int uniquePaths3(int m, int n) {
        int[] cur = new int[n];
        Arrays.fill(cur, 1);
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                cur[j] += cur[j - 1];
            }
        }
        return cur[n - 1];
    }

    /**
     * 排列组合法
     * 直接使用阶乘计算，内存会溢出
     * 因此直接计算 C ,(m-1) ,(m+n-2)
     *
     * @param m
     * @param n
     * @return
     */
    public int uniquePaths4(int m, int n) {
        return (int) (factorial(m + n - 2) / factorial(m - 1) / factorial(n - 1));
    }

    /**
     * 排列组合法
     * 必须使用BigDecimal数据类型，否则内存会溢出
     *
     * @param m
     * @param n
     * @return
     */
    public int uniquePaths5(int m, int n) {
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
}

