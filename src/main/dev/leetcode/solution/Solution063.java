package dev.leetcode.solution;

/**
 * 63. 不同路径 II
 * https://leetcode-cn.com/problems/unique-paths-ii/
 * <p>
 * 方法:
 * 1. DP 算法
 *
 * @Author: RunAtWorld
 * @Date: 2020/4/12 22:27
 */
public class Solution063 {
    public static void main(String[] args) {
        int[][] arr = new int[][]{
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        // 预期为 : 2
        System.out.println(new Solution063().uniquePathsWithObstacles(arr));

    }
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        return uniquePathsWithObstacles2(obstacleGrid);
    }


    /**
     * DP算法
     * 使用 dp[][] 二维矩阵
     * @param obstacleGrid
     * @return
     */
    public int uniquePathsWithObstacles1(int[][] obstacleGrid) {
        int row = obstacleGrid.length;
        int col = obstacleGrid[0].length;
        if (obstacleGrid[0][0] == 1) {
            return 0;
        }
        int[][] dp = new int[row][col];
        dp[0][0] = 1;
        for (int i = 1; i < row; i++) {
            dp[i][0] = (dp[i - 1][0] == 1 && obstacleGrid[i][0] == 0) ? 1 : 0;
        }
        for (int i = 1; i < col; i++) {
            dp[0][i] = (dp[0][i - 1] == 1 && obstacleGrid[0][i] == 0) ? 1 : 0;
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = dp[i][j - 1] + dp[i - 1][j];
                }
            }
        }
        return dp[row - 1][col - 1];
    }

    /**
     * DP算法
     * 利用 obstacleGrid[][] 替换 dp[][] 二维矩阵
     * @param obstacleGrid
     * @return
     */
    public int uniquePathsWithObstacles2(int[][] obstacleGrid) {
        int row = obstacleGrid.length;
        int col = obstacleGrid[0].length;
        if (obstacleGrid[0][0] == 1) {
            return 0;
        }
        obstacleGrid[0][0] = 1;
        for (int i = 1; i < row; i++) {
            obstacleGrid[i][0] = (obstacleGrid[i - 1][0] == 1 && obstacleGrid[i][0] == 0) ? 1 : 0;
        }
        for (int i = 1; i < col; i++) {
            obstacleGrid[0][i] = (obstacleGrid[0][i - 1] == 1 && obstacleGrid[0][i] == 0) ? 1 : 0;
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                if (obstacleGrid[i][j] == 1) {
                    obstacleGrid[i][j] = 0;
                } else {
                    obstacleGrid[i][j] = obstacleGrid[i][j - 1] + obstacleGrid[i - 1][j];
                }
            }
        }
        return obstacleGrid[row - 1][col - 1];
    }
}
