package dev.leetcode.solution;

/**
 * 70. 爬楼梯
 * https://leetcode-cn.com/problems/climbing-stairs/
 *
 * @Author: RunAtWorld
 * @Date: 2020/4/16 0:29
 */
public class Solution070 {

    /**
     * 迭代法
     *
     * @param n
     * @return
     */
    public static int climbStairs(int n) {
        if (n == 1 || n == 2) {
            return n;
        }

        int a = 1;
        int b = 2;
        int num = 0;
        for (int i = 3; i <= n; i++) {
            num = a + b;
            a = b;
            b = num;
        }
        return b;
    }

    /**
     * 动态规划法
     *
     * @param n
     * @return
     */
    public static int climbStairs2(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i < n + 1; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    /**
     * 递归法
     *
     * @param n
     * @return
     */
    public static int climbStairs3(int n) {
        if (n == 1 || n == 2) {
            return n;
        }
        return climbStairs3(n - 1) + climbStairs3(n - 2);
    }

    public static void main(String[] args) {
        System.out.printf("%d,%d,%d\n",climbStairs(3),climbStairs(40),climbStairs(60));
        System.out.printf("%d,%d,%d\n",climbStairs2(3),climbStairs2(40),climbStairs2(60));
        System.out.printf("%d,%d,%d\n",climbStairs3(3),climbStairs3(40),climbStairs3(60));
    }
}
