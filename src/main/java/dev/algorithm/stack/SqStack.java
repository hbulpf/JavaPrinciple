package dev.algorithm.stack;

/**
 * 顺序栈
 *
 */
public class SqStack<Elem> {
	final int Maxsize = 10000000;
	Elem[] data;
	int top;
	
	SqStack(Elem[] elemes){
		this.data = elemes;
		init();
	}
	
	/**
	 * 栈的初始化
	 */
	private void init(){
		this.top=-1;
	}
	
	/**
	 * 判空
	 * @return
	 */
	public boolean IsEmpty() {
		if(this.top==-1)
			return true;
		else
			return false;
	}
	
	/**
	 * 进栈
	 * @param elem
	 * @return
	 */
	public boolean push(Elem elem) {
		if(this.top==Maxsize -1)
			return false;
		this.data[++this.top]=elem;
		return true;
	}
	
	/**
	 * 出栈
	 * @return
	 */
	public Elem pop() {
		if(this.top==-1)
			return null;
		return this.data[this.top--];
	}
	
	/**
	 * 取栈顶元素
	 * @return
	 */
	public Elem getTop() {
		if(this.top==-1)
			return null;
		return this.data[this.top];
	}
	
}
