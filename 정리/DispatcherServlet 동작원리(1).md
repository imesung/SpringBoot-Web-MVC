## DispatcherServlet 동작원리(1)

**DispatcherServlet 초기화**

- HandlerMapping : 핸들러를 찾아주는 인터페이스
- HandlerAdapter : 핸들러를 실행하는 인터페이스
- ReturnValueHandlers : return 값을 http 본문에 넣어주는 핸들러



**HandlerMapping**

- **url에 해당하는 handler를 찾는 인터페이스**
  - 찾는 핸들러 : RequestMappingHandlerMapping

~~~java
//DispatcherServlet.class
@Nullable
protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
  if (this.handlerMappings != null) {
    Iterator var2 = this.handlerMappings.iterator();

    //반복문을 통해 url에 해당하는 핸들러를 찾는다.
    while(var2.hasNext()) {
      HandlerMapping mapping = (HandlerMapping)var2.next();
      HandlerExecutionChain handler = mapping.getHandler(request);
      if (handler != null) {
        return handler;
      }
    }
  }

  return null;
}
~~~





**HandlerAdapter**

- **찾은 handler를 실행할 수 있는 adapter를 찾는 인터페이스**
  - 실행 핸들러 : RequestMappingHandlerAdapter

~~~java
protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
  if (this.handlerAdapters != null) {
    Iterator var2 = this.handlerAdapters.iterator();

    while(var2.hasNext()) {
      HandlerAdapter adapter = (HandlerAdapter)var2.next();
      if (adapter.supports(handler)) {
        return adapter;
      }
    }
  }

  throw new ServletException("No adapter for handler [" + handler + "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
}
~~~

- Java reflection을 활용하여 해당하는 핸들러를 실행하게 되는 것이다.

  ~~~java
  mav = this.invokeHandlerMethod(request, response, handlerMethod);
  ~~~

  - **handlerMethod를 보면 어떤 메소드를 실행해야 하는지가 들어가 있다.**



**ReturnValueHandlers**

- 사용 핸들러 : RequestResponseBodyMethodProcessor

- returnValueHandler : **컨버터를 사용해 리턴값을 http 본문에 넣어주는 역할을 한다.**

  - **hello() 메소드는 @RestController에 의해 @ResponseBody로 인식하여 View 이름으로 확인되지 않아 ModelAndView는 Null이다.**

  ~~~java
  @RestController
  public class HelloController {
  		...
  
      @GetMapping("/hello")
      public String hello() {
          return "hello, " + helloService.getName();
      }
  }
  ~~~

  

> **@Controller와 @RestController의 차이**
>
> *@ResponseBody가 있냐 없냐의 차이이다.*
>
> @Controller
>
> ~~~java
> @Controller
> public class HelloController {
> 		...
> 
>     @GetMapping("/hello")
>     @ResponseBody
>     public String hello() {
>         return "hello, " + helloService.getName();
>     }
> }
> ~~~
>
> 
>
> @RestController
>
> ~~~java
> @RestController
> public class HelloController {
> 		...
> 
>     @GetMapping("/hello")
>     public String hello() {
>         return "hello, " + helloService.getName();
>     }
> }
> ~~~





