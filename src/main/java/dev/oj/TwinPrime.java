package dev.oj;

import java.util.Scanner;

/**
 * 孪生素数问题
 */
class TwinPrime {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long tmp = 2;
        long count = 0;
        while (sc.hasNextInt()) {
            long n = sc.nextInt();
            for (long i = 2; i <= n; i++) {
                if (isPrime(i)) {
                    if (i - tmp == 2) {
                        count++;
                    }
                    tmp = i;
                }
            }
            System.out.println(count);
            count = 0;
        }
    }

        /**
         * 常规方法判断是否素数
         * @param n
         * @return
         */
        public static boolean isPrime (long n){
            for (long i = 2; i*i <= n; i++) {
                if (n % i == 0) {
                    return false;
                }
            }
            return true;
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
