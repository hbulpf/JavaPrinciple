package designpattern.proxy.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {
	// the proxy
	private Object target = null;
	
	public MyInvocationHandler(Object _obj) {
		this.target = _obj;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(this.target, args);
	}

}
