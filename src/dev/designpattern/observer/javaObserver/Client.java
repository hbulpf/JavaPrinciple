package dev.designpattern.observer.javaObserver;

/**
 * Observer Pattern java did
 * @author foreverlpficloud.com
 *
 */
public class Client {

	public static void main(String[] args) {
		ConcreteSubject subject = new ConcreteSubject();
		ConcreteOberver observer = new ConcreteOberver();
		subject.addObserver(observer);
		
		subject.havaBreakfast();
		subject.havaFun();
	}

}
