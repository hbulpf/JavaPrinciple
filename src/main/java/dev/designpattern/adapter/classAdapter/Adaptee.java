package dev.designpattern.adapter.classAdapter;

/**
 * Source Rolex
 * @author foreverlpficloud.com
 *
 */
public class Adaptee {
	private String name;
	//business of old role
	public Adaptee(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void doSomething() {
		System.out.println("I am kind of busy,leave me alone,pls!");
	}
}
