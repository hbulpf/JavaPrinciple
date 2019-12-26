package test.basic.classz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ClassTest {
    public static void main(String[] args) {
        System.out.println(new ClassTest().sayHi());
    }

    public String sayHi(){
        System.out.println("className = " + this.getClass().getName());
        System.out.println("typeName = " + this.getClass().getTypeName());
        System.out.println("methodName = " + Thread.currentThread().getStackTrace()[1].getMethodName());
        System.out.println("line="+
                Thread.currentThread().getStackTrace()[1].getLineNumber());
        return String.format("%s.%s  line:%d",this.getClass().getTypeName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),Thread.currentThread().getStackTrace()[1].getLineNumber());
    }
    
     /**
     * @param obj  操作的对象
     * @param attr 操作的属性
     */
    public static Object getObjField(Object obj, String attr) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(attr);
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * @param obj   操作的对象
     * @param attr  操作的属性
     * @param value 设置属性值
     */
    public static void setObjField(Object obj, String attr, Object value) throws NoSuchFieldException,
        IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Field field = obj.getClass().getDeclaredField(attr);
        field.setAccessible(true);
        Class typeClass = field.getType();
        System.out.println(typeClass);
        Constructor constructor = typeClass.getDeclaredConstructor(String.class);
        Object valObj = constructor.newInstance(value);
        field.set(obj, valObj);
    }
}
