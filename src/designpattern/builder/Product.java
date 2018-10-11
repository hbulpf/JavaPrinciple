package designpattern.builder;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * using Model Method Pattern
 * @author foreverlpficloud.com
 *
 */
public abstract class Product {
	ArrayList<String> sequence;

	public abstract void getFunctions();
	public abstract void getName();
	public abstract void sayHi();
	public abstract void stop();
	public void setSequence(ArrayList<String> _sequence) {
		this.sequence = _sequence; 
	};
	public void work() {
		for(String str:this.sequence) {
			if(str.equals("getFunctions"))
				this.getFunctions();
			if(str.equals("getName"))
				this.getName();
			if(str.equals("sayHi"))
				this.sayHi();
			if(str.equals("stop"))
				this.stop();			
		}
	};
}
