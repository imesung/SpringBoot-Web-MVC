## 스프링 MVC 연동(Dispatcher Servlet)

**DispatcherServlet 사용**

~~~java
@RestController
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String hello() {
        return "hello, " + helloService.getName();
    }
}
~~~

- **DispatcherServlet은 언제 사용하나?**
  - Spring MVC로 웹 애플리케이션을 구동하고 싶다.
  - @GetMapping과 @RestController 같은 어노테이션을 이해하고 있고, Controller에서 URL를 받을 수 있는 핸들러를 사용하고 싶다.
  - return 값이 이름이 되는 ui 페이지 전환이 되고 싶다. return을 http 응답으로 만들고 싶다.



**DispatcherServlet을 사용하기 위한 설정**

- web.xml에서 servlet 설정

~~~xml
<servlet>
  <servlet-name>app</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  
  <!-- 애플리케이션 컨텍스트에 모든 Bean을 등록 -->
  <init-param>
    <param-name>contextClass</param-name>
    <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContex</param-value>
  </init-param>
  
  <init-param>
    <param-name>contextConfigLoaction</param-name>
    <param-value>com.mesung.WebConfig</param-value>
  </init-param>
</servlet>

<servlet>
  <servlet-name>app</servlet-name>
  <servlet-class>/app/*</servlet-class>
</servlet>
~~~



- Java 페이지

~~~java
//애플리케이션 컨텍스트를 만들 때 사용하는 설정 정보
@Configuration
@ComponentScan
public class WebConfig {
  //@Configuration : 해당 클래스가 설정 정보라는 의미이다.
  //@ComponentScan : 해당 클래스가 있는 패키지 내에 빈 등록을 원하는 것들을 모두 등록한다.
}

//controller
@RestController
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String hello() {
        return "hello, " + helloService.getName();
    }
}
~~~



