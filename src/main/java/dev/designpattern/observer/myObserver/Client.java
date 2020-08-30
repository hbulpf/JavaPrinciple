package dev.designpattern.observer.myObserver;

/**
 * Observer Pattern I did
 * @author foreverlpficloud.com
 *
 */
public class Client {

	public static void main(String[] args) {
		ConcreteSubject subject = new ConcreteSubject();
		Observer observer = new Observer();
		subject.addObserver(observer);
		subject.doSomething();
		
		Minister minister = new Minister();
		minister.addObserver(observer);
		minister.doSomething();
	}

}
