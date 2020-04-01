
package dev.leetcode.solution;

/**
 * 给定一个二叉树，判断它是否是高度平衡的二叉树。
 * 本题中，一棵高度平衡二叉树定义为：
 * 一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过1。
 * Definition for a binary tree node.
 * public class TreeNode {
 * int val;
 * TreeNode left;
 * TreeNode right;
 * TreeNode(int x) { val = x; }
 *
 *
 * 方法2
 *
 * DFS递归计算树高度，如果左右子树高度差>1, 提前阻断递归过程，
 * 避免多余的递归计算。
 * 最差情况下，递归一遍DFS，
 * O(n) = N;
 * }
 */
public class Solution110Opt1 {

    /**
     * Tree height
     *
     * @param node the node
     * @return the int
     */
    static int treeHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = treeHeight(node.left);
        if (leftHeight < 0) {
            return leftHeight;
        }
        int rightHeight = treeHeight(node.right);
        if (rightHeight < 0) {
            return rightHeight;
        }
        return Math.abs(leftHeight - rightHeight) < 2 ? Math.max(leftHeight, rightHeight) + 1 : -1;
    }

    /**
     * Is balanced
     *
     * @param root the root
     * @return the boolean
     */
    public boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }
        if (treeHeight(root) < 0) {
            return false;
        }
        return true;
    }

}
