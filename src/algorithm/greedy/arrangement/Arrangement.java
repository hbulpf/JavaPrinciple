package algorithm.greedy.arrangement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 活动安排问题
 *
 * 设有n个活动的集合E={1,2,…,n}，其中每个活动都要求使用同一资源，如演讲会场等，而在同一时间内只有一个活动能使用这一资源。
 * 每个活动i都有一个要求使用该资源的起始时间si和一个结束时间fi,且si <fi 。要求设计程序，使得安排的活动最多。n=11
 * https://www.jianshu.com/p/50f1d4e0555c
11
1	4
3	5
0	6
5	7
3	8
5	9
6	10
8	11
8	12
2	13
12	14
 *算法：
 */
public class Arrangement {
	public static void main(String[] args) {
		method1();
	}
	
	public static void method1() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		Activity[] acs = new Activity[n];
		for(int i=0;i<n;i++){
			acs[i]=new Activity(sc.nextInt(),sc.nextInt());
		}
		Arrays.sort(acs);
		for(Activity a:acs)
			System.out.println(a);
		int[] arr = new int[n]; //记录活动序号
		int count=0; //计数活动数
		arr[++count]=0;
		int i=0,j = 0; //i记录当前活动序号，j记录下一活动序号
		for(j=arr[count]+1;j<n;j++) { 
			if(acs[i].end <= acs[j].start) {
				arr[++count] = j;
				i=j;
			}			
		}
		System.out.println(count);
		for(int k=1;k<=count;k++) {
			System.out.println(acs[arr[k]]);
		}
	}
	
	public static void method2() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		Activity[] acs = new Activity[n];
		for(int i=0;i<n;i++){
			acs[i]=new Activity(sc.nextInt(),sc.nextInt());
		}
		Arrays.sort(acs);
		int time = 0; //当前活动结束时间
		int[] arr = new int[n]; //记录活动序号
		int count=0; //计数活动数
		arr[++count]=0;
		time=acs[0].end;
		for(int i=1;i<n;i++) {
			if(time<=acs[i].start) {
				arr[++count]=i; //下一活动序号
				time=acs[i].end; //将time设置当前活动结束时间
			}
		}
		System.out.println(count);
		for(int k=1;k<=count;k++) {
			System.out.println(acs[arr[k]]);
		}
	}
}
