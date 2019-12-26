package dev.lpf.designpattern.singleton;

/**
 * hungry mode
 *
 */
public class Singleton1 {
	//initialize when class is loaded
	private static final Singleton1 instance = new Singleton1();
	
	private Singleton1() {}
	
	public static Singleton1 getInstance() {
		return instance;
	}
	
	public static void doSomething() {
		System.out.println("Singleton in hungry mode do Something");
	}
}
