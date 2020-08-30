# RSA数字证书生成

# 一、Java代码生成cer证书文件

```
/**
 * RSA数字证书生成，jks文件生成以及读取。
 */
public class GetCertFile {

    /**
     * 证书颁发者
     */
    public static final String CertificateIssuer = "C=中国,ST=广东,L=广州,O=人民组织,OU=人民单位,CN=人民颁发";
    //

    /**
     * 证书使用者
     */
    public static final String CertificateUser = "C=中国,ST=广东,L=广州,O=人民组织,OU=人民单位,CN=";

    /**
     * X509 证书文件路径
     */
    public static final String X509_CER_PATH = "e:\\cer.cer";

    public static void main(String[] args) {
        try {
            X509Certificate cert = getCert();
            System.out.println(cert.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * CN(Common Name名字与姓氏)
     * OU(Organization Unit组织单位名称)
     * O(Organization组织名称)
     * ST(State州或省份名称)
     * C(Country国家名称)
     * L(Locality城市或区域名称)
     *
     * @return
     * @throws Exception
     */
    public static X509Certificate getCert() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        X509V3CertificateGenerator x509V3CertificateGenerator = new X509V3CertificateGenerator();
        //设置证书序列号
        x509V3CertificateGenerator.setSerialNumber(BigInteger.TEN);
        //设置证书颁发者
        x509V3CertificateGenerator.setIssuerDN(new X500Principal(CertificateIssuer));
        //设置证书使用者
        x509V3CertificateGenerator.setSubjectDN(new X500Principal(CertificateUser + "sun"));
        //设置证书有效期
        x509V3CertificateGenerator.setNotAfter(new Date(System.currentTimeMillis() + 1000 * 365 * 24 * 3600));
        x509V3CertificateGenerator.setNotBefore(new Date(System.currentTimeMillis()));
        //设置证书签名算法
        x509V3CertificateGenerator.setSignatureAlgorithm("SHA1withRSA");

        x509V3CertificateGenerator.setPublicKey(publicKey);

        //临时bc方法添加都环境变量
        Security.addProvider(new BouncyCastleProvider());
        X509Certificate x509Certificate = x509V3CertificateGenerator.generateX509Certificate(keyPair.getPrivate(),
            "BC");
        //写入文件
        FileOutputStream fos = new FileOutputStream(X509_CER_PATH);
        fos.write(x509Certificate.getEncoded());
        fos.flush();
        fos.close();
        return x509Certificate;
    }
}
```

#  二、Java代码生成jks文件和cer证书文件，以及jks文件和cer文件的读取：

```
/**
 * 生成jks文件和cer证书文件，以及jks文件和cer文件的读取
 */
public class GetJksAndCerFile {

    public static final String JKS_PATH = "e:/demo.jks";

    public static final String CER_PATH = "e:/demo.cer";

    public static void main(String[] args) {
        buildKeyAndSaveToJksFile();
        exportCerFile();
        try {
            readJks();
            readCer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executeCommand(String[] arstringCommand) {
        try {
            Runtime.getRuntime().exec(arstringCommand);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 生成密钥并保存到jks文件
     */
    public static void buildKeyAndSaveToJksFile() {
        String[] command = new String[] {
            "cmd ",
            "/k",
            "start", // cmd Shell命令
            "keytool",
            "-genkeypair", //表示生成密钥
            "-alias", //要处理的条目的别名（jks文件别名）
            "sun",
            "-keyalg", //密钥算法名称(如 RSA DSA（默认是DSA）)
            "RSA",
            "-keysize",//密钥位大小(长度)
            "1024",
            "-sigalg", //签名算法名称
            "SHA1withRSA",
            "-dname",// 唯一判别名,CN=(名字与姓氏), OU=(组织单位名称), O=(组织名称), L=(城市或区域名称),ST=(州或省份名称), C=(单位的两字母国家代码)"
            "CN=(张三), OU=(人民单位), O=(人民组织), L=(广州), ST=(广东), C=(中国)",
            "-validity", // 有效天数
            "36500",
            "-keypass",// 密钥口令(私钥的密码)
            "123456",
            "-keystore", //密钥库名称(jks文件路径)
            JKS_PATH,
            "-storepass", // 密钥库口令(jks文件的密码)
            "123456",
            "-v"// 详细输出（秘钥库中证书的详细信息）
        };
        executeCommand(command);
    }

    /**
     * 从jks文件中导出证书文件
     */
    public static void exportCerFile() {
        String[] command = new String[] {
            "cmd ", "/k",
            "start", // cmd Shell命令
            "keytool",
            "-exportcert", // - export指定为导出操作
            "-alias", // -alias指定别名，这里是ss
            "sun",
            "-keystore", // -keystore指定keystore文件，这里是d:/demo.keystore
            JKS_PATH,
            "-rfc",
            "-file",//-file指向导出路径
            CER_PATH,
            "-storepass",// 指定密钥库的密码
            "123456"
        };
        executeCommand(command);
    }

    //读取jks文件获取公、私钥
    public static void readJks() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(JKS_PATH), "123456".toCharArray());
        Enumeration<String> aliases = keyStore.aliases();
        String alias = null;
        while (aliases.hasMoreElements()) {
            alias = aliases.nextElement();
        }
        System.out.println("jks文件别名是：" + alias);
        PrivateKey key = (PrivateKey) keyStore.getKey(alias, "123456".toCharArray());
        System.out.println("jks文件中的私钥是：" + new String(Base64.encode(key.getEncoded())));
        Certificate certificate = keyStore.getCertificate(alias);
        PublicKey publicKey = certificate.getPublicKey();
        System.out.println("jks文件中的公钥:" + new String(Base64.encode(publicKey.getEncoded())));
    }

    //读取证书文件获取公钥
    public static void readCer() throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate certificate =
            certificateFactory.generateCertificate(new FileInputStream(JKS_PATH));
        PublicKey publicKey = certificate.getPublicKey();
        System.out.println("证书中的公钥:" + new String(Base64.encode(publicKey.getEncoded())));
    }
}
```

需导入的jar包：

```
<dependency>
      <groupId>org.bouncycastle</groupId>
      <artifactId>bcprov-jdk16</artifactId>
      <version>1.46</version>
    </dependency>
```

#  三、使用keytool命令生成jks文件和cer证书文件：
  先进入jdk的bin目录，然后执行keytool命令。
1、生成jks文件命令：

```
keytool -genkey -validity 36500 -keysize 1024 -alias sun -sigalg SHA1withRSA -keyalg RSA-keystore f:/demo.jks -dname CN=(SS)OU=(SS)O=(SS)L=(BJ)ST=(BJ)C=(CN) -storepass 123456 -keypass123456 -v
```

2、生成cer证书文件命令：

```
keytool -exportcert -alias sun -keystore f:/demo.jks -rfc -file f:/demo.cer -storepass 123456
```

3、生成jks文件的keytool命令参数：



```
-alias <alias>          要处理的条目的别名
-keyalg <keyalg>         密钥算法名称
-keysize <keysize>        密钥位大小
-sigalg <sigalg>         签名算法名称
-destalias <destalias>      目标别名
-dname <dname>          唯一判别名
-startdate <startdate>      证书有效期开始日期/时间
-ext <value>           X.509 扩展
-validity <valDays>       有效天数
-keypass <arg>          密钥口令
-keystore <keystore>       密钥库名称
-storepass <arg>         密钥库口令
-storetype <storetype>      密钥库类型
-providername <providername>   提供方名称
-providerclass <providerclass>  提供方类名
-providerarg <arg>        提供方参数
-providerpath <pathlist>     提供方类路径
-v                详细输出
-protected            通过受保护的机制的口令
```

4、生成cer证书文件keytool命令参数：

```
-certreq       生成证书请求
-changealias     更改条目的别名
-delete       删除条目
-exportcert     导出证书
-genkeypair     生成密钥对
-genseckey      生成密钥
-gencert       根据证书请求生成证书
-importcert     导入证书或证书链
-importpass     导入口令
-importkeystore   从其他密钥库导入一个或所有条目
-keypasswd      更改条目的密钥口令
-list        列出密钥库中的条目
-printcert      打印证书内容
-printcertreq    打印证书请求的内容
-printcrl      打印 CRL 文件的内容
-storepasswd     更改密钥库的存储口令
```

# 证书工具

1. [亚洲诚信](https://myssl.com/csr_create.html)
2. [SSL Tools](https://tools.ssldun.com/)

# 参考

1. [java中RSA数字证书生成，jks文件生成以及读取](https://blog.csdn.net/maguanghui_2012/article/details/80381476)