# POI

处理 word/excel 的工具类

## 抽取word中的内容
Word包括docx和doc，其中doc源文件为二进制流文件，可读性较差。docx为xml文件，可读性较强。
想要使用全套的poi解析word，引用的maven包如下：


```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>3.17</version>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>3.17</version>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>ooxml-schemas</artifactId>
    <version>1.3</version>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-scratchpad</artifactId>
    <version>3.17</version>
</dependency>
<dependency>
    <groupId>org.apache.xmlbeans</groupId>
    <artifactId>xmlbeans</artifactId>
    <version>2.6.0</version>
</dependency>
```

如使用其他版本，参考 [Maven Repository:poi](https://mvnrepository.com/artifact/org.apache.poi/poi) 点击对应版本号往下拉可以看到对应依赖版本配置，以保证没有依赖错误。


docx是微软Word的文件扩展名，Microsoft Office2007之后版本使用，其基于Office Open XML标准的压缩文件格式取代了其以前专有的默认文件格式，在传统的文件名扩展名后面添加了字母“x”,任何能够打开DOC文件的文字处理软件都可以将该文档转换为DOCX文件。

在poi中，doc与docx使用的是完全不同的类，方法内部逻辑也不相同，需要各自开发。

* HWPF 是 POI 支持 Word(97-2003) 的 Java 组件，支持读写Word文档，但是写功能目前只实现一部分；它也提供更早版本的Word6和Word95版本的简单的文本摘录功能。 doc主要使用的类：HWPFDocument/WordExtractor。
* XWPF是 POI 支持 Word 2007+ 的 Java组件，提供简单文件的读写功能。 docx主要使用的类：XWPFDocument/POIXMLTextExtractor。[相关规范](http://officeopenxml.com/WPtableRowProperties.php)
* HSSF提供读写Microsoft Excel XLS格式档案的功能
* XSSF提供读写Microsoft Excel OOXML XLSX格式档案的功能。
* HSLF提供读写Microsoft PowerPoint格式档案的功能。
* HDGF提供读Microsoft Visio格式档案的功能。
* HPBF提供读Microsoft Publisher格式档案的功能。
* HSMF提供读Microsoft Outlook格式档案的功能。
