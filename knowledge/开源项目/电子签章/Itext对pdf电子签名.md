# Itext对pdf进行电子签名

最近在研究Itext对pdf进行签章，记录一下。

可参照：http://developers.itextpdf.com/examples/security-itext5/digital-signatures-white-paper中的例子

## 一. 准备相关文件：

1.背景色为空的印章图片

2.扩展名为.p12的证书（可自行百度keytool 生成证书 pkcs12）

3.一个使用acrobat处理过的pdf模板（使用数字签名表单）

## 二. 引入相关maven依赖：

```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itextpdf</artifactId>
    <version>5.5.11</version>
</dependency>
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext-asian</artifactId>
    <version>5.2.0</version>
</dependency>
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk15on</artifactId>
    <version>1.49</version>
</dependency>
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcpkix-jdk15on</artifactId>
    <version>1.49</version>
</dependency> 
```


## 三. 编写相关代码，

1.编写签章类，收集签章相关信息

```java
package com.example.itext.utils;

import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.security.MakeSignature;
import lombok.Data;

import java.security.PrivateKey;
import java.security.cert.Certificate;


@Data
public class SignatureInfo {
    private String reason; //理由
    private String location;//位置
    private String digestAlgorithm;//摘要类型
    private String imagePath;//图章路径
    private String fieldName;//表单域名称
    private Certificate[] chain;//证书链
    private PrivateKey pk;//私钥
    private int certificationLevel = 0; //批准签章
    private PdfSignatureAppearance.RenderingMode renderingMode;//表现形式：仅描述，仅图片，图片和描述，签章者和描述
    private MakeSignature.CryptoStandard subfilter;//支持标准，CMS,CADES
}
```

2.编写工具类，红色字体为完成签章的主体逻辑，以ByteArrayOutputStream充当输出流是为了完成多次签章，期间不会生成临时文件。MakeSignature.signDetached()方法每调用一次，必须重新获取

```java
PdfSignatureAppearance对象，因为分析源码方法可知，该方法调用时，appearance对象的preClosed参数会被置为false。
```

```java
package com.example.itext.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class ItextUtil {

    public static final char[] PASSWORD = "123456".toCharArray();//keystory密码

    /**
     * 单多次签章通用
     * @param src
     * @param target
     * @param signatureInfos
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public void sign(String src, String target, SignatureInfo... signatureInfos){
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            inputStream = new FileInputStream(src);
            for (SignatureInfo signatureInfo : signatureInfos) {
                ByteArrayOutputStream tempArrayOutputStream = new ByteArrayOutputStream();
                PdfReader reader = new PdfReader(inputStream);
                //创建签章工具PdfStamper ，最后一个boolean参数是否允许被追加签名
                PdfStamper stamper = PdfStamper.createSignature(reader, tempArrayOutputStream, '\0', null, true);
                // 获取数字签章属性对象
                PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
                appearance.setReason(signatureInfo.getReason());
                appearance.setLocation(signatureInfo.getLocation());
                //设置签名的签名域名称，多次追加签名的时候，签名预名称不能一样，图片大小受表单域大小影响（过小导致压缩）
                appearance.setVisibleSignature(signatureInfo.getFieldName());
                //读取图章图片
                Image image = Image.getInstance(signatureInfo.getImagePath());
                appearance.setSignatureGraphic(image);
                appearance.setCertificationLevel(signatureInfo.getCertificationLevel());
                //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
                appearance.setRenderingMode(signatureInfo.getRenderingMode());
                // 摘要算法
                ExternalDigest digest = new BouncyCastleDigest();
                // 签名算法
                ExternalSignature signature = new PrivateKeySignature(signatureInfo.getPk(), signatureInfo.getDigestAlgorithm(), null);
                // 调用itext签名方法完成pdf签章
                MakeSignature.signDetached(appearance, digest, signature, signatureInfo.getChain(), null, null, null, 0, signatureInfo.getSubfilter());
                //定义输入流为生成的输出流内容，以完成多次签章的过程　　　　　　　　　inputStream = new ByteArrayInputStream(tempArrayOutputStream.toByteArray());
                result = tempArrayOutputStream;
            }
            outputStream = new FileOutputStream(new File(target));
            outputStream.write(result.toByteArray());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null!=outputStream){
                    outputStream.close();
                }
                if(null!=inputStream){
                    inputStream.close();
                }
                if(null!=result){
                    result.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            ItextUtil app = new ItextUtil();
            //将证书文件放入指定路径，并读取keystore ，获得私钥和证书链
            String pkPath = app.getClass().getResource("/template/zhengshu.p12").getPath();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(pkPath), PASSWORD);
            String alias = ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias, PASSWORD);
            Certificate[] chain = ks.getCertificateChain(alias);
            String src = app.getClass().getResource("/template/check.pdf").getPath();     　　　　　//封装签章信息
            SignatureInfo info = new SignatureInfo();
            info.setReason("理由");
            info.setLocation("位置");
            info.setPk(pk);
            info.setChain(chain);
            info.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
            info.setDigestAlgorithm(DigestAlgorithms.SHA1);
            info.setFieldName("sig1");
            info.setImagePath(app.getClass().getResource("/template/yinzhang.png").getPath());
            info.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);

            SignatureInfo info1 = new SignatureInfo();
            info1.setReason("理由1");
            info1.setLocation("位置1");
            info1.setPk(pk);
            info1.setChain(chain);
            info1.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
            info1.setDigestAlgorithm(DigestAlgorithms.SHA1);
            info1.setFieldName("sig2");
            info1.setImagePath(app.getClass().getResource("/template/yinzhang.png").getPath());
            info1.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);

            app.sign(src, "D://sign.pdf", info,info1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
```

至此，电子签章的代码整理完毕。