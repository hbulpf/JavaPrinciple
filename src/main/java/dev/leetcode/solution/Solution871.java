package dev.leetcode.solution;


import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * 871. 最低加油次数
 * https://leetcode-cn.com/problems/minimum-number-of-refueling-stops/
 *
 */
class Solution871 {
    public static void main(String[] args) {
        solution1();
    }
    
    public static void solution1(){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Integer[][] gasStation = new Integer[n][2];
        // 已经加过油的加油站就不能再入队列了
        boolean[] flag = new boolean[n];
        for (int i = 0; i < n; ++i) {
            gasStation[i][0] = sc.nextInt();
            gasStation[i][1] = sc.nextInt();
            flag[i] = false;
        }
        // 使用优先队列存储可用的加油站
        Queue<Integer[]> queue = new PriorityQueue<>(n, (o1, o2) -> o2[1] - o1[1]);
        int l = sc.nextInt();
        int p = sc.nextInt();
        int mile = 0;
        int count = 0;
        while (mile < l) {
            if (p > 0) {
                mile += p;
                p = 0;
                for (int i = 0; i < n; ++i) {
                    if ((l - gasStation[i][0]) <= mile && !flag[i]) {
                        queue.add(gasStation[i]);
                        flag[i] = true;
                    }
                }
            } else {
                if (queue.isEmpty()) {
                    System.out.println(-1);
                    return;
                } else {
                    count++;
                    p = queue.poll()[1];
                }
            }
        }
        System.out.println(count);
        sc.close();
    }
}
