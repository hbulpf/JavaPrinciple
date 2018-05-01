package designpattern.factorymethod;

public abstract class Product {
	public void method1() {
		System.out.println("在这里处理Product抽象类的业务逻辑");
	}
	
	//抽象方法
	public abstract void getProductName();
}
