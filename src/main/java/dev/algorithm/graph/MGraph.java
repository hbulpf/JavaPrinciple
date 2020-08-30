package dev.algorithm.graph;

public class MGraph<V,E> {
	static boolean visited[];
	V[] vertexs;
	E[][] edges;
	int vexnum,arcnum;
	public MGraph(V[] vertexs,E[][] edges) {
		this.vertexs = vertexs;
		this.edges = edges;
	}
	
	public static MGraph createMGraph(){
		Integer[] vertexs = null;
		Integer[][] edges = null;
		MGraph<Integer, Integer> mgraph = new MGraph<Integer,Integer>(vertexs, edges);
		return null;
	}
	
	/**
	 * 广度优先搜索
	 * @param G
	 */
	public static void BFS(MGraph G){
		
	}
	
	
	/**
	 * 深度优先搜索
	 * @param G
	 */
	public static void DFS(MGraph G){
		
	}
}
