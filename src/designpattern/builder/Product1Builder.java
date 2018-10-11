package designpattern.builder;

import java.util.ArrayList;

public class Product1Builder extends Builder {

	@Override
	public void setPart(ArrayList<String> _sequence) {
		this.sequence=_sequence;
	}

	@Override
	public Product getProduct() {
		Product product = new Product1();
		product.setSequence(this.sequence);
		return product;
	}

}
