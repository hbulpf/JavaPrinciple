package designpattern.proxy.dynamicproxy;

public class RealSubject implements Subject {

	@Override
	public void doSomething(String str) {
		System.out.println("do Something!-->" + str);
	}

}
