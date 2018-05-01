package designpattern.proxy.dynamicproxy;

import designpattern.proxy.staticproxy.Proxy;
import designpattern.proxy.staticproxy.RealSubject;
import designpattern.proxy.staticproxy.Subject;

public class Client {
	public static void main(String[] args) {
		Subject real = new RealSubject();
		Subject proxy = new Proxy(real);
		proxy.request();
	}
}
