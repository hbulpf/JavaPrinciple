package dev.lpf.oj;

import java.util.Arrays;
import java.util.List;

public class Prime {
    /**
     * 求小于MAX的所有素数
     * 使用 筛选法 
     */    
    public static List<Integer> getPrimes(int MAX){
        Integer[] isPrime = new Integer[MAX];
        for(int i=0;i<MAX;i++){
            if(i==0 || i==1){
                isPrime[i] = 0;
            }else{
                isPrime[i] = 1;
            }
        }

        for(int i=2;i*i<MAX;i++){
            if(isPrime[i]==1){
                for(int j=i;j*i<MAX;j++){
                    isPrime[j*i] = 0;
                }
            }
        }
        return Arrays.asList(isPrime);
    }

    public static void main(String...args){
        long start = System.currentTimeMillis();
        List<Integer> pList = getPrimes(45678912);
        int j = 0;
        for(int i=0;i<pList.size();i++){
            if(pList.get(i)==1){
                j++;
            }
        }
        System.out.println("\ntotal prime numbers: "+j);
        long end = System.currentTimeMillis();
        System.out.println("time consumed: "+(end-start));
    }
    
        /**
         * 常规方法判断是否素数 ， 效率一般
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
