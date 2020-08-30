package dev.leetcode.solution;

/**
 * 450. 删除二叉搜索树中的节点
 * https://leetcode-cn.com/problems/delete-node-in-a-bst/
 *
 * @Author: RunAtWorld
 * @Date: 2020/6/16 23:45
 */
public class Solution450 {
    /**
     * 求后继的key
     *
     * @param root
     * @return
     */
    public int successor(TreeNode root) {
        root = root.right;
        while (root.left != null) {
            root = root.left;
        }
        return root.val;
    }

    /**
     * 求前驱的key
     *
     * @param root
     * @return
     */
    public int predecessor(TreeNode root) {
        root = root.left;
        while (root.right != null) {
            root = root.right;
        }
        return root.val;
    }

    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) {
            return null;
        }
        if (key < root.val) {
            root.left = deleteNode(root.left,key);
        } else if (key > root.val) {
            root.right = deleteNode(root.right,key);
        } else {
            if (root.left == null && root.right == null) {
                //叶子节点，删除自身
                root = null;
            } else if (root.right != null) {
                //有右子节点，用后继节点代替自身
                root.val = successor(root);
                root.right = deleteNode(root.right, root.val);
            } else {
                //只有左子节点，用前驱节点代替自身
                root.val = predecessor(root);
                root.left = deleteNode(root.left, root.val);
            }
        }
        return root;
    }
}
