package dev.lpf.designpattern.observer.myObserver;

/**
 * Concrete Subject
 * @author foreverlpficloud.com
 *
 */
public class ConcreteSubject extends Subject {
	public void doSomething() {
		System.out.println("I did something");
		super.notifyObservers(this.getClass().getSimpleName() + " do something");
	}
}
