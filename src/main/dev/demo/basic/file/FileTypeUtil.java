package dev.demo.basic.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class FileTypeUtil {
    private static final Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

    public static Logger LOGGER = Logger.getLogger(FileTypeUtil.class.getName());

    static {
        // 初始化文件类型信息
        getAllFileType();
    }

    /**
     * getAllFileType
     */
    private static void getAllFileType() {
        for (FileTypeEnum typeEnum : FileTypeEnum.values()) {
            FILE_TYPE_MAP.put(typeEnum.getFileType(), typeEnum.getFilePrefix());
        }
    }

    /**
     * getFileTypeByFile
     *
     * @param file the file
     * @return file by file
     */
    public static String getFileTypeByFile(File file) {
        String filetype = null;
        byte[] b = new byte[50];
        InputStream is = null;
        try {
            is = new FileInputStream(file);

            if (is.read(b) == -1) {
                LOGGER.severe("Failed to Read bytes from the input stream.");
                return null;
            }

            filetype = getFileTypeByStream(b);
        } catch (FileNotFoundException e) {
            LOGGER.severe("Failed to find file.");
        } catch (IOException e) {
            LOGGER.severe("IO Exception.");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.severe("InputStream close failed");
                }
            }
        }

        return filetype;
    }

    /**
     * getFileTypeByStream
     *
     * @param b the b
     * @return file type by stream
     */
    public static String getFileTypeByStream(byte[] b) {
        String filetypeHex = String.valueOf(getFileHexString(b));
        Iterator<Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();

        while (entryiterator.hasNext()) {
            Entry<String, String> entry = entryiterator.next();
            String fileTypeHexValue = entry.getValue();
            if (filetypeHex.toUpperCase(Locale.ENGLISH).startsWith(fileTypeHexValue)) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * getFileHexString
     *
     * @param b the b
     * @return file hex string
     */
    public static String getFileHexString(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();

        if (b == null || b.length <= 0) {
            return null;
        }

        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);

            if (hv.length() < 2) {
                stringBuilder.append(0);
            }

            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * getFileTypeByFile
     *
     * @param input the input
     * @return file by file
     */
    public static String getFileTypeByFile(InputStream input) {
        String filetype = null;
        byte[] b = new byte[50];
        InputStream is = null;
        try {
            is = input;

            if (is.read(b) == -1) {
                LOGGER.severe("Failed to Read bytes from the input stream.");
                return null;
            }

            filetype = getFileTypeByStream(b);
        } catch (FileNotFoundException e) {
            LOGGER.severe("Failed to find file." + e);
        } catch (IOException e) {
            LOGGER.severe("IO Exception." + e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.severe("InputStream close failed");
                }
            }
        }
        return filetype;
    }

    /**
     * 校验文件类型
     *
     * @param fileType 文件类型
     * @return 校验结果
     */
    public static boolean isValidateFileType(String fileType) {
        // 发票中心:JPG,JPEG,PNG,GIF,PDF,ZIP,RAR
        // 工单:jpg / bmp / png / gif / txt / doc / docx

        return (FileTypeEnum.JPG.getFileType().equalsIgnoreCase(fileType) || FileTypeEnum.BMP.getFileType()
            .equalsIgnoreCase(fileType) || FileTypeEnum.GIF.getFileType().equalsIgnoreCase(fileType)
            || FileTypeEnum.DOCX.getFileType().equalsIgnoreCase(fileType) || "txt".equalsIgnoreCase(fileType)
            || FileTypeEnum.PNG.getFileType().equalsIgnoreCase(fileType)
            || FileTypeEnum.JPEG.getFileType().equalsIgnoreCase(fileType) || FileTypeEnum.DOC.getFileType()
            .equalsIgnoreCase(fileType) || FileTypeEnum.PDF.getFileType().equalsIgnoreCase(fileType)
            || FileTypeEnum.RAR.getFileType().equalsIgnoreCase(fileType) || FileTypeEnum.ZIP.getFileType()
            .equalsIgnoreCase(fileType));
    }

    /**
     * 安全问题整改：
     * zip包中不能含有jsp木马病毒
     *
     * @return
     */
    public static boolean checkJSPFile(InputStream input, String fileName) {
        String type = getFileTypeByFile(input);
        return "jsp".equalsIgnoreCase(type);
    }

    /**
     * Zip 文件检查
     *
     * @param path
     * @return false 解压失败，true 解压通过
     * @throws FileDemoException
     */
    public static Boolean checkZipFileType(String path) throws FileDemoException {
        LOGGER.info("check zip start... path = " + path);
        Enumeration<?> e = null;
        try (ZipFile zipFileObj = new ZipFile(path, Charset.forName("GBK"))) {
            if (null == zipFileObj) {
                LOGGER.severe("get compressed package failed,check zip file return ");
                return false;
            } else {
                e = zipFileObj.entries();
            }
            if (null == e) {
                LOGGER.severe("Enumeration is null!");
                return false;
            }
            ZipEntry zipEntry = null;
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
                if (zipEntry.isDirectory()) {
                    continue;
                }
                if (!FileValidator.isIllegalFilePath(zipEntry.getName())) {
                    LOGGER.severe("File path is illegal. File name =  " + zipEntry.getName());
                    throw new FileDemoException("com.huawei.csb.csbcontractservice.common.utils.fileError.illegalPath");
                }
                try (InputStream input = zipFileObj.getInputStream(zipEntry)) {
                    // 校验zip中不能包含jsp文件
                    if (checkJSPFile(input, zipEntry.getName())) {
                        LOGGER.severe("check zip file fail . fileName = " + zipEntry.getName());
                        throw new FileDemoException("com.huawei.csb.csbcontractservice.common.utils.fileError.ZipFile");
                    }
                } catch (IOException e1) {
                    LOGGER.severe("Failed to decompress package!");
                }
            }
        } catch (IOException e3) {
            LOGGER.severe("get compressed package failed:" + e3);
        }
        return true;
    }

}
