package algorithm.greedy.bagproblem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * 背包问题
 * 问题描述：有一个背包，背包容量是M=150。有n个物品n=7，物品可以分割成任意大小。要求尽可能让装入背包中的物品总价值最大，但不能超过总容量。
 * https://www.jianshu.com/p/50f1d4e0555c
7	150
35	10
30	40
60	30
50	50
40	35
10	40
25	30
 *
 */
public class BagProblem {	
	public static void main(String[] args) {
		int n,m;
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		m = sc.nextInt();
		int maxValue=0;
		List<P> list = new ArrayList<P>();
		int i=0;
		while(i<n) {
			list.add(new P(sc.nextInt(),sc.nextInt()));
			i++;
		}
		Comparator<P> comparator = new Comparator<P>() {
			@Override
			public int compare(P o1, P o2) {
				if((o1.avg-o2.avg) < 0)
					return 1;
				if((o1.avg-o2.avg) == 0)
					return 0;
				else
					return -1;
			}	
		};
		list.sort(comparator);
		P p = null;
		i=0;
		for(P pa:list)
			System.out.println(pa);
		System.out.println("result:");
		while(i<n) {
			p = list.get(i++);
			if(p.w<=m) {
				m-=p.w;
				maxValue += p.v;
				System.out.println(p);
			}else {
				continue;
			}
		}
		System.out.println(maxValue);
	}
}
