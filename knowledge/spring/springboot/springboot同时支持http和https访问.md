# springboot支持http和https访问

**前言**

关于spring boot同时支持http和https访问，在spring boot官网已经有说明文档了，同样在github上也有官网的[例子](https://github.com/spring-projects/spring-boot/tree/v1.5.9.RELEASE/spring-boot-samples/spring-boot-sample-tomcat-multi-connectors)。

在这里，我向大家讲述一下，我是怎么实现的。

## 方式一

一、生成证书

使用Java keytool工具生成证书,[利用keytool工具生成数字证书](利用keytool工具生成数字证书)

```
keytool -genkey -alias test -dname "CN=Andy,OU=kfit,O=kfit,L=HaiDian,ST=BeiJing,C=CN"  -keyalg RSA -keysize 1024 -keystore test.jks -validity 365
```

设置JKS的密钥库口令为 `secret` , 设置密钥口令 `password`

查看JKS中生成的证书的详细信息

```
keytool -list -v -keystore test.jks
```

导出证书，并可以双击打开证书查看证书信息

```
keytool -alias test -exportcert -keystore test.jks -file test.cer
```

二、相关配置

```
server:
  port: 4000

https:
  port: 8443
  ssl:
    key-store: classpath:sample.jks
    key-store-password: secret
    key-password: password
```

可以看到，只是简简单单添加端口的信息，sample.jks可以自己生成（记得对应密码），也可以在官网例子里面[下载](https://github.com/spring-projects/spring-boot/raw/v1.5.9.RELEASE/spring-boot-samples/spring-boot-sample-tomcat-multi-connectors/src/main/resources/sample.jks)。

三、spring boot启动文件读取配置信息（注：请添加必要的jar）

```
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Value("${https.port}")
    private Integer port;

    @Value("${https.ssl.key-store-password}")
    private String key_store_password;

    @Value("${https.ssl.key-password}")
    private String key_password;　　　　
    /* --------------------请按照自己spring boot版本选择 start--------------------- */
　　// 这是spring boot 1.5.X以下版本的 添加了这个，下一个就不用添加了
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector()); // 添加http
        return tomcat;
    }　　　　
    // 这是spring boot 2.0.X版本的 添加这个，上一个就不用添加了　　
    @Bean　　
    public ServletWebServerFactory servletContainer() {　　　　
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        // 添加http　　　　　
        tomcat.addAdditionalTomcatConnectors(createSslConnector()); 　　　
        return tomcat;　　
    }
　　/* -------------------请按照自己spring boot版本选择 end---------------------- */
 
 　　// 配置https
    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            File keystore = new ClassPathResource("sample.jks").getFile();
            /*File truststore = new ClassPathResource("sample.jks").getFile();*/
            connector.setScheme("https");
            connector.setSecure(true);
            connector.setPort(port);
            protocol.setSSLEnabled(true);
            protocol.setKeystoreFile(keystore.getAbsolutePath());
            protocol.setKeystorePass(key_store_password);
            protocol.setKeyPass(key_password);
            return connector;
        }
        catch (IOException ex) {
            throw new IllegalStateException("can't access keystore: [" + "keystore"
                    + "] or truststore: [" + "keystore" + "]", ex);
        }
    }
}
```



启动项目后，可以看到两个端口，说明已经成功

![img](2.png)

## 方式二

一、相关配置

```
server:
  port: 8443
  ssl:
    key-store: classpath:sample.jks
    key-store-password: secret
    key-password: password

http:
  port: 8080
```

二、spring boot启动文件读取配置信息（注：请添加必要的jar）

```
@SpringBootApplication
public class SampleTomcatTwoConnectorsApplication {

    @Value("${http.port}")
    private Integer port;

　　/* --------------------请按照自己spring boot版本选择 start--------------------- */
　　　　// 这是spring boot 1.5.X以下版本的 添加了这个，下一个就不用添加了
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector()); // 添加http
        return tomcat;
    }　　　　
    // 这是spring boot 2.0.X版本的 添加这个，上一个就不用添加了　　
    @Bean　　
    public ServletWebServerFactory servletContainer() {　　　　
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();　　　　
        // 添加http　　　　
        tomcat.addAdditionalTomcatConnectors(createStandardConnector()); 
        return tomcat;　　}
    /* --------------------请按照自己spring boot版本选择 end--------------------- */
 
　　// 配置http
    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(port);
        return connector;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleTomcatTwoConnectorsApplication.class, args);
    }

}
```

启动项目后，可以看到两个端口，说明已经成功

![img](2.png)

 

## 方式三

一、首先要生成证书

如果配置了JAVA开发环境，可以使用keytool命令生成证书。我们打开控制台，输入：

```bash
keytool -genkey -alias tomcat -dname "CN=Andy,OU=kfit,O=kfit,L=HaiDian,ST=BeiJing,C=CN" -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 365
```

输入后会提示输入密码 `123123`，这个密码在下面配置文件有用到。

生成后，在生成目录找到证书文件，复制到SpringBoot应用的src/main/resources下。

二、application.yml相关配置

```
server:
  tomcat:
    uri-encoding: UTF-8
    max-threads: 1000
    min-spare-threads: 30
  port: 8087
  connection-timeout: 5000ms
  servlet:
    context-path: /
  ssl:
    key-store: classpath:keystore.p12
    key-alias: tomcat
    key-store-type: PKCS12
    key-store-password: 123123
http: # 新加一个http的端口号配置
  port: 8086
```


三、spring boot启动文件读取配置信息（注：请添加必要的jar）

```
@Configuration
public class HttpsConfig {
    @Value("${http.port}")
    private Integer httpPort;

    @Value("${server.port}")
    private Integer httpsPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                // 如果要强制使用https，请松开以下注释
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(createStandardConnector()); // 添加http
        return tomcat;
    }

    /**
     * 配置http
     * @return
     */
    private Connector createStandardConnector() {
        // 默认协议为org.apache.coyote.http11.Http11NioProtocol
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setSecure(false);
        connector.setScheme("http");
        connector.setPort(httpPort);
        // 当http重定向到https时的https端口号
        connector.setRedirectPort(httpsPort);
        System.out.println(String.format("%d=>%d", httpPort, httpPort));
        return connector;
    }
}
```

## 总结

对比两种方法可以看出方式二比方式一简单一点，主要是因为方式二用的代码比较少，我也不知道这两种方式有什么区别，我自己测试过，无论是spring boot还是spring cloud,这两个方式都没问题，就算是方式二，同样可以帮服务注册到eureka上。不一样的是方式一注册到eureka的端口是4000，方式二注册到eureka的端口是8443。作为一个强迫的人士，在我自己的项目上，用的方式二，因为我的eureka用的http注册服务。如果你只是spring boot，当然选择少一点代码的方式二啦。

# 参考

1. [spring cloud/spring boot同时支持http和https访问](https://www.cnblogs.com/lianggp/p/8136540.html)
2. [SSL在线工具](http://www.ssleye.com/)
3. [腾讯云SSL申请](https://cloud.tencent.com/product/ssl)