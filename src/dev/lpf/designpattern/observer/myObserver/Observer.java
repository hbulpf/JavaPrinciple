package dev.lpf.designpattern.observer.myObserver;

public class Observer implements IObserver {

	@Override
	public void update(String str) {
		System.out.println("spy get MQ,report to Boss:");
		this.reportBoss(str);
	}
	
	protected void reportBoss(String MQstr) {
		System.out.println("Boss,"+MQstr);
	}

}
