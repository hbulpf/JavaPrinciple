package dev.designpattern.adapter.objectAdapter;

/**
 * Object Adapter
 * @author foreverlpficloud.com
 *
 */
public class Client {

	public static void main(String[] args) {
		ITarget target = new Target("Lili","Zhongshan Avenue","Alibaba");
		System.out.println(target.getUserName());
		System.out.println(target.getHomeAddress());
		System.out.println(target.getOfficeName());
		target.request();
		
		System.out.println("--------------------");
		ITarget adapter = new Adapter(
				new AdapteeBase("Tom Zhang"),
				new AdapteeHome("Tianhe Square"),
				new AdapteeOffice("Tencent"));
		System.out.println(adapter.getUserName());
		System.out.println(adapter.getHomeAddress());
		System.out.println(adapter.getOfficeName());
		adapter.request();
	}

}
