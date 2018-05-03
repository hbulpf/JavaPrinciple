package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringRes {
	public static void main(String[] args) {
		System.out.println(StringRes.myTrim("    ")+"ss");
		System.out.println(StringRes.myTrim("   232ssass   "));
		System.out.println(StringRes.myTrim("Sdsg"));
		
		System.out.println(StringRes.reverseString("abcdefghi", 2, 7));
		System.out.println(StringRes.getTimes("asdgfasdsasds", "asd"));
		
		List<String> ls =StringRes.getMaxSubString("qwweeisnglnxassn9sn223nslng","23nsln23nslsoii8qwweei");
		for(String s : ls)
			System.out.println(s);
		
		System.out.println(StringRes.sort("qwweeisnglnxassn9sn223nslng"));
	}
	
	//模拟trim()
	public static String myTrim(String str){
		int start = 0;
		int end = str.length();
		while(start<end && str.charAt(start)==' '){
			start ++;
		}
		while(start<end && str.charAt(end-1)==' '){
			end--;
		}
		
		String substr = str.substring(start, end);
		return substr;
	}
	
	//将一个字符串指定的位置进行反转
	public static String reverseString(String str,int start,int end){
		char[] c = str.toCharArray();	//字符串转换为字符数组
		for(int i=start,j=end;i<j;i++,j--){
			char temp = c[j];
			c[j] = c[i];
			c[i] = temp;
		}
		return new String(c);
	}
	
	//获取一个字符串在另一个字符串中出现的次数
	public static int getTimes(String str,String substr){
		int count = 0;
		int record=0;
		while((record=str.indexOf(substr))!=-1){
			count++;
			str =str.substring(record+substr.length());
		}
		return count;		
	}
	
	//获取两个字符串中最大的相同子串，可能会有多个子串
	/*	思路：短的字符串minStr,长的字符串maxStr,
		取minStr的长度len,	看minStr是否在maxStr中，
		依次减小len,看minStr中长度为len的子串是否在maxStr中*/
	public static List<String> getMaxSubString(String str1,String str2){
		String maxStr = (str1.length()>str2.length())?str1:str2;
		String minStr = (str1.length()<str2.length())?str1:str2;
		
		int len = minStr.length();
		List<String> list = new ArrayList();
		for(int i=0;i<len;i++){
			for(int x=0,y=len-i;y<=len;x++,y++){
				String str = minStr.substring(x, y);
				if(maxStr.contains(str)){
					list.add(str);
				}
			}
			if(list.size()!=0)
				return list;
		}
		return null;
	}
	
	//对字符串中的字符进行自然排序
	public static String sort(String str){
		char[] c = str.toCharArray();
		Arrays.sort(c);
		return new String(c);
	}
	

	
}
