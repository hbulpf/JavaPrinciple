package dev.lpf.designpattern.model;

public class ConcreteM1 extends AbstractModel {

	@Override
	protected void doSomething() {
		System.out.println("I am " + this.getClass().getName()+",I can do something!");
	}

	@Override
	protected void doAnything() {
		System.out.println("I am " + this.getClass().getName()+",I can do something!");
	}

}
