package dev.designpattern.proxy.dynamicproxy;

import java.lang.reflect.InvocationHandler;

/**
 * the dynamic proxy of some concrete business class
 * @author foreverlpficloud.com
 *
 */
public class SubjectDynamicProxy extends DynamicProxy {
	public static<T>T newProxyInstance(Subject subject) {
		//get ClassCloader
		ClassLoader loader = subject.getClass().getClassLoader();
		//get interfaces
		Class<?>[] classes = subject.getClass().getInterfaces();
		//get handler
		InvocationHandler handler = new MyInvocationHandler(subject);
		//overload newProxyInstance
		return newProxyInstance(loader,classes,handler);
	}
}
