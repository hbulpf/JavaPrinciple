package exercise;

public class BaseTest {
	public static void main(String[] args) {
		printPrime();
		System.out.println();
		printPrime2();
		System.out.println();
	}
	
	/**
	 * 打印素数方法1
	 */
	public static void printPrime(){
		System.out.print(2); //2是偶数又是奇数
	    for(int i=3; i<=100; i+=2){ //只判断奇数
	        int j = 0;
	        for(j=2; j<Math.sqrt(i); j++){ //小于根号下i
	            if(i%j==0)
	                break;
	        }
	        if(j>Math.sqrt(i))
	            System.out.print(" " + i);
	    }
	}
	
	/**
	 * 打印素数方法2:暴力破解
	 */
	public static void printPrime2(){
		boolean IS_Prime = true;
	    for(int i=2; i<=100; i++){ 
	        int j = 0;
	        IS_Prime = true;
	        for(j=2; j<=i/2; j++){
	            if(i%j==0) {
	            	IS_Prime = false;
	                break;
	            }
	        }
	        if(i==j || IS_Prime)
	            System.out.print(i + " " );
	    }
	}
	
	
}
