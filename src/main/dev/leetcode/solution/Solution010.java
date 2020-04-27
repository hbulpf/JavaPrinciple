package dev.leetcode.solution;

/**
 * 10. 正则表达式匹配
 * https://leetcode-cn.com/problems/regular-expression-matching/
 *
 *  1. 递归法求解
 *  2. dp算法
 * @Author: RunAtWorld
 * @Date: 2020/4/7 23:26
 */
public class Solution010 {

    public static void main(String[] args) {
        String s = "aa", p = "a";
        Solution010 solution010 = new Solution010();
        System.out.println(solution010.isMatch(s, p));  //false
        s = "aa";
        p = "a*";
        System.out.println(solution010.isMatch(s, p)); //true
        s = "ab";
        p = ".*";
        System.out.println(solution010.isMatch(s, p)); //true
        s = "aab";
        p = "c*a*b";
        System.out.println(solution010.isMatch(s, p));  //true
        s = "mississippi";
        p = "mis*is*p*.";
        System.out.println(solution010.isMatch(s, p));  //false
    }

    /**
     * 递归法求解
     * 时间复杂度和空间复杂度较为难算
     * T=O((S+P)*2^(T+P/2))
     * O=O((S+P)*2^(T+P/2))
     *
     * @param s
     * @param p
     * @return
     */
    public boolean isMatch2(String s, String p) {
        if (p.isEmpty()) {
            return s.isEmpty();
        }
        //首字符匹配结果
        boolean firstMatch = !s.isEmpty() && (p.charAt(0) == s.charAt(0) || p.charAt(0) == '.');

        if (p.length() >= 2 && p.charAt(1) == '*') {
            // p的长度>=2并第2个字符是 *
            // 情况1. p的第2个字符为*,p[0]*和s前面字符不匹配,p向后检查
            // 情况2. p的首字符匹配*和s首字符匹配,s向后再次匹配p
            return isMatch(s, p.substring(2)) || firstMatch && isMatch(s.substring(1), p);
        } else {
            //其他情况
            // 情况1. 首字符不匹配
            // 情况2. p.length()<2,但首字符匹配
            return firstMatch && isMatch(s.substring(1), p.substring(1));
        }
    }

    /**
     * dp算法求解
     *
     * @param s
     * @param p
     * @return
     */
    public boolean isMatch(String s, String p) {
        return true;
    }
}
