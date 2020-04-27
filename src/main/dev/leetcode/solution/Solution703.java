package dev.leetcode.solution;

import java.util.PriorityQueue;

/**
 * 703. 数据流中的第K大元素
 * https://leetcode-cn.com/problems/kth-largest-element-in-a-stream/
 * 数据流中第K大元素
 * 使用 优先级队列,
 * 优先级队列默认从小到大排序
 *
 * @Author: RunAtWorld
 * @Date: 2020/4/2 22:36
 */
public class Solution703 {

    private PriorityQueue<Integer> queue;
    private int limit;

    public static void main(String[] args) {
        new Solution703().KthLargest(5, new int[]{1, 2, 5, 4, 6, 2, 9, 21, 89});
        System.out.println("=====");
        new Solution703().KthLargest(5, new int[]{1, 4, 6, 2});
        System.out.println("====");
        new Solution703().KthLargest(5, new int[]{1, 2, 6, 2, 9});
    }

    public void KthLargest(int k, int[] nums) {
        queue = new PriorityQueue<Integer>(k);
        limit = k;
        for (int i = 0; i < nums.length; i++) {
            add(nums[i]);
        }
    }

    public int add(int val) {
        if (queue.size() < limit) {
            queue.add(val);
        } else {
            if (val > queue.peek()) {
                queue.poll();
                queue.add(val);
            }
        }
        return queue.peek();
    }
}
