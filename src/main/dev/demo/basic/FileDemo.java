package dev.demo.basic.file;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import dev.utils.StringUtils;

/**
 * FileDemo
 */
public class FileDemo {

    static Logger logger = Logger.getLogger(FileDemo.class.getName());

    public static void main(String[] args) {
        File file = new File("F:/tmp/output.docx");

        try {
            test01(file);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }


        System.out.println("=========检测文件");
        try {
            FileUtils.checkFile(file);
        } catch (FileDemoException e) {
            logger.severe(e.getMessage());
        }

        System.out.println("=========非法路径检测");
        try {
            File file3 = new File("F:/tmp/../output.docx");
            FileUtils.checkFile(file3);
        } catch (FileDemoException e) {
            e.printStackTrace();
        }

        System.out.println("=========解压测试");
        try {
            File file4 = new File("F:\\mavendemo.zip");
            if(FileUtils.isZipBombAttack(file4)){
                System.out.println("是文件炸弹");
            }else{
                System.out.println("不是文件炸弹");
            }
            StringUtils.printHr("=========开始解压");
            FileUtils.unzip(file4,file4.getParent()+File.separator+"tmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test01(File file) throws IOException {
        String fileName = file.getName();
        StringUtils.printHr("fileName", fileName);
        String filePath = file.getPath();
        StringUtils.printHr("filePath", filePath);
        String absolutePath = file.getAbsolutePath();
        StringUtils.printHr("absolutePath", absolutePath);
        String canonicalPath = file.getCanonicalPath();
        StringUtils.printHr("canonicalPath", canonicalPath);
        String parrentPath = file.getParent();
        StringUtils.printHr("canonicalPath", parrentPath);
        long len = file.length();
        StringUtils.printHr("len", String.valueOf(len));

        StringUtils.printHr("fileName 特殊字符检查");
        if (FileValidator.checkFileSpecialChar(fileName)) {
            System.out.println("无非法字符");
        } else {
            System.out.println("有非法字符");
        }

        StringUtils.printHr("从filePath得到: fileName 特殊字符检查");
        if (StringUtils.isNotEmpty(filePath) && filePath.contains("\\")) {
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        }
        if (FileValidator.checkFileSpecialChar(fileName)) {
            System.out.println("无非法字符");
        } else {
            System.out.println("有非法字符");
        }

        StringUtils.printHr("filePath 特殊字符检查");
        if (FileValidator.checkFileSpecialChar(filePath)) {
            System.out.println("无非法字符");
        } else {
            System.out.println("有非法字符");
        }

        StringUtils.printHr("filePath 非法路径");
        if (FileValidator.isIllegalFilePath(filePath)) {
            System.out.println("是非法路径");
        } else {
            System.out.println("不是非法路径");
        }
    }

}
