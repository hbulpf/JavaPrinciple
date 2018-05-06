package designpattern.observer.myObserver;

/**
 * Concrete Subject
 * @author foreverlpficloud.com
 *
 */
public class Minister extends Subject {
	public void doSomething() {
		System.out.println("go to other country");
		super.notifyObservers(this.getClass().getSimpleName() + " went to other country");
	}
}
