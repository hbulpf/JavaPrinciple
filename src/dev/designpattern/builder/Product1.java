package dev.designpattern.builder;

public class Product1 extends Product {

	@Override
	public void getFunctions() {
		System.out.println("Product1's function is to speak for you!");
	}

	@Override
	public void getName() {
		System.out.println("Product1's name is Speaker");
	}

	@Override
	public void sayHi() {
		System.out.println("Hi~I'am XiaoNa");
	}

	@Override
	public void stop() {
		System.out.println("I want to stay with you for a longer time.");
	}

}
