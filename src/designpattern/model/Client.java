package designpattern.model;

/**
 * model method
 * @author foreverlpficloud.com
 *
 */
public class Client {
	public static void main(String[] args) {
		AbstractModel model1 = new ConcreteM1();
		model1.templateRun();
		
		AbstractModel model2 = new ConcreteM2();
		model2.templateRun();
	}
}
