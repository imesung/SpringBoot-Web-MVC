## 스프링 MVC 구성 요소

![image](https://user-images.githubusercontent.com/40616436/76861024-48482d80-689f-11ea-84d8-8637f46399a1.png)



- DispatcherServlet 기본전략

  - DispatcherServlet.properties
  - DispatcherServlet의 기본 값 목록

- MultipartResolver

  - 파일 업로드 요청 처리에 필요한 인터페이스
  - HttpServletRequest를 MultipartHttpServletRequest로 변환해주어 File을 꺼낼 수 있는 API를 제공한다.

- LocaleResolver

  - 클라이언트의 위치 정보를 파악하는 인터페이스
  - 기본 값은 요청의 accept-language를 보고 판단한다.

- ThemeResolver

  - 애플리케이션에 설정된 테마를 변경할 수 있는 인터페이스

- **HandlerMapping**

  - 요청을 처리할 핸들러를 찾는 인터페이스
  - controller의 url을 찾는 인터페이스

  ~~~properties
  #기본값
  org.springframework.web.servlet.HandlerMapping=org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping,\
  	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
  ~~~

  - 주로 RequestMappingHandlerMapping를 본다.

- **HandlerAdapter**

  - HandlerMapping이 찾아낸 핸들러를 처리하는 인터페이스
  - 스프링 MVC 확장력의 핵심이다.

  ~~~properties
  org.springframework.web.servlet.HandlerAdapter=org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter,\
  	org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter,\
  	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
  ~~~

  - 주로 RequestMappingHandlerAdapter를 본다.

- HandlerExceptionResolver

  - 요청 처리 중에 발생한 에러를 처리하는 인터페이스

- RequestToViewNameTranslator

  - 핸들러에서 뷰 이름을 명시적으로 리턴하지 않은 경우, 요청을 기반으로 뷰 이름을 판단하는 인터페이스
  - url과 동일한 뷰를 호출하게 된다.
    - Ex. @GetMapping("/hello") -> hello.jsp 호출

- ViewResolver

  - 뷰 이름에 해당하는 뷰를 찾아내는 인터페이스

- FlashMapManager

  - FlashMap은 주로 리다이렉션을 사용할 때 요청 매개변수를 사용하지 않고 데이터를 전달하고 정리할 때 사용한다. 