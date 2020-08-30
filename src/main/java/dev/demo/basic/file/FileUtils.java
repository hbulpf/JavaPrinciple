package dev.demo.basic.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import dev.utils.StringUtils;

public class FileUtils {

    /**
     * 压缩文件 BUFFER
     */
    static final int BUFFER = 512;

    /**
     * 解压数据最大
     */
    static final long TOOBIG = 0x6400000;

    /**
     * 解压数据最大条目
     */
    static final int TOOMANY = 1024;

    /**
     * 文件大小总和 50MB
     */
    private static final int SIZE_MAX = 50 * 1024 * 1024;

    /**
     * 单个文件大小 10MB
     */
    private static final int SINGLE_FILE_SIZE_MAX = 10 * 1024 * 1024;

    /**
     * 单次上传文件数量的最大值
     */
    private static final int FILE_AMOUNT_MAX = 3;

    private static final String FILE_SEPARATOR = File.separator;

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    /**
     * 从一个文件名中获取该文件的类型
     *
     * @param fileName 文件名，可以不是全路径
     * @return 如果不包含.，则返回null
     */
    public static String getFileType(String fileName) {
        if (StringUtils.isNotEmpty(fileName)) {
            int index = fileName.lastIndexOf(".");
            if (index != -1) {
                return fileName.substring(index + 1);
            }
        }

        return null;
    }

    /**
     * 解析出文件名
     *
     * @param fileName 文件名，可以不是全路径
     * @return 如果不包含.，则返回null
     */
    public static String getFileName(String fileName) {
        if (StringUtils.isNotEmpty(fileName)) {
            int index = fileName.lastIndexOf(".");
            if (index != -1) {
                return fileName.substring(0, index);
            }
        }

        return null;
    }

    /**
     * 解析出不带文件名的文件路径
     *
     * @param path 带路径文件名
     * @return 不带文件名的文件路径
     */
    public static String getFilePath(String path) {
        String filePath = path;
        if (StringUtils.isNotEmpty(filePath) && filePath.contains(FILE_SEPARATOR)) {
            filePath = filePath.substring(0, filePath.lastIndexOf(FILE_SEPARATOR));
            return filePath;
        } else {
            return null;
        }
    }

    /**
     * 校验文件名
     * 不包含特殊字符\:*?"<>|
     *
     * @param objectName
     */
    public static void checkObjectName(String objectName) {
        if (objectName.contains("\\") || objectName.contains(":") || objectName.contains("*")
            || objectName.contains("?") || objectName.contains("\"") || objectName.contains("<")
            || objectName.contains(">") || objectName.contains("|")) {
            throw new RuntimeException("objectName not validate");
        }
    }

    /**
     * 文件校验
     * <p>
     * 1、文件名长度校验
     * 2、文件名特殊字符校验
     * 3、文件大小校验
     * 4、文件类型校验
     * 5、文件数量校验，当前一次不能超过三个
     *
     * @param file
     */
    public static void checkFile(File file) throws FileDemoException {
        if (file != null) {

            String fileName = file.getName();

            if (StringUtils.isNotEmpty(fileName) && fileName.contains(FILE_SEPARATOR)) {
                fileName = fileName.substring(fileName.lastIndexOf(FILE_SEPARATOR) + 1);
            }

            //校验是否为目录
            if (file.isDirectory()) {
                LOGGER.severe("not file,it is directory");
                throw new FileDemoException("not file,it is directory");
            }
            // 校验文件名长度
            if (!FileValidator.checkFileNameLength(getFileName(fileName))) {
                LOGGER.severe("File name is too long. File name = " + fileName);
                throw new FileDemoException("File name is too long. File name = " + fileName);
            }
            // 文件名特殊字符校验
            if (!FileValidator.checkFileSpecialChar(fileName)) {
                LOGGER.severe("File name contains special chars. File name = " + fileName);
                throw new FileDemoException(
                    "File name contains special chars. File name = " + fileName);
            }
            // 初步校验文件类型
            if (!FileValidator.checkFileType(getFileType(fileName))) {
                LOGGER.severe("File type illegal. File name = " + fileName);
                throw new FileDemoException("File type illegal. File name = " + fileName);
            }
            // 校验文件大小
            if (SINGLE_FILE_SIZE_MAX < file.length()) {
                LOGGER.severe("File is too large. File name =" + fileName);
                throw new FileDemoException("File is too large. File name =" + fileName);
            }
            // 文件路径校验
            if (FileValidator.isIllegalFilePath(getFilePath(file.getPath()))) {
                LOGGER.severe("File path is illegal. File name =  " + fileName);
                throw new FileDemoException("File path is illegal. File name =  " + fileName);
            }
            // 用文件的二进制流来获取文件类型
            String fileType = FileTypeUtil.getFileTypeByFile(file);
            if (StringUtils.isEmpty(fileType)) {
                LOGGER.severe("Could not resolve file type.File name =  " + fileName);
                throw new FileDemoException(
                    "Could not resolve file type. File name =  " + fileName);
            }
            // 校验真实文件类型
            if (!FileValidator.checkFileType(fileType)) {
                LOGGER.severe("File type illegal. File type = " + fileType);
                throw new FileDemoException("File type illegal. File type = " + fileType);
            }
            // 检查zip炸弹
            if (FileTypeEnum.ZIP.getFileType().equals(fileType)) {
                if (isZipBombAttack(file)) {
                    LOGGER.severe("zip file might be ZipBomb. File name = " + fileName);
                    throw new FileDemoException("zip file might be ZipBomb. File name = " + fileName);
                }
            }
            LOGGER.info("check file ok");
        }
    }

    /**
     * 校验文件名
     *
     * @param filename        文件名
     * @param intendedDirPath 希望解压的目录的路径
     * @return
     * @throws java.io.IOException
     */
    private static String validateFilename(String filename, String intendedDirPath) throws java.io.IOException {
        File f = new File(filename);
        String filePath = f.getCanonicalPath();

        File intendedDir = new File(intendedDirPath);
        String intendedPath = intendedDir.getCanonicalPath();

        if (filePath.startsWith(intendedPath)) {
            return filePath;
        } else {
            throw new IllegalStateException("File is outside extraction target directory.");
        }
    }

    /**
     * 解压文件
     *
     * @param zipFile     要解压的文件
     * @param unzippedDir 解压的目录
     * @throws java.io.IOException
     */
    public static final void unzip(File zipFile, String unzippedDir) throws java.io.IOException {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)),
            Charset.forName("GBK"));
        ZipEntry entry;
        int entries = 0;
        long total = 0;
        if (!unzippedDir.endsWith(File.separator)) {
            unzippedDir += File.separator;
        }
        try {
            while ((entry = zis.getNextEntry()) != null) {
                LOGGER.info("Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];
                // Write the files to the disk, but ensure that the filename is valid,
                // and that the file is not insanely big
                String name = validateFilename(entry.getName(), ".");
                if (entry.isDirectory()) {
                    LOGGER.info("Creating directory " + name);
                    File dirFile = new File(unzippedDir + name.substring(name.lastIndexOf(File.separator) + 1));
                    boolean ok = dirFile.mkdir();
                    if (!ok) {
                        LOGGER.severe("failed to creating directory:" + dirFile.getName());
                    }
                    continue;
                }
                FileOutputStream fos = new FileOutputStream(unzippedDir + entry.getName());
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                while (total + BUFFER <= TOOBIG && (count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                    total += count;
                }
                dest.flush();
                dest.close();
                zis.closeEntry();
                entries++;
                if (entries > TOOMANY) {
                    throw new IllegalStateException("Too many files to unzip.");
                }
                if (total + BUFFER > TOOBIG) {
                    throw new IllegalStateException("File being unzipped is too big.");
                }
            }
        } finally {
            zis.close();
        }
    }

    /**
     * Zip炸弹检查
     * 上传压缩包之前先判断是否为zip炸弹攻击，看下压缩包里面内容大小是否合法再上传
     *
     * @param zipFile 压缩文件
     * @return
     */
    public static boolean isZipBombAttack(File zipFile) {
        LOGGER.info("begin to check if zip file content is valid...");
        byte[] buffer = new byte[BUFFER];
        try (ZipInputStream zipIn =
            new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)), Charset.forName("GBK"))) {
            ZipEntry zipEntry;
            long fileCount = 0L;
            long uncompressedSize = 0L;
            while ((zipEntry = zipIn.getNextEntry()) != null) {
                if (++fileCount > TOOMANY) {
                    LOGGER.info(String.format("fileCount [{}] exceed maxFileCount [{}]", fileCount, TOOMANY));
                    return true;
                }
                long size = zipEntry.getSize();
                int len;
                if (size == -1L) {
                    while ((len = zipIn.read(buffer, 0, buffer.length)) != -1) {
                        uncompressedSize += (long) len;
                    }
                } else {
                    uncompressedSize += size;
                }
                if (uncompressedSize > TOOBIG) {
                    LOGGER.severe(String.format("file size [%d] exceed maxTotalBytes [%d]", uncompressedSize, TOOBIG));
                    zipIn.closeEntry();
                    return true;
                }
            }
        } catch (IOException e) {
            LOGGER.severe("read zip file error occured,error message is: " + e.getMessage());
            return true;
        }
        return false;
    }

}
