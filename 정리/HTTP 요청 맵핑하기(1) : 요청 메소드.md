### HTTP 요청 맵핑하기(1) : 요청 메소드

**HTTP Method**

- GET, POST, PUT, PATCH, DELETE...


## 
**GET 요청**

- 클라이언트가 서버의 리소스를 요청할 때 사용한다.
- 캐싱을 할 수 있다.
- 브라우저 기록에 남는다.
- 북마크 할 수 있다.
- 민감한 데이터를 보낼 때는 사용하지 않는 것이 좋다.
- idempotent(동일한 요청은 항상 동일한 응답을 해야한다.)


## 
**POST 요청**

- 클라이언트가 서버의 리소스를 수정하거나 새로 만들 때 사용한다.
- 서버에 보내는 데이터를 POST 요청 본문에 담는다.
- 캐시할 수 없다.
- 브라우저 기록에 남지 않는다.
- 북마크 할 수 없다.
- 데이터 길이 제한이 없다.
- idempotent하지 않는다.(동일한 요청은 항상 동일한 응답을 하지 않는다.)


## 
**PUT 요청**

- URI에 해당하는 데이터를 새로 만들거나 수정할 때 사용한다.
- POST와 다른 점은 "URI"에 대한 의미가 다르다.
  - POST의 URI는 보내는 데이터를 처리할 리소스를 지칭한다.
  - PUT의 URI는 보내는 데이터에 해당하는 리소스를 지칭한다.
- idempotent(동일한 요청은 항상 동일한 응답을 해야한다.)


## 
**PATCH 요청**

- PUT과 비슷하지만, 기존 엔티티와 새 데이터의 차이점만 보낸다는 차이가 있다.
- idempotent(동일한 요청은 항상 동일한 응답을 해야한다.)


## 
**DELETE 요청**

- URI에 해당하는 리소스를 삭제할  때 사용한다.
- idempotent(동일한 요청은 항상 동일한 응답을 해야한다.)


## 
**명시되지 않은 HandlerMapping**

- @RequestMapping에 HTTP Method가 명시되어 있지 않으면, GET, POST, PUT 등 모든 메소드에 매핑이 가능해진다.
  - Test를 진행했을 때 GET이든 POST든 모두 올바르게 테스트가 진행된다.

~~~java
//Controller
@Controller
public class SampleController {
  @RequestMapping("/hello")
  @ResponseBody
  public String hello() {
    return "hello";
  }
}

//Test
//GET 방식으로 요청
@Test
public void helloTestGet() throws Exception {
  mockMvc.perform(get("/hello"))
    .andDo(print()) //요청과 응답을 출력
    .andExpect(status().isOk()) //status 값이 isOk를 나오기를 기대
    .andExpect(content().string("hello"));  //본문에 있는 문자열이 hello로 나타날 것이다.
}

//POST 방식으로 요청
@Test
public void helloTestPost() throws Exception {
  mockMvc.perform(post("/hello"))
    .andDo(print()) //요청과 응답을 출력
    .andExpect(status().isOk()) //status 값이 isOk를 나오기를 기대
    .andExpect(content().string("hello"));  //본문에 있는 문자열이 hello로 나타날 것이다.
}
~~~

- get()과 print()

<img src="https://user-images.githubusercontent.com/40616436/77084466-5b473300-6a42-11ea-95bb-996aa39046ed.png" alt="image" style="zoom:50%;" />

- status()와 content()

<img src="https://user-images.githubusercontent.com/40616436/77084602-87fb4a80-6a42-11ea-8915-bcd9ecbdd5e8.png" alt="image" style="zoom:50%;" />


## 
**명시된 HandlerMapping**

- @RequestMapping에 HTTP Method를 명시해보자.
  - 클래스에 mapping 설정 시, 해당 메소드는 모두 클래스에서 지정한 method 방식을 따른다.
  - 메소드에 mapping 설정 시, @RequestMapping과 (@GetMapping 혹은 @PostMapping 등)이 있다.

~~~java
//Controller = 클래스에 mapping 설정 (해당 메소드는 모두 GET)
@Controller
@RequestMapping(method = RequestMethod.GET)
public class SampleController {
  @RequestMapping(value = "/hello")
  @ResponseBody
  public String hello() {
    return "hello";
  }
}


//Controller = 메소드에 mapping 설정
@Controller
public class SampleController {
  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  //or
  @GetMapping("/hello")
  @ResponseBody
  public String hello() {
    return "hello";
  }
}


//Test
@Test
public void helloTestGet() throws Exception {
  mockMvc.perform(get("/hello"))
    .andDo(print()) //요청과 응답을 출력
    .andExpect(status().isOk()) //status 값이 isOk를 나오기를 기대
    .andExpect(content().string("hello"));  //본문에 있는 문자열이 hello로 나타날 것이다.
}

@Test
public void helloTestPost() throws Exception {
  mockMvc.perform(post("/hello"))
    .andDo(print()) //요청과 응답을 출력
    .andExpect(status().isMethodNotAllowed()) //status 값 확인
    .andExpect(content().string("hello"));  //본문에 있는 문자열이 hello로 나타날 것이다.
}
~~~

- Post로 테스트를 진행하면 그림과 같이 오류가 응답될 것이다. (405)

  <img src="https://user-images.githubusercontent.com/40616436/77085749-1f14d200-6a44-11ea-8566-c7baceea6f30.png" alt="image" style="zoom:50%;" />


## 
**다중으로 메소드를 명시한 HandlerMapping**

~~~java
//Controller
@Controller
public class SampleController {
	@RequestMapping(value = "/hello", method = {RequestMethod.GET, RequestMethod.PUT})
  @ResponseBody
  public String hello() {
    return "hello";
  }
}
~~~


## 
**테스트 소스를 간략히 살펴보자**

- 간단히 작성한 controller의 hello() 메소드를 테스트 해보자

~~~java
//Controller
@Controller
public class SampleController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }
}


//스프링에서 제공해주는 JUnit용 Runner로서, 내부적으로 애플리케이션 컨텍스트도 만들어준다.
@RunWith(SpringRunner.class)
@WebMvcTest
public class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

  	//GET 방식으로 요청
    @Test
    public void helloTestGet() throws Exception {
        mockMvc.perform(get("/hello"))
                .andDo(print()) //요청과 응답을 출력
                .andExpect(status().isOk()) //status 값이 isOk를 나오기를 기대
                .andExpect(content().string("hello"));  //본문에 있는 문자열이 hello로 나타날 것이다.
    }
  
  	//POST 방식으로 요청
  	@Test
    public void helloTestPost() throws Exception {
        mockMvc.perform(post("/hello"))
                .andDo(print()) //요청과 응답을 출력
                .andExpect(status().isOk()) //status 값이 isOk를 나오기를 기대
                .andExpect(content().string("hello"));  //본문에 있는 문자열이 hello로 나타날 것이다.
    }

}
~~~
