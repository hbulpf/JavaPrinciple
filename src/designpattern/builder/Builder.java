package designpattern.builder;

import java.util.ArrayList;

public abstract class Builder {
	ArrayList<String> sequence;
	public abstract void setPart(ArrayList<String> _sequence);
	public abstract Product getProduct();
}
