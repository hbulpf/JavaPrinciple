package dev.leetcode.solution;

import java.util.Stack;

/**
 * 42. 接雨水
 * https://leetcode-cn.com/problems/trapping-rain-water/
 *
 * @Author: RunAtWorld
 * @Date: 2020/5/12 0:29
 */
public class Solution042 {

    /**
     * 使用栈
     * <p>
     * T=O(N)
     * S=O(N)
     *
     * @param height
     * @return
     */
    public static int trap3(int[] height) {
        Stack<Integer> stack = new Stack<Integer>();
        if (height.length <= 1) {
            return 0;
        }
        int current = 1;
        stack.push(height[0]);
        while (!stack.empty()&&current<height.length){

        }
        return 0;
    }
}
