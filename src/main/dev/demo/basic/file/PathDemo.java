package dev.demo.basic.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * PathDemo
 */
public class PathDemo {
    public static void main(String[] args) {
        test01();
    }

    public static void test01() {
        File file = new File("F:\\CloudBU");
        String[] files = file.list();
        if (files == null || files.length == 0) {
            System.out.println("为空");
            return;
        }
        int len = files.length;
        System.out.println(len);
        for (int i = 0; i < len; i++) {
            System.out.println(files[i]);
        }
        try {
            FileUtils.checkFile(file);
        } catch (FileDemoException e) {
            System.out.println(e);
        }
        System.out.println("end~");
    }


    public static void test02() {
        Path filePath = Paths.get("/opt/test.txt");
        if (Files.exists(filePath)) {
            // TODO
        }
        // 下面的不存在
/*        if (filePath.exists()) {
            // TODO
        }
        if (filePath.isExists()) {
            // TODO
        }
        if (Files.isExists(filePath)) {
            // TODO
        }*/
    }

}
