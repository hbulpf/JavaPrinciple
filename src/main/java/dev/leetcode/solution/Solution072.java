package dev.leetcode.solution;

/**
 * 72. 编辑距离
 * https://leetcode-cn.com/problems/edit-distance/
 * <p>
 * 动态规划方法
 * T=O(mn)
 * S=O(mn)
 *
 * @Author: RunAtWorld
 * @Date: 2020/5/12 23:34
 */
public class Solution072 {

    /**
     * 动态规划方法
     * T=O(mn)
     * S=O(mn)
     *
     * @param word1
     * @param word2
     * @return
     */
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        if (m * n == 0) {
            return m + n;
        }
        int[][] dp = new int[m + 1][n + 1];

        //初始化dp数组
        for (int i = 0; i < m + 1; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i < n + 1; i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int left = dp[i - 1][j] + 1;
                int down = dp[i][j - 1] + 1;
                int leftDown = dp[i - 1][j - 1];
                if (word1.charAt(i - 1) != word2.charAt(j - 1)) {
                    leftDown += 1;
                }
                dp[i][j] = Math.min(Math.min(left, down), leftDown);
            }
        }
        return dp[m][n];
    }
}
