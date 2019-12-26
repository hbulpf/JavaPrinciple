package dev.lpf.designpattern.abstractfactory;

public class ProductB1 extends AbstractProductB {

	@Override
	public String getName() {
		System.out.println("the product name of "+this.getClass().getName()+" is:"+this.name);
		return this.name;
	}

	@Override
	public void setName(String name) {
		System.out.println("set the product name of "+this.getClass().getName()+" to:" + name);
		this.name=name;
	}

}
