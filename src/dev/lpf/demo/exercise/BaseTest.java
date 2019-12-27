package dev.lpf.demo.exercise;

import java.util.Stack;

public class BaseTest {
	public static void main(String[] args) {
		// printPrime();
		// System.out.println();
		// printPrime2();
		// System.out.println();

//		int[] A = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
//		int[] B = { 2, 9, 8, 7, 6, 5, 4, 3, 1 };
//		int[] C = { 2, 9, 8, 7, 6, 5, 3, 4, 1 };
//		System.out.println(isOutStack(A, B));
//		System.out.println(isOutStack(A, C));
		
		int[] E = {1, 2, 3, 4 };
		System.out.println(kSum(E,2,5));
	}

	/**
	 * 打印素数方法1
	 */
	public static void printPrime() {
		System.out.print(2); // 2是偶数又是奇数
		for (int i = 3; i <= 100; i += 2) { // 只判断奇数
			int j = 0;
			for (j = 2; j < Math.sqrt(i); j++) { // 小于根号下i
				if (i % j == 0)
					break;
			}
			if (j > Math.sqrt(i))
				System.out.print(" " + i);
		}
	}

	/**
	 * 打印素数方法2:暴力破解
	 */
	public static void printPrime2() {
		boolean IS_Prime = true;
		for (int i = 2; i <= 100; i++) {
			int j = 0;
			IS_Prime = true;
			for (j = 2; j <= i / 2; j++) {
				if (i % j == 0) {
					IS_Prime = false;
					break;
				}
			}
			if (i == j || IS_Prime)
				System.out.print(i + " ");
		}
	}

	/**
	 * 给定入栈顺序，给出一组出栈顺序，判断是否满足条件。如入栈 ：12345 出栈：21453
	 * 算法 需要一个栈作为辅助:
	 * 设置两个指针in , out表示需入栈元素和需出栈元素的位置,
	 * 循环判断out是否已经>=需入栈元素序列长度；out已经>=需入栈元素序列长度表示满足条件
	 *   1. 需入栈元素未入栈完毕:
	 *     1.1 需入栈元素和需出栈元素相等: in++,out++
	 *     1.2 需入栈元素和需出栈元素不相等：
	 *       1.2.1 栈空：需入栈元素入栈
	 *       1.2.2 栈不空:如果栈顶元素和需出栈元素相等，出栈并out++;如果栈顶元素和需出栈元素不相等， 需入栈元素入栈;
	 *   2. 需入栈元素已入栈完毕:  
	 *    栈内元素依次出栈，出栈元素与需出栈元素不相等则表示不满足条件。
	 * @param inBound
	 * @param outBound
	 * @return
	 */
	public static boolean isOutStack(int[] inBound, int[] outBound) {
		if (inBound == null || outBound == null)
			return false;

		Stack<Integer> stack = new Stack<Integer>();
		int in = 0; // 进栈的下标
		int out = 0; // 出栈的下标
		while (out != outBound.length) {
			if (in < inBound.length) { // 需入栈元素未入栈完毕
				if (inBound[in] == outBound[out]) { // 需入栈元素和需出栈元素相等
					in++;
					out++;
				} else if (stack.isEmpty()) { // 栈为空
					stack.push(inBound[in++]);
				} else { // 栈不为空
					if (stack.peek() == outBound[out]) { // 栈顶元素与需出栈元素相等
						out++;
						stack.pop();
					} else { // 栈顶元素与需出栈元素不相等
						stack.push(inBound[in++]);
					}
				}
			} else { // 需入栈元素已入栈完毕
				if (stack.pop() == outBound[out++]) // 栈内元素依次出栈
					continue;
				else
					return false;
			}
		}
		return true;
	}
	
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
