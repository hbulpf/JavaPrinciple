package designpattern.abstractfactory;

public class Creator2 extends AbstractCreator {

	@Override
	public IProduct createProductA() {
		return new ProductA2();
	}

	@Override
	public IProduct createProductB() {
		return new ProductB2();
	}

}
