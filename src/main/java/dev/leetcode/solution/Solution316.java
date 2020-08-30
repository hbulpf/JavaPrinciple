
package dev.leetcode.solution;

import java.util.Arrays;

/**
 * 316. 去除重复字母
 * https://leetcode-cn.com/problems/remove-duplicate-letters/
 *
 * leetcode 316. 去除重复字母
 * 贪心
 * 给定一个仅包含小写字母的字符串，去除字符串中重复的字母，使得每个字母只出现一次。需保证返回结果的字典序最小（要求不能打乱其他字符的相对位置）。
 * 示例 1:
 * 输入: “bcabc”
 * 输出: “abc”
 * 示例 2:
 * 输入: “cbacdcbc”
 * 输出: “acdb”
 *
 */
class Solution316 {
    /**
     * The Ct.
     */
    public int[] ct;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        System.out.println(new Solution316().removeDuplicateLetters1("cbacdcbc"));
    }

    /**
     * Remove duplicate letters
     * 方法1
     * 思路：先记录每个字母的出现的次数，开个26的数组。
     * 每次贪心，我们就找当前字符串最小的数，如果遇到某个字符的个数为1的话，
     * 那么我们就跳出来，把当前遍历到的最小的字符取出来，接着从pos+1的位置
     * 切断字符串并且删除切断字符串中出现的当前最小字符，
     * 继续对其进行搜索。
     * 
     * @param s the s
     * @return the string
     */
    public String removeDuplicateLetters1(String s) {
        this.ct = new int[26];
        return solve(s);
    }

    /**
     * Solve string.
     *
     * @param s the s
     * @return the string
     */
    public String solve(String s) {
        if (s == null || s.equals("")) {
            return "";
        }
        Arrays.fill(this.ct, 0);
        for (char cur : s.toCharArray()) {
            ++this.ct[cur - 'a'];
        }
        int i = 0;
        int pos = 0;
        for (; i < s.length(); ++i) {
            if (s.charAt(i) < s.charAt(pos)) {
                pos = i;
            }
            --this.ct[s.charAt(i) - 'a'];
            if (this.ct[s.charAt(i) - 'a'] == 0) {
                break;
            }
        }
        return s.charAt(pos) + solve(s.substring(pos + 1).replace("" + s.charAt(pos), ""));
    }

}
