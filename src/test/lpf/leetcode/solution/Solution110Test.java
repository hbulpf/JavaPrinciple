
package test.lpf.leetcode.solution;

import dev.lpf.leetcode.solution.Solution110;
import dev.lpf.leetcode.solution.TreeNode;
import test.BaseCase;

/**
 * 功能描述
 *
 */
public class Solution110Test extends BaseCase {

    public void test011() {
        Solution110 solution = new Solution110();
        TreeNode n0 = new TreeNode(1);
        TreeNode n1 = new TreeNode(2);
        TreeNode n2 = new TreeNode(3);
        TreeNode n3 = new TreeNode(2);
        TreeNode n4 = new TreeNode(5);
        TreeNode n5 = new TreeNode(6);
        TreeNode n6 = new TreeNode(2);
        n0.setLeft(n1);
        n0.setRight(n2);
        n1.setLeft(n3);
        n1.setRight(n4);
        n2.setLeft(n5);
        n2.setRight(n6);
        boolean flag = solution.isBalanced(n0);
        assertEquals(true, flag);
    }

    public void test012() {
        Solution110 solution = new Solution110();
        TreeNode n0 = new TreeNode(1);
        TreeNode n2 = new TreeNode(3);
        TreeNode n5 = new TreeNode(2);
        TreeNode n6 = new TreeNode(2);
        n0.setRight(n2);
        n2.setLeft(n5);
        n2.setRight(n6);
        boolean flag = solution.isBalanced(n0);
        assertEquals(false, flag);
    }

    public void test013() {
        Solution110 solution = new Solution110();
        TreeNode n0 = new TreeNode(1);
        TreeNode n1 = new TreeNode(2);
        TreeNode n2 = new TreeNode(2);
        TreeNode n3 = new TreeNode(3);
        TreeNode n4 = new TreeNode(3);
        TreeNode n7 = new TreeNode(4);
        TreeNode n8 = new TreeNode(4);
        n0.setLeft(n1);
        n0.setRight(n2);
        n1.setLeft(n3);
        n1.setRight(n4);
        n3.setLeft(n7);
        n3.setRight(n8);
        boolean flag = solution.isBalanced(n0);
        assertEquals(false, flag);
    }

    public void test014() {
        Solution110 solution = new Solution110();
        TreeNode n0 = new TreeNode(1);
        TreeNode n1 = new TreeNode(2);
        TreeNode n2 = new TreeNode(2);
        TreeNode n3 = new TreeNode(3);
        TreeNode n4 = new TreeNode(3);
        TreeNode n7 = new TreeNode(4);
        TreeNode n8 = new TreeNode(4);
        n0.setLeft(n1);
        n0.setRight(n2);
        n1.setLeft(n3);
        n1.setRight(n4);
        n3.setLeft(n7);
        n3.setRight(n8);
        boolean flag = solution.isBalanced(n0);
        assertEquals(false, flag);
    }
}
