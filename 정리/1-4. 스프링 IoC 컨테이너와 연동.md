## 스프링 IoC 컨테이너와 연동

### 서블릿 애플리케이션 스프링 연동하기

- 지금까지 만든 Servlet을 활용하여 스프링이 제공하는 IoC 컨테이너와 연동해보자.



### 서블릿에서 스프링이 제공하는 IoC 컨테이너 활용하는 방법

***애플리케이션 컨텍스트를 서블릿 컨텍스트에 등록 후 IoC 컨테이너를 활용해보자.***

- **pom.xml에 dependency 추가**

  - 스프링이 제공하는 IoC 컨테이너를 활용하기 위해선 의존성이 필요하다(spring-webmvc)

  ~~~xml
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.1.3.RELEASE</version>
  </dependency>
  ~~~

- **리스너 추가**

  - 우리가 만든 서블릿 애플리케이션의 생명주기에 맞추어 애플리케이션 컨텍스트(IoC 컨테이너)를 등록해주기 위해서는 스프링이 제공해주는 리스너가 필요하다.

  ~~~xml
  <listener>
      <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
  ~~~

  - ***ContextLoaderListener는 애플리케이션 컨텍스트를 만들어 서블릿 컨텍스트에 등록하는 역할을 한다.***
  - 애플리케이션 컨텍스트를 서블릿 애플리케이션의 생명주기에 맞춰서 바인딩 해주는 것이다.
  - **등록되어 있는 서블릿이 사용될 수 있도록 ContextLoaderListener를 활용하여 애플리케이션 컨텍스트를 만들어 서블릿 컨텍스트에 등록을 해준다.**

- **Java 설정 파일 추가**

  - **ContextLoaderListener가 애플리케이션 컨텍스트(AnnotationConfigWebApplicationContext)를 등록할 때  Java 설정 파일을 참고하여 애플리케이션 컨텍스트를 등록한다.**
  
  ~~~xml
  <!-- 애플리케이션 컨텍스트 등록 -->
  <context-param>
    <param-name>contextClass</param-name>
    <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
  </context-param>
  
  <!-- Java 설정 파일 위치 -->
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>com.mesung.AppConfig</param-value>
  </context-param>
  ~~~
  
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
  
  - **ContextLoaderListener**가 **AnnotationConfigWebApplicationContext를 만들 때 ContextConfigLocation의 정보(빈 등록려는 패키지 위치)를 참고하여 만들게 된다.**
  
    ~~~java
    public class ContextLoaderListener extends ContextLoader implements ServletContextListener {
       ...
         
        public void contextInitialized(ServletContextEvent event) {
          //애플리케이션 컨텍스트 등록  
         	this.initWebApplicationContext(event.getServletContext());
        }
    
       ...
  }
    
    
    public class ContextLoader {
      ...
      public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
      	...
        //서블릿 컨텍스트에 애플리케이션 컨텍스트를 등록.  
      	servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);    
    	}
      ...
    }
    
    ~~~
  
    
  
- **애플리케이션 컨텍스트는 서블릿 컨텍스트에 등록 되는데, 등록된 애플리케이션 컨텍스트를 꺼내오는 방법을 살펴보자**

  ~~~java
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
    ApplicationContext context = (ApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);	//서블릿 컨텍스트에서 애플리케이션 컨텍스트를 꺼내온다.
  
    //애플리케이션 컨텍스트에서 service의 빈을 꺼내온다.
  	HelloService helloService = context.getBean(HelloService.class);
    
    String name = helloService.getName();
  
  }
  ~~~

  

**우리가 만든 서블릿을 활용하여 스프링이 제공하는 IoC 컨테이너에 연동을 진행해봤다.**

**하지만, 서블릿을 활용하여 웹 애플리케이션을 구현할 때 페이지가 여러개가 필요할 경우에는 url 하나당 서블릿 하나를 만들게 되고 설정 파일도 추가되는 것을 볼 수 있다.**

- **Url 추가시 추가되는 소스**

  ~~~java
  //서블릿 파일
  public class HelloServlet extends HttpServlet {
  
      @Override
      public void init() throws ServletException {
          System.out.println("init");
      }
  
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
          ApplicationContext context = (ApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
          HelloService helloService = context.getBean(HelloService.class);
          String name = helloService.getName();
  
          System.out.println("doGet");
          resp.getWriter().println("<html>");
          resp.getWriter().println("<head>");
          resp.getWriter().println("<body>");
          resp.getWriter().println("<h1>hello " + getServletContext().getAttribute("name") + "servlet</h1>");
          resp.getWriter().println("</body>");
          resp.getWriter().println("</head>");
          resp.getWriter().println("</html>");
      }
  
      @Override
      public void destroy() {
          System.out.println("destroy");
      }
  
  }
  ~~~

  ~~~xml
  <servlet>
  	<servlet-name>hello</servlet-name>
    <servlet-class>com.mesung.HelloServlet</servlet-class>
  </servlet>
  
  <servlet-mapping>
  	<servlet-name>hello</servlet-name>
    <url-pattern>/hello</url-pattern>
  </servlet-mapping>
  ~~~

- 너무 비효율적으로 많이 추가되고 또한, **만약 여러 서블릿에서 공통적으로 사용하고 싶은 부분이 있을 시에는 Filter를 활용해도 되지만 이 때 등장한 디자인 패턴이 있는데, 그것이 바로 Front Controller이다.**

  - Front Controller는 모든 요청을 Controller 하나가 받고, Controller는 해당 요청들을 처리할 핸들러에게 분배(**Dispatch**)를 한다.
  - **스프링은 Front Controller와 Dispatch 해주는 역할을 구현해놓았다. 그것이 바로 DispatcherServlet이다.**