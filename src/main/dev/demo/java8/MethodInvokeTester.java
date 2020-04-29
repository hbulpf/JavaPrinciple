package dev.demo.java8;

import java.util.Arrays;
import java.util.List;

/**
 * Java8 方法引用 测试
 */
public class MethodInvokeTester {
    //Supplier是jdk1.8的接口，这里和lamda一起使用了
    public static MethodInvokeTester create(final Supplier<MethodInvokeTester> supplier) {
        System.out.println("create ");
        return supplier.get();
    }

    public static void collide(final MethodInvokeTester MethodInvokeTester) {
        System.out.println("Collided " + MethodInvokeTester.toString());
    }

    public void follow(final MethodInvokeTester another) {
        System.out.println("Following the " + another.toString());
    }

    public void repair() {
        System.out.println("Repaired " + this.toString());
    }

    public static void main(String[] args) {
        //构造器引用：它的语法是Class::new，或者更一般的Class< T >::new
        final MethodInvokeTester methodInvokeTester = MethodInvokeTester.create( MethodInvokeTester::new );
        final List< MethodInvokeTester > MethodInvokeTesters = Arrays.asList( methodInvokeTester );

        //静态方法引用：它的语法是Class::static_method
        MethodInvokeTesters.forEach( MethodInvokeTester::collide );

        //特定类的任意对象的方法引用：它的语法是Class::method
        MethodInvokeTesters.forEach( MethodInvokeTester::repair );

        //特定对象的方法引用：它的语法是instance::method
        final MethodInvokeTester police = MethodInvokeTester.create( MethodInvokeTester::new );
        MethodInvokeTesters.forEach( police::follow );
    }

}
