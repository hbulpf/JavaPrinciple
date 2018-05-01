package designpattern.proxy.staticproxy;

public class RealSubject implements Subject{

	@Override
	public void request() {
		System.out.println("I am RealSubject,I want to deal with something!");
	}

}
