package algorithm.greedy.bagproblem;

class P{
	public P(int w, int v) {
		this.w = w;
		this.v = v;
		this.avg = v*1.0/w;
	}
	int w;
	int v;
	double avg;
	
	@Override
	public String toString() {
		return "P [w=" + w + ", v=" + v + ", avg=" + avg + "]";
	}
	
	
}
