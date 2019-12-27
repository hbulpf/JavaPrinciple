package dev.lpf.demo.basic.reflecttest.proxy;

/**
 * 真实项目
 */
public class RealSubject implements ISubject{
    @Override
    public String say(String subjectName) {
        return "真实项目:"+subjectName;
    }
}
