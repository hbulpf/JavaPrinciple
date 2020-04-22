package zmqc.iceyung.aoptool;

public class ProxyTest {

    public static void main(String[] args){
        //声明业务类实例
        Operator operator = new OperatorImpl();
        //使用代理工厂生成业务类的代理实例
        Operator operatorProxy = (Operator)ProxyFactory.getProxy(operator,new MyHandler());
        //运行
        int num = operatorProxy.getAllStudentNum();
        System.out.println("student num is "+ num);
    }
}
