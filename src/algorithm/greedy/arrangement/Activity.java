package algorithm.greedy.arrangement;

/**
 * 活动
 *
 */
public class Activity implements Comparable<Activity>{
	int start;
	int end;
	
	@Override
	public int compareTo(Activity o) {
		return this.end - o.end;
	}

	
	
	public Activity(int start, int end) {
		super();
		this.start = start;
		this.end = end;
	}



	@Override
	public String toString() {
		return "Activity [start=" + start + ", end=" + end + "]";
	}
	
}
