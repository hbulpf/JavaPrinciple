# 注解@CrossOrigin允许跨域

## 一. 注解@CrossOrigin

出于安全原因，浏览器禁止Ajax调用驻留在当前原点之外的资源。例如，当你在一个标签中检查你的银行账户时，你可以在另一个选项卡上拥有EVILL网站。来自EVILL的脚本不能够对你的银行API做出Ajax请求（从你的帐户中取出钱！）使用您的凭据。


跨源资源共享（CORS）是由大多数浏览器实现的W3C规范，允许您灵活地指定什么样的跨域请求被授权，而不是使用一些不太安全和不太强大的策略，如IFRAME或JSONP。


### 跨域(CORS)支持

Spring Framework 4.2 GA为CORS提供了第一类支持，使您比通常的基于过滤器的解决方案更容易和更强大地配置它。所以springMVC的版本要在4.2或以上版本才支持@CrossOrigin

## 二. 使用方法

### controller配置CORS

#### controller方法的CORS配置

您可以向@RequestMapping注解处理程序方法添加一个@CrossOrigin注解，以便启用CORS（默认情况下，@CrossOrigin允许在@RequestMapping注解中指定的所有源和HTTP方法）：

```
@RestController
@RequestMapping("/account")
public class AccountController {

    @CrossOrigin
    @GetMapping("/{id}")
    public Account retrieve(@PathVariable Long id) {
        // ...
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        // ...
    }
}
```


其中@CrossOrigin中的2个参数：

**origins** ： 允许可访问的域列表

**maxAge**：准备响应前的缓存持续的最大时间（以秒为单位）。

#### 为整个controller启用@CrossOrigin

```
@CrossOrigin(origins = "http://domain2.com", maxAge = 3600)
@RestController
@RequestMapping("/account")
public class AccountController {

    @GetMapping("/{id}")
    public Account retrieve(@PathVariable Long id) {
        // ...
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        // ...
    }
}
```

在这个例子中，对于retrieve()和remove()处理方法都启用了跨域支持，还可以看到如何使用@CrossOrigin属性定制CORS配置。

#### 同时使用controller和方法级别的CORS配置

Spring将合并两个注释属性以创建合并的CORS配置。

```
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/account")
public class AccountController {

    @CrossOrigin(origins = "http://domain2.com")
    @GetMapping("/{id}")
    public Account retrieve(@PathVariable Long id) {
        // ...
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        // ...
    }
}
```

**如果正在使用Spring Security，请确保在Spring安全级别启用CORS，并允许它利用Spring MVC级别定义的配置。**

```
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()...
    }
}
```

### 全局CORS配置

除了细粒度、基于注释的配置之外，您还可能需要定义一些全局CORS配置。这类似于使用筛选器，但可以声明为Spring MVC并结合细粒度@CrossOrigin配置。默认情况下，所有origins and GET, HEAD and POST methods是允许的。

JavaConfig

使整个应用程序的CORS简化为：

```
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
```
如果您正在使用Spring Boot，建议将WebMvcConfigurer bean声明如下：

```
@Configuration
public class MyConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
}
```

您可以轻松地更改任何属性，以及仅将此CORS配置应用到特定的路径模式：

```
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOrigins("http://domain2.com")
        .allowedMethods("PUT", "DELETE")
            .allowedHeaders("header1", "header2", "header3")
        .exposedHeaders("header1", "header2")
        .allowCredentials(false).maxAge(3600);
}
```

如果您正在使用Spring Security，请确保在Spring安全级别启用CORS，并允许它利用Spring MVC级别定义的配置。

### XML命名空间

还可以将CORS与MVC XML命名空间配置。

a、如果整个项目所有方法都可以访问，则可以这样配置；此最小XML配置使CORS在/**路径模式具有与JavaConfig相同的缺省属性：

```
<mvc:cors>
    <mvc:mapping path="/**" />
</mvc:cors>
```

> 其中 * 表示匹配到下一层；** 表示后面不管有多少层，都能匹配。

如：

```
<mvc:cors>  
    <mvc:mapping path="/api/*"/>  
</mvc:cors>  
```

这个可以匹配到的路径有：

/api/aaa

/api/bbbb

不能匹配的：

/api/aaa/bbb

因为* 只能匹配到下一层路径，如果想后面不管多少层都可以匹配，配置如下：

```
<mvc:cors>  
    <mvc:mapping path="/api/**"/>  
</mvc:cors>  
```

注：其实就是一个(*)变成两个(**)

b、也可以用定制属性声明几个CORS映射：

```
<mvc:cors>

    <mvc:mapping path="/api/**"
        allowed-origins="http://domain1.com, http://domain2.com"
        allowed-methods="GET, PUT"
        allowed-headers="header1, header2, header3"
        exposed-headers="header1, header2" allow-credentials="false"
        max-age="123" />

    <mvc:mapping path="/resources/**"
        allowed-origins="http://domain1.com" />

</mvc:cors>
```



请求路径有/api/，方法示例如下：

```
@RequestMapping("/api/crossDomain")  
@ResponseBody  
public String crossDomain(HttpServletRequest req, HttpServletResponse res, String name){  
    ……  
    ……  
} 
```

c、如果使用Spring Security，不要忘记[在Spring安全级别启用CORS](https://docs.spring.io/spring-security/site/docs/current/reference/html/cors.html)：

```
<http>
    <!-- Default to Spring MVC's CORS configuration -->
    <cors />
    ...
</http>
```



### How does it work?

CORS请求（包括预选的带有选项方法）被自动发送到注册的各种HandlerMapping 。它们处理CORS准备请求并拦截CORS简单和实际请求，这得益于CorsProcessor实现（默认情况下默认[DefaultCorsProcessor](https://github.com/spring-projects/spring-framework/blob/master/spring-web/src/main/java/org/springframework/web/cors/DefaultCorsProcessor.java)处理器），以便添加相关的CORS响应头（如Access-Control-Allow-Origin）。 [CorsConfiguration](https://docs.spring.io/spring/docs/4.2.x/javadoc-api/org/springframework/web/cors/CorsConfiguration.html) 允许您指定CORS请求应该如何处理：允许origins, headers, methods等。

a、`AbstractHandlerMapping#setCorsConfiguration()` 允许指定一个映射，其中有几个[CorsConfiguration](https://docs.spring.io/spring/docs/4.2.x/javadoc-api/org/springframework/web/cors/CorsConfiguration.html) 映射在路径模式上，比如/api/**。

b、子类可以通过重写 `AbstractHandlerMapping` 类的 `getCorsConfiguration(Object, HttpServletRequest)` 方法来提供自己的`CorsConfiguration`。

c、处理程序可以实现 [`CorsConfigurationSource`](https://docs.spring.io/spring/docs/4.2.x/javadoc-api/org/springframework/web/cors/CorsConfigurationSource.html)接口（如[`ResourceHttpRequestHandler`](https://github.com/spring-projects/spring-framework/blob/master/spring-webmvc/src/main/java/org/springframework/web/servlet/resource/ResourceHttpRequestHandler.java)），以便为每个请求提供一个[CorsConfiguration](https://docs.spring.io/spring/docs/4.2.x/javadoc-api/org/springframework/web/cors/CorsConfiguration.html)。

### 基于过滤器的CORS支持
作为上述其他方法的替代，Spring框架还提供了[CorsFilter](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/filter/CorsFilter.html)。在这种情况下，不用使用`@CrossOrigin或``WebMvcConfigurer#addCorsMappings(CorsRegistry)`,，例如，可以在Spring Boot应用程序中声明如下的过滤器：

```
@Configuration
public class MyConfiguration {

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://domain1.com");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}
```

## 三. spring注解@CrossOrigin不起作用的原因

1、是springMVC的版本要在4.2或以上版本才支持@CrossOrigin

2、非@CrossOrigin没有解决跨域请求问题，而是不正确的请求导致无法得到预期的响应，导致浏览器端提示跨域问题。

3、在Controller注解上方添加@CrossOrigin注解后，仍然出现跨域问题，解决方案之一就是：

在@RequestMapping注解中没有指定Get、Post方式，具体指定后，问题解决。

类似代码如下：

```
@CrossOrigin
@RestController
public class person{
    
    @RequestMapping(method = RequestMethod.GET)
    public String add() {
        // 若干代码
    }
}
```


## 参考

1. [官方文档](https://spring.io/blog/2015/06/08/cors-support-in-spring-framework)
2. http://fanshuyao.iteye.com/blog/2384189
3. https://blog.csdn.net/taiyangnimeide/article/details/78305131
4. https://blog.csdn.net/snowin1994/article/details/53035433