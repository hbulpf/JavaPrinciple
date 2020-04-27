package dev.leetcode.solution;

/**
 * 最大子序和
 * 1. DP算法 : T=O(N),S=O(1).利用nums数组本身作为DP数组
 * 2. 分治法
 *
 * @Author: RunAtWorld
 * @Date: 2020/4/4 22:36
 */
public class Solution053 {
    public static void main(String[] args) {
        int[] arr = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int res = new Solution053().maxSubArray(arr);
        System.out.println(res);

        for (int k : arr) {
            System.out.printf("%-5d", k);
        }
    }

    /**
     * DP算法 : T=O(N),S=O(1).
     * 利用nums数组本身作为DP数组,
     * nums[i]代表包含第i个元素的最大和
     *
     * @param nums
     * @return
     */
    public int maxSubArray(int[] nums) {
        int maxSum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] > 0) {
                nums[i] += nums[i - 1];
            }
            maxSum = Math.max(maxSum, nums[i]);
        }
        return maxSum;
    }
}
