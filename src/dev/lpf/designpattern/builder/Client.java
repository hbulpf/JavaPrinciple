package dev.lpf.designpattern.builder;

public class Client {

	public static void main(String[] args) {
		Director director = new Director();
		director.getProduct1().work();
		System.out.println("----------------");
		director.getProduct2().work();
	}

}
