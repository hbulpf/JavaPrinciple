package leetcode;

import java.util.Scanner;

/**
 * 198. 打家劫舍
 */
public class Rob {
    public static void main(String[] args) {
        //预处理输入
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        str = str.replaceAll("]", "");
        str = str.replace("[", "");
        String[] strs = str.split(",");
        int[] nums = new int[strs.length];
        int i = 0;
        for (String s : strs) {
            nums[i++] = Integer.valueOf(s);
        }
        System.out.println(rob(nums));
    }

    /**
     * 动态规划简化版
     */
    public int rob(int[] nums) {
        int n = nums.length;
        //没有房子或1间房子
        if (n <= 1) return n == 0 ? 0 : nums[0];
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < n; i++)
            dp[i] = Math.max(dp[i - 1], nums[i] + dp[i - 2]);
        return dp[n - 1];
    }
}
