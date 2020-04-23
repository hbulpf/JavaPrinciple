
package dev.lpf.leetcode.solution;

/**
 * 470. 用 Rand7() 实现 Rand10()
 * https://leetcode-cn.com/problems/implement-rand10-using-rand7/
 *
 * 分为2种情况:
 * 1. 小数映射到大数
 * 2. 大数映射到小数
 * 都要遵循: 等概率映射原则
 */
public class Solution470 {
    public static void main(String[] args) {
        Solution470 solution470 = new Solution470();
        System.out.println(solution470.rand10());
    }

    public int rand10() {
        int idx = 100;
        while (idx > 40) {
            int col = rand7();
            int row = rand7();
            idx = col + (row - 1) * 7;
        }
        return idx % 10 + 1;
    }

    public int rand7() {
        return (int) (7 * Math.random()) + 1;
    }
}
