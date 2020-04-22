package zmqc.iceyung.aoptool;

import java.lang.reflect.Proxy;


public class ProxyFactory {

    public static Object getProxy(Object targetObject,
                                  MyHandler handlers) {
        Object proxyObject = null;
        if (handlers != null) {
            proxyObject = targetObject;
            handlers.setTargetObject(proxyObject);
            proxyObject = Proxy.newProxyInstance(targetObject.getClass()
                    .getClassLoader(), targetObject.getClass()
                        .getInterfaces(), handlers);
            return proxyObject;
        } else {
            return targetObject;
        }
    }
}
