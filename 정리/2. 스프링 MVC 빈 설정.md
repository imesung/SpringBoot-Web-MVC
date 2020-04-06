##  스프링 MVC구성 요소 직접 빈으로 등록하기

**스프링 기본 전략**

- 스프링은 기본적인 설정이 없어도 스프링이 제공해주는 DispatcherServlet.propeties를 통해 기본 전략을 사용하고 있다.

  ~~~properties
  org.springframework.web.servlet.LocaleResolver=org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
  
  org.springframework.web.servlet.ThemeResolver=org.springframework.web.servlet.theme.FixedThemeResolver
  
  org.springframework.web.servlet.HandlerMapping=org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping,\
  	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
  
  org.springframework.web.servlet.HandlerAdapter=org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter,\
  	org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter,\
  	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
  
  org.springframework.web.servlet.HandlerExceptionResolver=org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver,\
  	org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver,\
  	org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
  
  org.springframework.web.servlet.RequestToViewNameTranslator=org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator
  
  org.springframework.web.servlet.ViewResolver=org.springframework.web.servlet.view.InternalResourceViewResolver
  
  org.springframework.web.servlet.FlashMapManager=org.springframework.web.servlet.support.SessionFlashMapManager
  ~~~

- 하지만 해당 기본적인 전략들은 각각 가지고 있는 클래스의 기본값으로 설정이 되어, 우리가 원하는 설정을 하지 못하는 경우가 있다.

  - Ex. ViewResolver의 경우 prefix와 suffix가 설정이 안 되어 있는 경우

    ~~~java
    @Bean
    public ViewResolver viewResolver() {
      InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
      viewResolver.setPrefix("/WEB-INF/");
      viewResolver.setSuffix(".jsp");
      return viewResolver;
    }
    ~~~

    

**기본적인 전략을 커스텀해보자**

~~~java
@Bean
public HandlerMapping handlerMapping() {
  //기본전략인 HandlerMapping을 빈으로 등록.
  RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
  handlerMapping.setInterceptors();
  handlerMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);	//우선순위 결정
  return handlerMapping;
}

@Bean
public HandlerAdapter handlerAdapter() {
  RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
  handlerAdapter.setArgumentResolvers(handlerAdapter.getCustomArgumentResolvers());	//메소드 파라미터에 넣을 것들을 설정할 수 있다.
  return handlerAdapter;
}
~~~

~~~java
//HandlerAdapter setArgumentResolvers 설정 시 @PathVariable, @RequestParam, @ModelAttribute.. 사용 가능
@GetMapping("/hello/{id}")
@ResponseBody
public String hello(@PathVariable int id, @RequestParam String name) {
  return "hello, " + helloService.getName();
}
~~~



**@Configuration을 사용한 자바 설정 파일에 직접 @Bean을 사용해서 등록하기**

- 결과적으로, 모든 것을 일일히 설정하기 위해서는 DispatcherServlet을 모두 Bean으로 등록해야한다. 기본적인 전략을 그대로 사용하기에는 한계가 있는 것이다.
- Ex. Json 파싱을 받는 것도 기본적인 전략으로도 안된다.
- 하지만, 현재 우리는 일일히 Bean을 설정하지 않는다.
  - 즉, **스프링 MVC가 Bean을 좀 더 간편하게 설정하기 방법을 제공했다. 다음 포스팅에서 살펴보자** 

