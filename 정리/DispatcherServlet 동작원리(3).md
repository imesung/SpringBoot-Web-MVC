## DispatcherServlet 동작원리(3)



**DispatcherServlet의 ViewResolver**

- DispatcherServlet 동작과정에서 viewResolver의 과정을 살펴보자

~~~java
//DispatcherServlet.class
protected void initStrategies(ApplicationContext context) {
  ...
  //0. init은 최초의 서블릿을 initializer할 때만 접근한다.
  this.initViewResolvers(context);
  ...
}


private void initViewResolvers(ApplicationContext context) {
  this.viewResolvers = null;
  if (this.detectAllViewResolvers) {
    
    //1. viewResolver 타입을 모두 찾아온다.
    Map<String, ViewResolver> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, ViewResolver.class, true, false);
    
    if (!matchingBeans.isEmpty()) {
      this.viewResolvers = new ArrayList(matchingBeans.values());
      AnnotationAwareOrderComparator.sort(this.viewResolvers);
    }
  } else {
    try {
      
			//2. 특정 viewResolver 타입을 찾아온다. (잘 사용하지 않는다.)
      ViewResolver vr = (ViewResolver)context.getBean("viewResolver", ViewResolver.class);
      
      this.viewResolvers = Collections.singletonList(vr);
    } catch (NoSuchBeanDefinitionException var3) {
    }
  }

  
  if (this.viewResolvers == null) {
    
    //3. 기본 viewResolver(prefix, suffix)가 등록되어 있지 않다면 기본 viewResolver를 찾아온다.
    this.viewResolvers = this.getDefaultStrategies(context, ViewResolver.class);
    
    if (this.logger.isTraceEnabled()) {
      this.logger.trace("No ViewResolvers declared for servlet '" + this.getServletName() + "': using default strategies from DispatcherServlet.properties");
    }
  }

}
~~~

- viewResolver가 커스텀되어 있다면, 사용자 설정에 맞게 view를 노출해주고, 커스텀 되어있지 않는다면 기본 viewResolver를 사용하여 화면에 노출(*사용자는 controller return 값에 절대경로 및 확장자까지 모두 작성해줘야 한다.*)해준다.



*ViewResolver 및 HandlerMapping, HandlerAdapter 등이 없을 시 해당 properties에 명시 (기본 viewResolver 세팅 위치)*

~~~properties
#DispatcherServlet.properties
...

org.springframework.web.servlet.HandlerMapping=org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping,\
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

org.springframework.web.servlet.HandlerAdapter=org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter,\
	org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter,\
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter

org.springframework.web.servlet.ViewResolver=org.springframework.web.servlet.view.InternalResourceViewResolver
...
~~~



*그럼 커스텀 ViewResolver를 만들어보자*

- **커스텀 Bean을 등록하자 (ViewResolver)**

  ~~~java
  @Configuration
  @ComponentScan
  public class WebConfig {
    @Bean
    public ViewResolver viewResolver() {
      //기본적으로 제공되는 ViewResolver의 설정은 커스텀해보자
      InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
      
      //return 값의 경로와 확장자를 미리 지정한다.
      viewResolver.setPrefix("/WEB-INF/");	//절대경로
      viewResolver.setSuffix(".jsp");				//확장자
      
      return viewResolver;
    }
  }
  ~~~

- **ViewResolver 설정에 따른 Controller의 변화**

  ~~~java
  //SimpleController
  @Controller("/simple")
  public class SimpleController implements org.springframework.web.servlet.mvc.Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
      
      // /WEB-INF/simple.jsp에서 변경됨.
      return new ModelAndView("simple");
    }
  }
  
  //HelloController
  @Controller
  public class HelloController {
  
    ...
  
    @GetMapping("/sample")
    public String sample() {
      
      // /WEB-INF/simple.jsp
      return "sample";
    }
    
  }
  ~~~

- **디버깅을 통해 initViewResolver가 어떤 조건문에서 실행되나 확인해보자.**

  ~~~java
  private void initViewResolvers(ApplicationContext context) {
    this.viewResolvers = null;
    
    if (this.detectAllViewResolvers) {
      Map<String, ViewResolver> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, ViewResolver.class, true, false);
      
      //1. 현재 우리는 Bean을 등록해놓았으므로(viewResolver()) 이 조건을 타게 된다.
      if (!matchingBeans.isEmpty()) {
        this.viewResolvers = new ArrayList(matchingBeans.values());
        AnnotationAwareOrderComparator.sort(this.viewResolvers);
      }
    } else {
      try {
        ViewResolver vr = (ViewResolver)context.getBean("viewResolver", ViewResolver.class);
        this.viewResolvers = Collections.singletonList(vr);
      } catch (NoSuchBeanDefinitionException var3) {
      }
    }
  
    //2. 현재 우리는 viewResovler에서 prefix와 suffix를 등록해놓았기 때문에 해당 조건은 타지 않게 된다.
    if (this.viewResolvers == null) {
      this.viewResolvers = this.getDefaultStrategies(context, ViewResolver.class);
      if (this.logger.isTraceEnabled()) {
        this.logger.trace("No ViewResolvers declared for servlet '" + this.getServletName() + "': using default strategies from DispatcherServlet.properties");
      }
    }
  
  }
  ~~~

  



