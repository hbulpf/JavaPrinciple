package dev.leetcode.solution;

import java.util.Arrays;

/**
 * 455. 分发饼干
 * https://leetcode-cn.com/problems/assign-cookies/
 *
 * @Author: RunAtWorld
 * @Date: 2020/4/8 22:45
 */
public class Solution455 {
    public static void main(String[] args) {
        Solution455 solution455 = new Solution455();
        int[] children = new int[]{1, 2, 3};
        int[] cookies = new int[]{1, 1};
        System.out.println(solution455.findContentChildren(children, cookies));

        children = new int[]{1, 2};
        cookies = new int[]{1, 2, 3};
        System.out.println(solution455.findContentChildren(children, cookies));

        children = new int[]{10, 9, 8, 7};
        cookies = new int[]{5, 6, 7, 8};
        System.out.println(solution455.findContentChildren(children, cookies));
        children = new int[]{1, 2, 3};
        cookies = new int[]{3};
        System.out.println(solution455.findContentChildren(children, cookies));
    }

    /**
     * 贪心算法
     * 对小朋友和饼干按照从小到大排序
     * T=O(n)
     * @param g
     * @param s
     * @return
     */
    public int findContentChildren(int[] g, int[] s) {
        //从小到大排序
        Arrays.sort(g);
        Arrays.sort(s);
        int child = 0, cookie = 0;
        while (child < g.length && cookie < s.length) {
            if (s[cookie] >= g[child]) {
                child++;
            }
            cookie++;
        }
        return child;
    }
}
