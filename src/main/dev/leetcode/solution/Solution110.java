
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
 * }
 *
 * 暴力破解
 * 计算子树高度，判断左右两棵子树高度之差
 * 注意点:
 * 1. 左右子树只有单分支的情况
 * 2. node 为空的情况
 */
public class Solution110 {

    /**
     * Tree height
     * 求左右子树高度
     * 
     * @param node the node
     * @return the int
     */
    static int treeHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = treeHeight(node.left);
        int rightHeight = treeHeight(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
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
        return Math.abs(treeHeight(root.left) - treeHeight(root.right)) > 1 ? false : isBalanced(root.left)&&isBalanced(root.right);
    }

}

