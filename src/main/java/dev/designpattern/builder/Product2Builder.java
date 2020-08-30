package dev.designpattern.builder;

import java.util.ArrayList;

public class Product2Builder extends Builder {

	@Override
	public void setPart(ArrayList<String> _sequence) {
		this.sequence=_sequence;
	}

	@Override
	public Product getProduct() {
		Product product = new Product2();
		product.setSequence(this.sequence);
		return product;
	}

}
