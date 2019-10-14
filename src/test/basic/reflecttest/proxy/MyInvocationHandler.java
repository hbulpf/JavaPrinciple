package test.basic.reflecttest.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 功能描述
 *
 *
 */
public class MyInvocationHandler implements InvocationHandler {
    private Object object;

    public Object bind(Object obj) {
        this.object = obj;
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object obj = method.invoke(this.object,args);
        return obj;
    }
}
