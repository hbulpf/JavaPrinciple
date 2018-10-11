package designpattern.factorymethod;

public class Client {
	public static void main(String[] args) {
		Creator creator = new ConcreteCreator();
		Product product1 = creator.createProduct(ConcreteProduct1.class);
		Product product2 = creator.createProduct(ConcreteProduct2.class);
		product1.getProductName();
		product2.getProductName();
	}
}
