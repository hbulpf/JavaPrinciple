package dev.leetcode.solution;

/**
 * 392. 判断子序列
 * 给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
 * https://leetcode-cn.com/problems/is-subsequence/
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
     * @param s
     * @param t
     * @return
     */
    public boolean isSubsequence(String s, String t) {
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
     *
     * @param s
     * @param t
     * @return
     */
    public boolean isSubsequence2(String s, String t) {
        int sLen = s.length(), tLen = t.length();
        int i = 0, j = 0;
        while (i < sLen && j < tLen) {
            if (s.charAt(i) == t.charAt(j)) {
                i++;
            }
            j++;
        }
        return i == sLen;
    }
}
