package designpattern.adapter.classAdapter;

/**
 * Class Adapter
 * @author foreverlpficloud.com
 *
 */
public class Client {

	public static void main(String[] args) {
		ITarget target = new Target("Lili");
		System.out.println(target.getUserName());
		target.request();
		
		ITarget adapter = new Adapter(new Adaptee("Tom Zhang"));
		System.out.println(adapter.getUserName());
		adapter.request();
	}

}
