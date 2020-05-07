package dev.leetcode.solution;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * 3. 无重复字符的最长子串
 * https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/
 *
 * (1) 暴力法 T=O(N^3)
 * (2) 双指针法 T=O(N)
 * @Author: RunAtWorld
 * @Date: 2020/4/28 0:11
 */
public class Solution003 {
    public static void main(String[] args) {
        int res = lengthOfLongestSubstring1("abcabcbb");
        System.out.println(res);

        res = lengthOfLongestSubstring2("pwwkew");
        System.out.println(res);
    }

    /**
     * 暴力法
     *
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring1(String s) {
        int max = 0;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            sb.setLength(0);
            for (int j = i + 1; j <= s.length(); j++) {
                sb.append(s.charAt(j - 1));
                if (uniqueStr(sb.toString())) {
                    max = Math.max(max, j - i);
                }
            }
        }
        return max;
    }

    public static boolean uniqueStr(String str) {
        int len = str.length();
        HashSet set = new HashSet<Character>();
        for (int i = 0; i < len; i++) {
            if (set.contains(str.charAt(i))) {
                return false;
            }
            set.add(str.charAt(i));
        }
        return true;
    }

    /**
     * 双指针法
     *
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring2(String s) {
        int max = 0;
        int rk = -1;
        int len = s.length();
        HashSet uniqueSet = new HashSet<Character>();
        for (int i = 0; i < len; i++) {
            if (i != 0) {
                //这里要去除掉前一个字符，大意大意！！！
                uniqueSet.remove(s.charAt(i-1));
            }
            while (rk + 1 < len && !uniqueSet.contains(s.charAt(rk + 1))) {
                uniqueSet.add(s.charAt(rk + 1));
                rk++;
            }
            max = Math.max(max, rk - i + 1);
            System.out.println(String.valueOf(uniqueSet.stream().collect(Collectors.toList())));
        }
        return max;
    }

    public int lengthOfLongestSubstring(String s) {
        return lengthOfLongestSubstring1(s);
    }
}
