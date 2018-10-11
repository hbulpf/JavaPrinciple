package designpattern.adapter.classAdapter;

public class Target implements ITarget {
	private String username;
	public Target(String name) {
		this.username = name;
	}
	@Override
	public String getUserName() {
		return username;
	}

	@Override
	public void request() {
		System.out.println("If you need help,pls call me!");

	}

}
