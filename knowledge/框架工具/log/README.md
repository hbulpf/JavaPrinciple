# Java 日志工具

## java自带log:java.util.logging.Logger

```
 public class HelloLogWorld {  
   private static String name = HelloLogWorld.class.getName();      
   private static Logger log = Logger.getLogger(name);// <= (2)  
   public void sub() {  
     log.info("Hello Logging World");    // <= (3)  
   }  
   public static void main(String[] args) {  
     HelloLogWorld logWorld = new HelloLogWorld();  
     logWorld.sub();  
   }  
 }  
```

## 使用 org.apache.log4j.Logger;

具体代码：

```
Logger logger=Logger.getLogger(ECntrCbsChargingService.class);

logger.info("success");
```