package dev.lpf.leetcode.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 406. 根据身高重建队列
 * 假设有打乱顺序的一群人站成一个队列。 每个人由一个整数对(h, k)表示，其中h是这个人的身高，k是排在这个人前面且身高大于或等于h的人数。 编写一个算法来重建这个队列。
 *
 * 注意：
 * 总人数少于1100人。
 *
 * 示例
 *
 * 输入:
 * [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
 *
 * 输出:
 * [[5,0], [7,0], [5,2], [6,1], [4,4], [7,1]]
 *
 * 分析：
 * 由于题目给的推理信息是：k是排在这个人前面且身高大于或等于h的人数
 * 所以当要判断某一个人的位置时，我们一定要知道所有比他高的人的信息，即让高的人先出现。
 * 当我们知道所有比他高的人的信息，就可以很简单的判断他的位置了，因为他就是在队列中第k+1个人。
 * （1）首先按h从高到低排序，h相同的按k从小到大排序
 * （2）进行排序后，当对第i个人进行位置判断时，他告诉我们“前面比他高或与他等高的人的个数为k”，并且前面已经进行排序的人都比他高或者与他等高，所以可以判断在前i个人中，他的排序位置为k+1。
 *
 */
class Solution406Optimum {
    static int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people, (a, b) -> (a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]));
        List<int[]> res = new ArrayList<>();
        for(int[] p : people) {
            res.add(p[1],p);
        }
        return res.toArray(new int[res.size()][]);
    }

    public static void main(String[] args) {
        int[][] height = {{7,0},{4,4},{7,1},{5,0},{6,1},{5,2}};
        reconstructQueue(height);
        for(int i = 0; i < height.length; i++) {
            System.out.println(Arrays.toString(height[i]));
        }
    }
}
