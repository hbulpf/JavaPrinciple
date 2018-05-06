package designpattern.observer.javaObserver;

import java.util.Observable;

public class ConcreteSubject extends Observable implements Subject {

	@Override
	public void havaBreakfast() {
		System.out.println("I starts to hava breakfast!");
		super.setChanged();
		super.notifyObservers(this.getClass().getName() + " starts to hava breakfast!");
	}

	@Override
	public void havaFun() {
		System.out.println("Subject starts to hava fun!");
		super.setChanged();
		super.notifyObservers(this.getClass().getName() + " starts to hava fun!");
	}
}
