package dev.lpf.designpattern.builder;

import java.util.ArrayList;

public class Director {
	private ArrayList<String> sequence = new ArrayList<String>();
	Builder product1Builder = new Product1Builder(); //可以批量生产
	Builder product2Builder = new Product2Builder(); //可以批量生产
	
	public Product getProduct1() {
		this.sequence.clear();
		this.sequence.add("sayHi");
		this.sequence.add("getName");
		this.sequence.add("getFunctions");
		this.sequence.add("stop");
		product1Builder.setPart(this.sequence);
		return product1Builder.getProduct();
	}
	
	public Product getProduct2() {
		this.sequence.clear();
		this.sequence.add("getName");
		this.sequence.add("getFunctions");
		this.sequence.add("sayHi");
		this.sequence.add("stop");
		product2Builder.setPart(this.sequence);
		return product2Builder.getProduct();
	}
}
