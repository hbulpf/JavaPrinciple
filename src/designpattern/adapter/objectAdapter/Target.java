package designpattern.adapter.objectAdapter;

public class Target implements ITarget {
	protected  String username;
	protected  String homeAddress;
	protected  String offceName;
	
	public Target(String username, String homeAddress, String offceName) {
		super();
		this.username = username;
		this.homeAddress = homeAddress;
		this.offceName = offceName;
	}
	@Override
	public String getUserName() {
		return username;
	}

	@Override
	public String getHomeAddress() {
		return homeAddress;
	}
	@Override
	public String getOfficeName() {
		return offceName;
	}
	
	@Override
	public void request() {
		System.out.println("If you need help,pls call me!");

	}
}
