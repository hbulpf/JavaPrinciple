package dev.leetcode.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 15. 三数之和
 * https://leetcode-cn.com/problems/3sum/
 * <p>
 * 解法：
 * 1. 双指针
 *
 * @Author: RunAtWorld
 * @Date: 2020/6/19 0:01
 */
class Solution015 {

    public static void main(String[] args) {
        int[] nums = new int[] {-1, 0, 1, 2, -1, -4};
        System.out.println(threeSum(nums));
    }

    public static List<List<Integer>> threeSum(int[] nums) {

        List<List<Integer>> res = new ArrayList<List<Integer>>();
        Arrays.sort(nums);
        int len = nums.length;
        //枚举a
        for (int first = 0; first < len; first++) {
            // first > 0 的条件保证 a=0 被枚举一次
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }
            int third = len - 1;
            //枚举b
            for (int second = first + 1; second < len; second++) {
                // second > first + 1 的条件保证 b=first + 1 被枚举一次
                if (second > first + 1 && nums[second - 1] == nums[second]) {
                    continue;
                }
                //枚举c
                while (second < third && nums[first] + nums[second] + nums[third] > 0) {
                    third--;
                }
                // 如果 b 和 c指针重合，就不会再有 b增加， a+b+c=0
                if (second == third) {
                    break;
                }
                if (nums[first] + nums[second] + nums[third] == 0) {
                    List<Integer> oneTuple = new ArrayList();
                    oneTuple.add(nums[first]);
                    oneTuple.add(nums[second]);
                    oneTuple.add(nums[third]);
                    res.add(oneTuple);
                }
            }
        }
        return res;
    }
}
