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

