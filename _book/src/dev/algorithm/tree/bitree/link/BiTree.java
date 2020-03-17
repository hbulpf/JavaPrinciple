package dev.algorithm.tree.bitree.link;

import dev.algorithm.tree.bitree.TreeNode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BiTree {
	TreeNode root;
    public static void main(String[] args) {
    	int[] arr = {0,1,2,3,4,5,6,7,8,9,10};
		BiTree tree = new BiTree(arr);
		System.out.println("recusive PreOrder:");
		BiTree.PreOrder(tree.root);
		System.out.println();
		System.out.println("PreOrde2:");
		BiTree.PreOrder2(tree.root);
		System.out.println();
		
		
		System.out.println("recusive InOrder:");
		BiTree.InOrder(tree.root);
		System.out.println();
		System.out.println("InOrder2:");
		BiTree.InOrder2(tree.root);
		System.out.println();
		
		System.out.println("recusive PostOrder:");
		BiTree.PostOrder(tree.root);
		System.out.println();
		
		System.out.println("LevelOrder:");
		BiTree.LevelOrder(tree.root);
		System.out.println();
	}
    
    
    public BiTree(int[] arr) {
    	TreeNode[] tree=new TreeNode[arr.length];
		//生成一棵树
    	for(int i=1;i<arr.length;i++) {
    		TreeNode n = new TreeNode(arr[i]);
    		tree[i]=n;
    	}
    	for(int j=1;j<tree.length;j++) {
    		tree[j].left=(j*2>tree.length-1)?null:tree[j*2];
    		tree[j].right=(j*2+1>tree.length-1)?null:tree[j*2+1];
    	}
    	for(int i=1;i<arr.length;i++) {
    		System.out.println(tree[i]);
    	}
    	this.root=tree[1];
	}
    
    
    /**
     * 二叉树先序递归遍历
     */
    public static void PreOrder(TreeNode root){
    	if(root!=null) {
    		root.visit();
    		PreOrder(root.left);
    		PreOrder(root.right);
    	}
    }
    
    /**
     * 二叉树先序非递归遍历
     */
    public static void PreOrder2(TreeNode root){
    	Stack<TreeNode> stack = new Stack<TreeNode>(); //初始化栈
    	TreeNode p = root; //p是遍历指针
    	while(p!=null || !stack.isEmpty()) {
    		if(p!=null) {
    			p.visit();
    			if(p.right!=null)
    				stack.push(p.right);
    			p=p.left;
    		}else {
    			p = stack.pop();	
    		}
    	}
    }
    
    /**
     * 二叉树中序递归遍历
     */
    public static void InOrder(TreeNode root){
    	if(root!=null) {
    		InOrder(root.left);
    		root.visit();
    		InOrder(root.right);
    	}
    }
    
    /**
     * 二叉树中序非递归遍历
     */
    public static void InOrder2(TreeNode root){
    	Stack<TreeNode> stack = new Stack<TreeNode>(); //初始化栈
    	TreeNode p = root; //p是遍历指针
    	while( p!=null || !stack.isEmpty()) {
    		if(p!=null) {
    			stack.push(p);
    			p = p.left;
    		}else {
    			p = stack.pop();
    			p.visit();
    			p = p.right;
    		}	
    	}
    }
    
    /**
     * 二叉树后序递归遍历：需借助栈来实现
     */
    public static void PostOrder(TreeNode root){
    	if(root!=null) {
    		PostOrder(root.left);
    		PostOrder(root.right);
    		root.visit();
    	}
    }
    
    
	/**
	 * 获取二叉树的最小高度
	 * @param root
	 * @return
	 */
	public static int getMinDepth(TreeNode root) {
        if(root == null)
            return 0;
        int l = getMinDepth(root.left);
        int r = getMinDepth(root.right);
        if(l==0||r==0)
            return 1+l+r;
        return l>r?r+1:l+1;     
    }
	
	/**
	 * 二叉树的层次遍历
	 * @param root
	 */
	public static void LevelOrder(TreeNode root) {
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		TreeNode p;
		queue.offer(root);
		while(!queue.isEmpty()) {
			p=queue.poll();
			p.visit();
			if(p.left!=null)
				queue.offer(p.left);
			if(p.right!=null)
				queue.offer(p.right);
		}
	}
}