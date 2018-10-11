package designpattern.abstractfactory;

/**
 * Abstract Factory: hard to extend to the product family,easy to extend the product levels
 * For a instance, we're going to build Text Editor and Image Editor for Windows,Linux,MacOS,Android, it is suitable to use Abstract Factory.
 * When we've build Text Editor and Image Editor on Windows, we can extend these to other platforms using Abstract Factory
 * @author foreverlpficloud.com
 *
 */
public class Client {

	public static void main(String[] args) {
		AbstractCreator creator1 = new Creator1();
		AbstractCreator creator2 = new Creator2();
		
		IProduct productA1 = creator1.createProductA();
		productA1.setName("产品A1");
		productA1.getName();
		IProduct productA2 = creator1.createProductB();
		productA1.setName("产品A2");
		productA1.getName();
		IProduct prodcutB1 = creator2.createProductA();
		productA1.setName("产品B1");
		productA1.getName();
		IProduct productB2 = creator2.createProductB();
		productA1.setName("产品B2");
		productA1.getName();
	}

}
