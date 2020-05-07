package dev.leetcode.solution;

/**
 * 11. 盛最多水的容器
 * https://leetcode-cn.com/problems/container-with-most-water/
 *
 * (1) 暴力法 T=O(N^2),S=O(1)
 * (2) 双指针法 T=O(N),S=O(1)
 * @Author: RunAtWorld
 * @Date: 2020/5/7 23:22
 */
public class Solution011 {
    public static void main(String[] args) {
        int ans;
        ans=maxArea1(new int[]{1,8,6,2,5,4,8,3,7});
        System.out.println(ans);

        ans=maxArea2(new int[]{1,8,6,2,5,4,8,3,7});
        System.out.println(ans);
    }

    /**
     * 暴力法
     *
     * @param height
     * @return
     */
    public static int maxArea1(int[] height) {
        int area = 0;
        for (int i = 0; i < height.length; i++) {
            for (int j = i + 1; j < height.length; j++) {
                area = Math.max(area, Math.min(height[i], height[j]) * (j - i));
            }
        }
        return area;
    }

    /**
     * 双指针法
     * l 为左指针，从左边开始；r为右指针，从右边开始
     *
     * @param height
     * @return
     */
    public static int maxArea2(int[] height) {
        int l = 0, r = height.length - 1;
        int area = 0, max = 0;
        while (l < r) {
            area = Math.min(height[l], height[r]) * (r - l);
            max = Math.max(area, max);
            if (height[l] < height[r]) {
                l++;
            } else {
                r--;
            }
        }
        return max;
    }
}
