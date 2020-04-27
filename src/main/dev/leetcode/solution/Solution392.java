package dev.leetcode.solution;

/**
 * 392. 判断子序列
 * https://leetcode-cn.com/problems/is-subsequence/
 *
 * 给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
 * 1. 贪心算法
 * 2. DP算法：不建议使用，纯粹学习思想
 *
 * @Author: RunAtWorld
 * @Date: 2020/4/8 22:45
 */
public class Solution392 {
    public static void main(String[] args) {
        Solution392 solution392 = new Solution392();
        String s = "abc", t = "ahbgdc";
        System.out.println(solution392.isSubsequence(s, t));
        s = "axc";
        t = "ahbgdc";
        System.out.println(solution392.isSubsequence(s, t));
    }

    /**
     * 贪心算法
     * T=O(n)
     *
     * @param s
     * @param t
     * @return
     */
    public boolean isSubsequence3(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        int i = 0, j = 0;
        while (i < sLen && j < tLen) {
            if (s.charAt(i) == t.charAt(j)) {
                i++;
            }
            j++;
        }
        return i == sLen;
    }

    /**
     * DP算法
     * 一题多解
     * T = O(m*n)
     * S = O(m*n)
     * @param s
     * @param t
     * @return
     */
    public boolean isSubsequence(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        boolean[][] dp = new boolean[sLen + 1][tLen + 1];
        for (int j = 0; j <= tLen; j++) {
            dp[0][j] = true;
        }
        for (int i = 1; i <= sLen; i++) {
            for (int j = 1; j <= tLen; j++) {
                if (s.charAt(i-1) == t.charAt(j-1)) {
                    //如果
                    dp[i][j] = dp[i - 1][j - 1];
                }else {
                    dp[i][j] = dp[i][j - 1];
                }
            }
        }
        return dp[sLen][tLen];
    }
}
