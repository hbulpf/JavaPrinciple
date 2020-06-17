package dev.leetcode.solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 17. 电话号码的字母组合
 * https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/
 * <p>
 * 解法：
 * 1. 递归/回溯：最后一次递归才加入结果列表
 *
 * @Author: RunAtWorld
 * @Date: 2020/6/18 0:23
 */
public class Solution017 {
    Map<String, String> phoneMap = new HashMap<String, String>() {{
        put("2", "abc");
        put("3", "def");
        put("4", "ghi");
        put("5", "jkl");
        put("6", "mno");
        put("7", "pqrs");
        put("8", "tuv");
        put("9", "wxyz");
    }};
    List<String> resList = new ArrayList<>();

    public void backTrack(String combination, String digits) {
        if (digits.length() == 0) {
            //最后一次递归才加入结果列表
            resList.add(combination);
        }else {
            String digit = digits.substring(0, 1);
            String nextDigits = digits.substring(1);
            String letters = phoneMap.get(digit);
            for (char ch : letters.toCharArray()) {
                backTrack(combination + ch, nextDigits);
            }
        }
    }

    public List<String> letterCombinations(String digits) {
        if (digits != null && digits.length() != 0) {
            backTrack("", digits);
        }
        return resList;
    }
}
