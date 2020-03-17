package dev.algorithm.tree.bitree;

public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;
    public TreeNode(int x) { val = x; }
    
	public void visit(){
		System.out.print("\t"+this.val);
	}

	@Override
	public String toString() {
		return "TBiNode [val=" + val + ", left=" + left + ", right=" + right + "]";
	}
}
