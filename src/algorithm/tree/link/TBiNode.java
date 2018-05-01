package algorithm.tree.link;

public class TBiNode {
    int val;
    TBiNode left;
    TBiNode right;
    TBiNode(int x) { val = x; }
    
	public void visit(){
		System.out.print("\t"+this.val);
	}

	@Override
	public String toString() {
		return "TBiNode [val=" + val + ", left=" + left + ", right=" + right + "]";
	}
}
