package dev.demo.java8.lambda;


/**多参数无返回*/
@FunctionalInterface
interface NoReturnMultiParam {
    void method(int a, int b);
}

/**无参无返回值*/
@FunctionalInterface
interface NoReturnNoParam {
    void method();
}

/**一个参数无返回*/
@FunctionalInterface
interface NoReturnOneParam {
    void method(int a);
}

/**多个参数有返回值*/
@FunctionalInterface
interface ReturnMultiParam {
    int method(int a, int b);
}

/*** 无参有返回*/
@FunctionalInterface
interface ReturnNoParam {
    int method();
}

/**一个参数有返回值*/
@FunctionalInterface
interface ReturnOneParam {
    int method(int a);
}