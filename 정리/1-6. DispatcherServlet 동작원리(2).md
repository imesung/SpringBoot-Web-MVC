## DispatcherServlet 동작원리(2)

**View가 포함된 DispatcherServlet 동작원리**

~~~java
//Controller
@Controller
public class HelloController {
  
  @GetMapping("/sample")
  public String sample() {
    return "/WEB-INF/sample.jsp";
  }	
  
}

~~~



1. *먼저 Localhost:8080/app/sample로 접속*
2. *HandlerMapping*
   - RequestMappingHandlerMapping
   - BeanNameUrlHandlerMapping
3. *HandlerAdapter*
   - RequestMappingHandlerAdapter
   - SimpleControllerHandlerAdapter
4. *ReturnValueHandlers*
   - ModelAndView가 Null일 경우(메소드에 @ResponseBody) 리턴값만 페이지에 보여준다.
   - 하지만, ModelAndView가 Null이 아닌 경우 viewResolver에 의해 리턴값이 view의 이름으로 호출한다. 



*ReturnValueHandlers*

- **@ResponseBody가 없는 경우에는 return되는 값을 View의 이름으로 인식한다.**
- 이런 경우로, **sample() 메소드는 modelAndView가 Null이 아니다.**
  - Model 객체를 바인딩하여 View를 렌더링 한다. 즉, jsp를 호출한다는 것이다.
- **return 되는 값을 View의 이름으로 인식하는 것은 viewResolver라는 녀석으로 인해 view를 호출하는 것이다.**



**또 다른 Handler Mapping**

*BeanNameUrlHandlerMapping*

- **url에 해당하는 handler를 찾는 인터페이스**

  - 찾는 핸들러 : BeanNameUrlHandlerMapping

  ~~~java
  @Controller("/simple")
  public class SimpleController implements org.springframework.web.servlet.mvc.Controller {
  
      @Override
      public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
          //ModelAndView : 모델과 뷰의 정보를 준다.
          return new ModelAndView("WEB-INF/simple.jsp");
      }
  }
  ~~~

  

*SimpleControllerHandlerAdapter*

- 위 Controller같은 핸들러를 처리해주는 어댑터이다.
- 해당 어댑터는  ModelAndView가 Null이 아닌 상태로 return 값을 찾는다.



*viewResolver*

- return 되는 값을 view 이름으로 인식하여 view를 호출한다.



