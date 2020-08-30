package dev.leetcode.solution;

/**
 * 55. 跳跃游戏
 * https://leetcode-cn.com/problems/jump-game/
 *
 * @Author: RunAtWorld
 * @Date: 2020/6/23 0:02
 */
public class Solution055 {
    public boolean canJump1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return true;
        }
        int rightmost = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (rightmost < i) {
                return false;
            }
            rightmost = Math.max(rightmost, i + nums[i]);
        }
        return true;
    }

    public boolean canJump2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return true;
        }
        int rightmost = 0;
        int len = nums.length;
        for (int i = 0; i < len; i++) {
            //rightmost到达 nums[0],距离为0
            if (rightmost >= i) {
                rightmost = Math.max(rightmost, i + nums[i]);
                //最远位置为len-1
                if (rightmost >= len - 1) {
                    return true;
                }
            }

        }
        return false;
    }
}
