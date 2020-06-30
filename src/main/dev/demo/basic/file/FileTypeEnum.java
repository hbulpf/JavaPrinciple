package dev.demo.basic.file;


public enum FileTypeEnum {
    /**
     * XLSX
     */
    XLSX("xlsx", "504B030414000600"),
    /**
     * PNG (png)
     */
    PNG("png", "89504E47"),
    /**
     * JPEG (jpg)
     */
    JPG("jpg", "FFD8FF"),

    /**
     * JPEG (jpg)
     */
    JPEG("jpeg", "FFD8FF"),

    /**
     * GIF (gif)
     */
    GIF("gif", "47494638"),

    /**
     * TIFF (tif)
     */
    TIF("tif", "49492A00"),

    /**
     * Windows Bitmap (bmp)
     */
    BMP("bmp", "424D"),

    /**
     * CAD (dwg)
     */
    DWG("dwg", "41433130"),

    /**
     * HTML (html)
     */
    HTML("html", "68746D6C3E"),

    /**
     * Rich Text Format (rtf)
     */
    RTF("rtf", "7B5C727466"),

    /**
     * The constant XML.
     */
    XML("xml", "3C3F786D6C"),

    /**
     * The constant ZIP.
     */
    ZIP("zip", "504B0304"),

    /**
     * The constant RAR.
     */
    RAR("rar", "52617221"),

    /**
     * // Photoshop (psd)
     */
    PSD("psd", "38425053"),

    /**
     * Email
     */
    EML("eml", "44656C69766572792D646174653A"),

    /**
     * Outlook Express (dbx)
     */
    DBX("dbx", "CFAD12FEC5FD746F"),

    /**
     * Outlook (pst)
     */
    PST("pst", "2142444E"),

    /**
     * MS Excel 注意：word 和 excel的文件头一样
     */
    XLS("xls", "D0CF11E0"),

    /**
     * MS Word
     */
    DOC("doc", "D0CF11E0"),


    /**
     * MS Word
     */
    DOCX("docx", "504B030414000600"),


    /**
     * MS Access (mdb)
     */
    MDB("mdb", "5374616E64617264204A"),

    /**
     * WordPerfect (wpd)
     */
    WPD("wpd", "FF575043"),

    /**
     * The constant EPS.
     */
    EPS("eps", "252150532D41646F6265"),

    /**
     * The constant PS.
     */
    PS("ps", "252150532D41646F6265"),

    /**
     * Adobe Acrobat (pdf)
     */
    PDF("pdf", "255044462D312E"),

    /**
     * Quicken (qdf)
     */
    QDF("qdf", "AC9EBD8F"),

    /**
     * Windows Password (pwl)
     */
    PWL("pwl", "E3828596"),

    /**
     * Wave (wav)
     */
    WAV("wav", "57415645"),

    /**
     * The constant AVI.
     */
    AVI("avi", "41564920"),

    /**
     * The constant RAM.
     */
    RAM("ram", "2E7261FD"),

    /**
     * Real Media (rm)
     */
    RM("rm", "2E524D46"),

    /**
     * The constant MPG.
     */
    MPG("mpg", "000001BA"),

    /**
     * Quicktime (mov)
     */
    MOV("mov", "6D6F6F76"),

    /**
     * Windows Media (asf)
     */
    ASF("asf", "3026B2758E66CF11"),

    /**
     * MIDI (mid)
     */
    MID("mid", "4D546864"),

    /**
     * DAT(dat)
     */
    DAT("dat", "48756177656920546563686E6F6"),

    /**
     * The constant SH.
     */
    SH("sh", "23212F62696E2F"),

    /**
     * The constant EXE.
     */
    EXE("exe", "4D5A900003"),

    /**
     * The constant BAT.
     */
    BAT("bat", "406563686F206F"),

    /**
     * The constant CLASS.
     */
    CLASS("class", "CAFEBABE00000033"),

    /**
     * The constant JSP.
     */
    JSP("jsp", "3C254020706167");

    private String fileType;

    private String filePrefix;

    FileTypeEnum(String fileType, String filePrefix) {
        this.fileType = fileType;
        this.filePrefix = filePrefix;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

}
