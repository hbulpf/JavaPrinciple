package designpattern.prototype;

/**
 * 原型模式的一般类，实现Cloneable接口，覆盖clone()方法
 * @author foreverlpficloud.com
 *
 */
public class PrototypeClass implements Cloneable {

	@Override
	protected PrototypeClass clone() throws CloneNotSupportedException {
		return (PrototypeClass) super.clone();
	}
}
