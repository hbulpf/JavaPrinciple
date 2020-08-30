package dev.designpattern.abstractfactory;

public class Creator1 extends AbstractCreator {

	@Override
	public IProduct createProductA() {
		return new ProductA1();
	}

	@Override
	public IProduct createProductB() {
		return new ProductB1();
	}

	
}
