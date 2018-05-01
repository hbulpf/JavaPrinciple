package designpattern.singleton;

/**
 * lazy mode
 * 
 * thread is safe
 */
public class Singleton2 {
	private static Singleton2 instance;
	
	private Singleton2() {}
	
	//add "synchronized" to make thread safe
	public static synchronized Singleton2 getInstance() {
		if(instance == null)
			instance = new Singleton2();
		return instance;
	}
}
