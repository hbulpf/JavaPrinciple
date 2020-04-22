package zmqc.iceyung.aoptool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader {
    //加载类的目录
    private String classpath;

    public MyClassLoader(String classpath) {

        this.classpath = classpath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            //获取类的字节码
            byte [] classDate = getDate(name);

            if(classDate!=null){
                //defineClass方法将字节码转化为类
                return defineClass(name,classDate,0,classDate.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }
    //返回类的字节码
    private byte[] getDate(String className) throws IOException{
        InputStream in = null;
        ByteArrayOutputStream out = null;
        //拼接字节码的地址和名称
        String path = classpath + File.separatorChar +
                className.replace('.',File.separatorChar)+".class";

        try {
            in = new FileInputStream(path);
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int len = 0;
            while((len=in.read(buffer))!=-1){
                out.write(buffer,0,len);
            }
            return out.toByteArray();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            in.close();
            out.close();
        }
        return null;
    }
}
