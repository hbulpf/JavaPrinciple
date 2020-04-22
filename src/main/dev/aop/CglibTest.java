package zmqc.iceyung.aoptool;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;

public class CglibTest implements MethodInterceptor {
    private Enhancer enhancer = new Enhancer();
    public Object getProxy(Class clazz){
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("前置代理");
        //通过代理类调用父类中的方法
        Object result = methodProxy.invokeSuper(o, objects);

        System.out.println("后置代理");
        return result;
    }
    public static void main(String[] args) {
        CglibTest proxy = new CglibTest();
        //通过生成子类的方式创建代理类
        OperatorImpl operator = (OperatorImpl)proxy.getProxy(OperatorImpl.class);
        int num = operator.getAllStudentNum();
        System.out.println("学生人数为："+ num);
        System.out.println("operator:"+ operator.getClass());
    }

}
