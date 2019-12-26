package dev.lpf.designpattern.adapter.objectAdapter;

/**
 * Adapter Role
 * 
 * @author foreverlpficloud.com
 *
 */
public class Adapter implements ITarget {
	AdapteeBase base;
	AdapteeHome home;
	AdapteeOffice office;
	
	public Adapter(AdapteeBase base,AdapteeHome home,AdapteeOffice office) {
		this.base = base;
		this.home = home;
		this.office = office;
	}
	
	@Override
	public String getUserName() {
		return this.base.getName();
	}
	
	@Override
	public String getHomeAddress() {
		return this.home.getHome();
	}

	@Override
	public String getOfficeName() {
		return this.office.getOffice();
	}
	
	@Override
	public void request() {
		this.base.doSomething();
	}

}
