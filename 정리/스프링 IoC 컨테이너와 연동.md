## 스프링 IoC 컨테이너와 연동

### 서블릿 애플리케이션 스프링 연동하기

- 서블릿에서 **스프링이 제공하는 IoC 컨테이너를 활용하는 방법**
- 스프링이 제공하는 서블릿 구현체 DispatcherServlet을 사용하기



### 서블릿에서 스프링이 제공하는 IoC 컨테이너 활용하는 방법

- **pom.xml에 dependency 추가**

  ~~~xml
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.1.3.RELEASE</version>
  </dependency>
  ~~~

- **리스너 추가**

  ~~~xml
  <listener>
      <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
  ~~~

  - 애플리케이션 컨텍스트를 서블릿 애플리케이션의 생명주기에 맞춰서 바인딩 해주는 것이다.

  - 등록되어 있는 서블릿이 사용될 수 있도록 **애플리케이션 컨텍스트를 만들어 서블릿 컨텍스트에 등록을 해준다.**

  - 즉, 애플리케이션 컨텍스트를 만들어야 하므로, 스프링 설정 파일이 필요하다.

    - Java 설정 파일을 사용해보자

    ~~~xml
    <listener>
      <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    
    <context-param>
      <param-name>contextClass</param-name>
      <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
    </context-param>
    
    <context-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>com.mesung.AppConfig</param-value>
    </context-param>
    ~~~

    - ContextLoaderListener가 **AnnotationConfigWebApplicationContext를 만들 때 ContextConfigLocation의 정보(빈 등록려는 패키지 위치)를 참고하여 만들게 된다.**

    

    ~~~java
    @Configuration
    @ComponentScan
    public class AppConfig {
    }
    
    @Service
    public class HelloService {
    
        public String getName() {
            return "mesung";
        }
    }
    ~~~

    - 그로인해, 그 애플리케이션 컨텍스트 안에는 우리가 설정한 HelloServie가 빈으로 등록되어 있는 것이다.

    

    - 또한, 애플리케이션 컨텍스트는 서블릿 컨텍스트에 등록을 하는데, 등록된 애플리케이션 컨텍스트를 꺼내오는 방법을 살펴보자

      ~~~java
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);	//서블릿 컨텍스트에서 애플리케이션 컨텍스트를 꺼내온다.
      
        //애플리케이션 컨텍스트에서 service의 빈을 꺼내온다.
      	HelloService helloService = context.getBean(HelloService.class);
        
        String name = helloService.getName();
      
      }
      ~~~

      

- 이어서 계속..