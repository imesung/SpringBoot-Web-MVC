## 스프링 MVC 동작 원리

### MVC

**모델 : 도메인 객체 또는 DTO로 화면에 전달하거나 화면에서 전달받은 데이터를 담고 있는 객체를 말한다.**

~~~java
//lombok을 활용하여 컴파일 시점에 자동으로 각 메소드들을 생성한다.
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Event {

    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int limitOfEnrollment;
}
~~~

- 평범한 POJO 객체이다.



**뷰 : 데이터를 보여주는 역할로서, 다양한 형태로 보여줄 수 있다.**

~~~java
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

- HTML, JSON, XML



**컨트롤러 : 사용자의 입력을 받아 모델 객체의 데이터를 변경하거나, 모델 객체를 뷰에 전달하는 역할을 한다.**

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

- 입력값 검증
- 입력 받은 데이터로 모델 객체를 변경
- 변경된 모델 객체를 뷰에 전달



**서비스**

~~~java
@Service
public class EventService {

    public List<Event> getEvent() {
        Event event1 = Event.builder()
                .name("Spring Web MVC study 1")
                .limitOfEnrollment(5)
                .startDateTime(LocalDateTime.of(2020, 2, 5, 10, 0))
                .startDateTime(LocalDateTime.of(2020, 2, 5, 11, 0))
                .build();

        Event event2 = Event.builder()
                .name("Spring Web MVC study 2")
                .limitOfEnrollment(5)
                .startDateTime(LocalDateTime.of(2020, 2, 5, 10, 0))
                .startDateTime(LocalDateTime.of(2020, 2, 5, 11, 0))
                .build();

        return List.of(event1, event2);
    }
}
~~~

- 컨트롤러에서 직접적으로 로직을 구성해도 되지만, 컨트롤러의 독립적인 구성을 위해 서비스를 활용하여 로직을 구성한다.



### MVC 패턴의 장점

- 동시 다발적인 개발이 가능하다.
  - 백엔드 개발자와 프론트 엔드 개발자가 독립적으로 개발을 진행할 수 있다.
- 높은 결합도
  - 논리적으로 관련있는 기능을 하나의 컨트롤러로 묶거나, 특정 모델과 관련있는 뷰를 그룹화할 수 있다.
- 낮은 의존도
  - 뷰, 모델, 컨트롤러는 각각 독립적이다.
- 개발 용이성
  - 개발자들간의 책임이 분리되어 있어 코드 수정 및 유지 보수가 간편하다.
- 한 모델에 대한 여러 형태의 뷰를 가질 수 있다.



### MVC 패턴의 단점

- 코드 네비게이션이 복잡하다
  - 로직의 흐름이 단번에 보이지않고, 내부적으로 들어가야 확인이 가능하다.
- 코드 일관성 유지에 노력이 필요하다.
  - 네이밍, 패키지 추가 혹은 컨트롤러 추가에 대한 일관성 유지의 노력이 필요하다.