package designpattern.singleton;

/**
 * 
 * 在实际应用中，线程池、缓存、日志对象、对话框对象常被设计成单例，
 * 总之，选择单例模式就是为了避免不一致状态
 * @author lipengfei
 *
 */
public class Client {
	public static void main(String[] args) {
		EasySingleton e = EasySingleton.INSTANCE2;
		System.out.println(e.name());
	}
}
