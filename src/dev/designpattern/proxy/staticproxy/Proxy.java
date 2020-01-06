package dev.designpattern.proxy.staticproxy;

public class Proxy implements Subject {
	Subject object;
	public Proxy(Subject _object){
		this.object = _object;
	}
	@Override
	public void request() {
		this.before();
		this.object.request();
		this.after();
	}

	private void before() {
		System.out.println("-------before-------");		
	}	
	private void after() {
		System.out.println("-------after--------");
	}
}
