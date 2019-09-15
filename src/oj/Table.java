package oj;


import java.util.Scanner;

/**
 * 13. Table
 * Mysql查询结果图
 */
public class Table {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n= sc.nextInt();
        int m=sc.nextInt();
        for(int i=0;i<=2*n;i++){
            for(int j=0;j<=2*m;j++){
                if (i % 2 == 0) {
                    if(j%2==0){
                        System.out.print("+");
                    }else{
                        System.out.print("---");
                    }
                }else{
                    if(j%2==0){
                        System.out.print("|");
                    }else{
                        System.out.print("   ");
                    }
                }
            }
            System.out.println();
        }
    }
}
