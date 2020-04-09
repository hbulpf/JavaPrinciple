
package dev.leetcode.solution;

import java.util.Arrays;

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
class Solution406 {
    /**
     * 打印数组
     *
     * @param arr the arr
     */
    static void printArr(int[][] arr) {
        Arrays.stream(arr).forEach(line -> {
            System.out.printf("[");
            for (int e : line) {
                System.out.printf("%d,", e);
            }
            System.out.printf("]\n");
        });
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
         int[][] people = new int[][] {{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}};
//        int[][] people = new int[][] {{8, 0}, {4, 4}, {8, 1}, {5, 0}, {6, 1}, {5, 2}};
        new Solution406().reconstructQueue(people);
        printArr(people);
    }

    /**
     * Reconstruct queue int [ ] [ ].
     *
     * @param people the people
     * @return the int [ ] [ ]
     */
    int[][] reconstructQueue(int[][] people) {
        sortDesc(people);
        putInOrder(people);
        return people;
    }

    /**
     * 将元素正序
     *
     * @param people the people
     */
    void putInOrder(int[][] people) {
        int p = 0;
        while (p < people.length) {
            int k = people[p][1];
            if (p > k) {
                insertArr(people, k, p);
            }
            p++;
        }
    }

    /**
     * 冒泡算法 倒序排序数组
     *
     * @param arr the arr
     * @return the int [ ] [ ]
     */
    int[][] sortDesc(int[][] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 1; j < arr.length; j++) {
                if (arr[j][0] > arr[j - 1][0]) {
                    swap(arr, j, j - 1);
                }
            }
        }
        return arr;
    }

    /**
     * 交换数组 元素i 和 元素j 的位置
     *
     * @param arr the arr
     * @param i   the
     * @param k   the k
     */
    void swap(int[][] arr, int i, int k) {
        int tmp1;
        int tmp2;
        tmp1 = arr[i][0];
        tmp2 = arr[i][1];
        arr[i][0] = arr[k][0];
        arr[i][1] = arr[k][1];
        arr[k][0] = tmp1;
        arr[k][1] = tmp2;
    }

    /**
     * 将 元素j  插入到元素 i 处，
     * 元素i及其后面的元素后移
     *
     * @param arr the arr
     * @param i   the
     * @param j   the j
     */
    void insertArr(int[][] arr, int i, int j) {
        int tmp1 = arr[j][0];
        int tmp2 = arr[j][1];
        int front = i;
        int tail = j;
        while (front < tail) {
            arr[tail][0] = arr[tail - 1][0];
            arr[tail][1] = arr[tail - 1][1];
            tail--;
        }
        arr[front][0] = tmp1;
        arr[front][1] = tmp2;
    }
}
