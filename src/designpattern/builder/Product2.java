package designpattern.builder;

public class Product2 extends Product {

	@Override
	public void getFunctions() {
		System.out.println("Product2's function is to cook for youx!");
	}

	@Override
	public void getName() {
		System.out.println("Product2's name is Cooker");
	}

	@Override
	public void sayHi() {
		System.out.println("Cooker Welcome!");
	}

	@Override
	public void stop() {
		System.out.println("I am going to stop!");
	}

}
