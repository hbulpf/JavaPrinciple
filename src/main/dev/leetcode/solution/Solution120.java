package dev.leetcode.solution;

import java.util.List;

/**
 * 120. 三角形最小路径和
 * https://leetcode-cn.com/problems/triangle/
 * <p>
 * 解法：
 * 1. dp
 *
 * @Author: RunAtWorld
 * @Date: 2020/6/19 0:01
 */
public class Solution120 {
    /**
     * 自上而下的 dp 算法
     * O(T)=O(N^2)
     * O(S)=O(N)
     * @param triangle
     * @return
     */
    public int minimumTotal2(List<List<Integer>> triangle) {
        if (triangle == null || triangle.size() == 0) {
            return 0;
        }
        int[] dp = new int[triangle.size()];
        //prev 表示dp[i][j-1],cur表示dp[i][j]
        int prev = 0, cur = 0;
        dp[0] = triangle.get(0).get(0);
        for (int i = 1; i < triangle.size(); i++) {
            for (int j = 0; j <= i; j++) {
                cur = dp[j];
                if (j == 0) {
                    dp[j] = cur + triangle.get(i).get(0);
                } else if (j == i) {
                    dp[j] = prev + triangle.get(i).get(i);
                } else {
                    dp[j] = Math.min(cur, prev) + triangle.get(i).get(j);
                }
                prev = cur;
            }
        }
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < triangle.size(); i++) {
            res = Math.min(res, dp[i]);
        }
        return res;
    }
}
