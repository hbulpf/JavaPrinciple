package dev.leetcode.solution;

/**
 *  458. 可怜的小猪
 * 假设有 n 只水桶，猪饮水中毒后会在 m 分钟内死亡，你需要多少猪（y）就能在 p 分钟内找出 “有毒” 水桶？这 n 只水桶里有且仅有一只有毒的桶。
 * 题目难在：喝水方式，如果按照2分法的思路，需要的小猪数至少为 log2(x) , n 较大时, y 也很大
 */
public class Solution458 {
    public static void main(String[] args){
        int res = new Solution458().poorPigs(1000,15,60);
        System.out.println(res); //输出 5
    }

    /**
     * 方法1
     */
    public int poorPigs(int buckets, int minutesToDie, int minutesToTest) {
        int pigs = 0;
        int round = (int) Math.ceil(minutesToTest/minutesToDie)+1;
        pigs =  (int) Math.ceil(Math.log(buckets)/Math.log(round));//计算 logN(x)的方法
        return pigs;
    }

    /**
     * 方法2
     */
    public int poorPigs2(int buckets, int minutesToDie, int minutesToTest) {
        int pigs = 0;
        int round = (int) Math.ceil(minutesToTest/minutesToDie)+1;
        while (Math.pow(round,pigs)<buckets){
            pigs++;
        }
        return pigs;
    }
}
