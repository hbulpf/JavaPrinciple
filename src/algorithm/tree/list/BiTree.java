package algorithm.tree.list;

/**
 * 顺序存储的二叉树
 *
 * @param <ElemType>
 */
public class BiTree<ElemType> {
	ElemType[] elements;
	
	public BiTree(ElemType[] elems) {
		this.elements = elems;
	}
	
	/**
	 * 获得节点i和节点j的最近公共祖先
	 * @param i
	 * @param j
	 * @return
	 */
	public ElemType getCommonAncestors(int i,int j) {
		if((elements[i]!=null)&&(elements[j]!=null)) {
			while(i!=j) {
				if(i>j)
					i/=2;
				if(j>i)
					j/=2;
			}
			return elements[i];
		}
		return null;
	}
	
/*	1
	 |-2
		|-4
		|-5
	 |-3
		|-6
		|-7*/
	public static void main(String[] args) {
		Integer[] arr = {0,1,2,3,4,5,6,7,8,9,10};
		
		BiTree tree = new BiTree<Integer>(arr);
		System.out.println(tree.getCommonAncestors(5, 9));
	}
}
