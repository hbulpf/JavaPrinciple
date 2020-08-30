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
        if (height == null) {
            return 0;
        }
        int ans = 0;
        int current = 0;
        while (current < height.length) {
            while (!stack.empty() && height[stack.peek()] < height[current]) {
                int top = stack.peek();
                stack.pop();
                if (stack.empty()) {
                    break;
                }
                int boundedHeight = Math.min(height[stack.peek()], height[current]) - height[top];
                int width = current - stack.peek() - 1;
                ans += boundedHeight * width;
            }
            stack.push(current++);
        }
        return ans;
    }
}
