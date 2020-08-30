package dev.oj;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 改进后的孪生素数问题解答:增加空间复杂度，减小时间复杂度
 */
class TwinPrime2 {
    public static int MAX = 99999999;
    public static List isPrimeArr = new ArrayList<Boolean>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long count = 0;
        buildPrimes();
        while (sc.hasNextInt()) {
            long n = sc.nextInt();
            for (int i = 2; i + 2 <= n; i++) {
                if((Boolean) isPrimeArr.get(i)&& (Boolean) isPrimeArr.get(i+2)){
                    count++;
                }
            }
            System.out.println(count);
            count = 0;
        }
    }

    /**
     * 得到素数列表
     */
    private static void buildPrimes() {
        for(int k=0;k<MAX;k++){
            isPrimeArr.add(k,false);
        }
        for (int i = 0; i < MAX; i++) {
            if (i == 0 || i == 1) {
                isPrimeArr.set(i,false);
            }
            else {
                isPrimeArr.set(i,true);
            }
        }

        for (int i = 2; i * i < MAX; i++) {
            if ((Boolean) isPrimeArr.get(i)) {
                for (int j = i; j * i < MAX; j++) {
                    isPrimeArr.set(j * i,false);
                }
            }
        }
    }
}
/*
in:
10
100
out:
2
8
*/
