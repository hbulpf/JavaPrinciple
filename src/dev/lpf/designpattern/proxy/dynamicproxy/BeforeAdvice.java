package dev.lpf.designpattern.proxy.dynamicproxy;

/**
 * 前置通知
 * @author foreverlpficloud.com
 *
 */
public class BeforeAdvice implements IAdvice{
	public void exec() {
		System.out.println("I am before Advice,I was executed!");
	}
}