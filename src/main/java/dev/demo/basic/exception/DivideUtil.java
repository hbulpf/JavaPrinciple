
package dev.demo.basic.exception;

/**
 * 功能描述
 *
 */
public class DivideUtil {
    public static double divide(int a, int b) throws MyException {
        System.out.println("invoke int");
        if (b == 0) {
            throw new MyException("divisor can not be zero");
        }
        return (double) a / b;
    }

    public static double divide(double a, double b) throws MyException {
        System.out.println("invoke double");
        if (b == 0) {
            throw new MyException("divisor can not be zero");
        }
        return a / b;
    }

    public static double divideInt(int a, int b) throws MyException {
        double res = -0.00000001;
        try {
            res = divide(a, b);
        } catch (MyException e) {
            throw e;
        } finally {
            // !!!! 不要在 finally 中使用 return , 这将抑制上面抛出的异常
            return res;
        }
    }
}
