package zmqc.iceyung.aoptool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException,
            SecurityException, IllegalArgumentException, InvocationTargetException {
        //自定义类加载器的加载路径
        MyClassLoader myClassLoader = new MyClassLoader("E:\\CodeLibrary\\Java\\iceyung");
        //包名+类名
        Class c = myClassLoader.loadClass("Test");

        if(c!=null){
            Object obj = c.newInstance();
            Method method = c.getMethod("getAllStudentNum", null);
            
            method.invoke(obj, null);
            System.out.println(c.getClassLoader().toString());
        }
    }
}
