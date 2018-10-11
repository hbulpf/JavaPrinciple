package designpattern.model;

public abstract class AbstractModel {
	
	/**
	 * framework model
	 */
	public void templateRun() {
		doAnything();
		doSomething();
	}
	
	protected abstract void doSomething();
	protected abstract void doAnything();
}
