package dev.demo.basic.file;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import dev.utils.StringUtils;

/**
 * FileValidator
 */
public class FileValidator {


    /**
     * 允许的文件字符
     */
    private static final String FILE_ALLOW_CHARS = "[^\\\\/:*?\"<>;'^|]+";

    /**
     * 允许的文件名最大长度
     */
    private static final Integer MAX_FILE_NAME_LENGTH = 256;

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    /**
     * 允许的文件类型
     */
    private static Set<String> ALLOW_TYPES = new HashSet<String>();

    static {
        for (FileTypeEnum typeEnum : FileTypeEnum.values()) {
            ALLOW_TYPES.add(typeEnum.getFileType());
        }

    }

    /**
     * 校验文件名长度
     *
     * @param fileName 文件名
     * @return true:校验通过 false:校验不通过
     */
    public static boolean checkFileNameLength(String fileName) {
        return !StringUtils.isNotEmpty(fileName) || fileName.length() <= MAX_FILE_NAME_LENGTH;
    }

    /**
     * 校验文件是否有特殊字符
     *
     * @param fileName 文件名
     * @return true:校验通过 false:校验不通过
     */
    public static boolean checkFileSpecialChar(String fileName) {
        if (StringUtils.isNotEmpty(fileName)) {
            return fileName.matches(FILE_ALLOW_CHARS);
        } else {
            return false;
        }
    }

    /**
     * 校验文件类型是否在允许的范围内
     *
     * @param fileType 文件类型
     * @return true:校验通过 false:校验不通过
     */
    public static boolean checkFileType(String fileType) {
        if (StringUtils.isEmpty(fileType)) {
            return false;
        }
        return ALLOW_TYPES.contains(fileType.toLowerCase(Locale.ENGLISH));
    }

    /**
     * 是否是非法路径
     *
     * @param filePath
     * @return
     */
    public static boolean isIllegalFilePath(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return true;
        }
        // 校验路径中的特殊字符
        String regEx = "^((?!\\.).){0,4096}$";
        return !Pattern.matches(regEx, filePath);
    }

}
