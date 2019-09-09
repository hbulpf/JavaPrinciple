package dev.designpattern.adapter.objectAdapter;

/**
 * Source Role 1
 * @author foreverlpficloud.com
 *
 */
public class AdapteeBase {
	private String name;
	//business of old role
	public AdapteeBase(String name) {
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
