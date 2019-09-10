package test.basic.property;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class PropertyEditor {

    public static void main(String[] args) throws IOException {
        //setProp();
        getProp();
    }
    public static void getProp() throws IOException {

        //获取classpath:jdbc.properties
        //URL url = ClassLoader.getSystemResource("jdbc.properties");
        //FileInputStream fis = new FileInputStream(url.getFile());

        Properties prop = new Properties();// 属性集合对象
        FileInputStream fis = new FileInputStream("jdbc.properties");// 属性文件输入流
        prop.load(fis);// 将属性文件流装载到Properties对象中
        fis.close();// 关闭流
        // 获取属性值，sitename已在文件中定义
        System.out.println("获取属性值：url=" + prop.getProperty("url"));
        // 获取属性值，country未在文件中定义，将在此程序中返回一个默认值，但并不修改属性文件
        System.out.println("获取属性值：username=" + prop.getProperty("username", "root"));
        // 获取属性值，sitename已在文件中定义
        System.out.println("获取属性值：password=" + prop.getProperty("password"));
        // 获取属性值，country未在文件中定义，将在此程序中返回一个默认值，但并不修改属性文件
        System.out.println("获取属性值：driver-class-name=" + prop.getProperty("driver-class-name", "com.mysql.jdbc.Driver"));
    }

    public static void setProp() throws IOException {
        Properties prop = new Properties();// 属性集合对象
        // 添加或修改sitename的属性值
        prop.setProperty("url", "jdbc:mysql://100.95.129.189:32080/crm-sky?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true");
        prop.setProperty("username", "root");
        prop.setProperty("password", "113456");
        prop.setProperty("driver-class-name", "com.mysql.jdbc.Driver");
        // 文件输出流
        FileOutputStream fos = new FileOutputStream("jdbc.properties");
        // 将Properties集合保存到流中
        prop.store(fos, null);
        fos.close();// 关闭流
        System.out.println("写入完成");
    }
}
