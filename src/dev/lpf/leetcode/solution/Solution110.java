package dev.lpf.leetcode.solution;

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
 */
public class Solution110 {

    static int treeHeight(TreeNode node) {
        if(node == null) {
            return 0;
        }
        int leftHeight = 0;
        int rightHeight = 0;
        if (node.left != null) {
            treeHeight(node.left);
        }
        if (node.right != null) {
            treeHeight(node.right);
        }
        return leftHeight > rightHeight ? leftHeight + 1 : rightHeight + 1;
    }

    public boolean isBalanced(TreeNode root) {
        int defference = treeHeight(root.left) - treeHeight(root.right);
        if (Math.abs(defference) > 1) {
            return false;
        }
        return true;
    }

}
