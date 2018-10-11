package designpattern.proxy.dynamicproxy;

import java.lang.reflect.InvocationHandler;

public class Client {
	public static void main(String[] args) {
		//定义主题
		Subject real = new RealSubject();
		//定义Handler
		//InvocationHandler handler = new MyInvocationHandler(real);
		//Subject proxy = DynamicProxy.newProxyInstance(real.getClass().getClassLoader(), real.getClass().getInterfaces(), handler);
		Subject proxy = SubjectDynamicProxy.newProxyInstance(real);
		proxy.doSomething("I am a Real Man,Eat a lot.");
	}
}
