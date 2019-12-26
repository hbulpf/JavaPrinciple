package dev.lpf.designpattern.observer.myObserver;

import java.util.Vector;

public abstract class Subject {
	private Vector<IObserver> observers = new Vector<IObserver>();
	//add observer
	public void addObserver(IObserver o) {
		this.observers.add(o);
	}
	//delete observer
	public void delObserver(IObserver o) {
		this.observers.remove(o);
	}
	//notify all observers
	public void notifyObservers(String str) {
		for(IObserver o:this.observers) {
			o.update(str);
		}
	}
}
