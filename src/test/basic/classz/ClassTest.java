package test.basic.classz;

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
}
