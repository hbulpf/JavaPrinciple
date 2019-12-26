package dev.lpf.designpattern.adapter.classAdapter;

/**
 * Adapter Role
 * 
 * @author foreverlpficloud.com
 *
 */
public class Adapter extends Adaptee implements ITarget {
	public Adapter(String name) {
		super(name);
	}
	
	public Adapter(Adaptee adaptee) {
		this(adaptee.getName());
	}
	
	@Override
	public String getUserName() {
		return this.getName();
	}

	@Override
	public void request() {
		doSomething();
	}

}
