package designpattern.observer.javaObserver;

import java.util.Observable;
import java.util.Observer;

public class ConcreteOberver implements Observer {

	protected void reportBoss(String MQstr) {
		System.out.println("Boss,"+MQstr);
	}
	
	@Override
	public void update(Observable o, Object message) {
		System.out.println("spy get MQ,report to Boss:");
		this.reportBoss(message.toString());
	}

}
