## 스프링 MVC 동작 원리 정리

### **스프링 부트를 활용한 MVC 동작**

- SpringBoot Main

~~~java
@SpringBootApplication
public class DemoSpringMvcApplication {

    public static void main(String [] args) {
        SpringApplication.run(DemoSpringMvcApplication.class);
    }
}
~~~



- Controller

~~~java
@Controller
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping("/events")
    public String events(Model model) {
        model.addAttribute("events", eventService.getEvent());
        return "events";
    }

}
~~~



- View

~~~html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h1>이벤트 목록</h1>
    <table>
        <tr>
            <th>이름</th>
            <th>참가 인원</th>
            <th>시작</th>
            <th>종료</th>
        </tr>
        <tr th:each="event: ${events}">
            <td th:text="${event.name}">이벤트 이름</td>
            <td th:text="${event.limitOfEnrollment}">100</td>
            <td th:text="${event.startDateTime}">2020년 2월 5일 오전 10시</td>
            <td th:text="${event.endDateTime}">2020년 2월 5일 오전 10시</td>
        </tr>
    </table>
</body>
</html>
~~~



- 스프링 부트 기반의 웹 MVC는 서블릿 기반으로 동작하며, 서블릿 컨테이너가 필요하는 것을 살펴보았다.



### 스프링 부트를 사용하지 않는 스프링 MVC

- 서블릿을 활용한 웹 MVC를 작성하여 스프링 부트의 동작원리를 파악해보았다.
  - 스프링 IoC 컨테이너와 연동 및 스프링 MVC연동
- 해당 부분은 서블릿 컨테이너에 등록한 웹 애플리케이션에 DispatcherServlet을 등록했다.
  - web.xml에 서블릿 등록
  - Java를 통해 서블릿 등록



**애플리케이션 컨텍스트 설정파일**

~~~java
@Configuration
@ComponentScan
public class WebConfig {
  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setPrefix("/WEB-INF/");
    viewResolver.setSuffix(".jsp");
    return viewResolver;
  }
}
~~~



**web.xml에 서블릿 등록**

~~~xml
<servlet>
  <servlet-name>app</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
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



**Java를 통해 서블릿 등록**

~~~java
public class WebApplication implements WebApplicationInitializer {
  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    context.register(WebConfig.class);
    context.refresh();

    DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
    ServletRegistration.Dynamic app = servletContext.addServlet("app", dispatcherServlet);
    app.addMapping("/app/*");
  }
}
~~~



### 스프링 부트를 사용하는 스프링 MVC

- 자바 애플리케이션에 내장 톰켓을 만들고 그 안에 DispatcherServlet을 등록한다.
  - 스프링 부트의 자동 설정이 자동으로 해준다.
- 스프링 부트의 주관에 따라 여러 인터페이스 구현체를 빈으로 등록한다.
  - **DispatcherServlet에 관련된 인터페이스의 구현체가 대부분 빈으로 등록되어 있다.**
  - **이로 인해 스프링 MVC를 좀 더 편리하게 사용할 수 있는 것이다.**