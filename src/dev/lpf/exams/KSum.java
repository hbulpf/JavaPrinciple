package dev.lpf.exams;

/**
 * 给定n个不同的正整数，整数k（k < = n）以及一个目标数字。在这n个数里面找出K个数，使得这K个数的和等于目标数字，求问有多少种方案？
 * 给出[1,2,3,4]，k=2， target=5，[1,4] and [2,3]是2个符合要求的方案
 */
public class KSum {

    /**
     * 给定n个不同的正整数，整数k（k < = n）以及一个目标数字。在这n个数里面找出K个数，使得这K个数的和等于目标数字，求问有多少种方案？
     * 给出[1,2,3,4]，k=2， target=5，[1,4] and [2,3]是2个符合要求的方案
     */
    public static int kSum(int[] A, int k, int target) {
        int[][][] dp=new int[A.length+1][k+1][target+1];
        for(int i=1;i<=A.length;i++){
            for(int j=1;j<=i&&j<=k;j++){
                for(int t=0;t<=target;t++){
                    int x=0;
                    if(t>A[i-1]){
                        x=dp[i-1][j-1][t-A[i-1]];
                    }
                    int y=dp[i-1][j][t];
                    int z=0;
                    if(t==A[i-1]){
                        z++;
                    }
                    dp[i][j][t]=x+y+z;
                }
            }
        }
        return dp[A.length][k][target];
    }
}
