package algorithm.DP;


/*如果我们有面值为1元、3元和5元的硬币若干枚，如何用最少的硬币凑够11元？ 
(表面上这道题可以用贪心算法，但贪心算法无法保证可以求出解，比如1元换成2元的时候)*/

public class Coins {
	public static void main(String[] args) {
		outputLIS();

		
	}
	
	public void coins() {
		
	}
	
	/*	一个序列有N个数：A[1],A[2],…,A[N]，求出最长非降子序列的长度。LIS：longest increasing subsequence*/
	/**
	 * 求出最长非降子序列的长度。
	 * 状态转移方程为：d(i) = max{1, d(j)+1},其中j<i,A[j]<=A[i]
	 * @param arr
	 */
	public static int[] getLIS(int[] arr) {
		int[] d=new int[arr.length]; 
		int len=1; //最长子序列的长度
		for(int i=0;i<arr.length;i++) {
			d[i]=1;
			for(int j=0;j<i;j++) {
				if(arr[i]>=arr[j] && d[i]<d[j]+1) {
					d[i] = d[j] +1;
				}
			}
			if(d[i]>len) {
				len = d[i];
			}
		}
		return d;
	}
	
	public static void outputLIS(){
		int[] arr= {5,3,4,8,6,7};
		int[] dp = getLIS(arr);
		int max=0,index = 0;
		for(int i = 0;i<arr.length;i++) {
			System.out.println(dp[i]);
			if(max<dp[i]) {
				max = dp[i];
				index = i;
			}	
		}
		System.out.println(index +":" +max);	
	}
	
}
