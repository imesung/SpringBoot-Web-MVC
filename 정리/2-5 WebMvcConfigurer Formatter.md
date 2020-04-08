## WebMvcConfigurer Formatter

- Controller의 파라미터를 받을 때, 파라미터를 객체로 받을 수 있을까?

~~~java
// url : hello/mesung
@GetMapping("/hello/{name}")
public String hello(@PathVariable("name") Person person) {
  return "hello" + person.getName();
}

// url : hello?name=mesung
@GetMapping("/hello")
public String helloReq(@RequestParam("name") Person person) {
  return "hello" + person.getName();
}
~~~



**Formatter**

- Formatter를 사용하여 위 Controller의 파라미터를 받을 수 있다.
- Printer: 해당 객체를 (Locale 정보를 참고하여) 문자열로 어떻게 출력할 것인가
- Parser: 어떤 문자열을 (Locale 정보를 참고하여) 객체로 어떻게 변환할 것인가

~~~java
public class PersonFormatter implements Formatter<Person> {

  @Override
  public Person parse(String s, Locale locale) throws ParseException {
    Person person = new Person();
    person.setName(s);
    return person;
  }

  //현재 우리 소스는 사용하고 있지 않음.
  @Override
  public String print(Person person, Locale locale) {
    return person.toString();
  }
}
~~~



**Formatter 등록**(빈으로 등록)

- 이제 위에서 만든 Formatter를 WebConfig 클래스를 통해 등록해보자.

~~~java
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addFormatter(new PersonFormatter());
  }
}
~~~



**스프링 부트를 활용할 시 WebConfig가 필요 없다.**

- Formatter가 만약에 빈(어노테이션 붙어있을 시)으로 등록되어 있으면 스프링 부트가 알아서 빈으로 등록 해준다.

~~~java
@Component
public class PersonFormatter implements Formatter<Person> {

}
~~~



**하지만 @Component는 웹과 관련되 빈이 아니므로, Test에서 제약사항이 나타난다.**

- @WebMvcTest를 통해 Test 진행을 할 수가 없다.

  ~~~java
  @RunWith(SpringRunner.class)
  @WebMvcTest
  public class SampleControllerTest {
  ~~~

  - WebMvcTest는 Test에서 웹과 관련된 빈들만 등록해주므로 @Component를 빈으로 등록하지 않는다.



- 그렇다면 테스트를 진행하기 위해서는?

  ~~~java
  @RunWith(SpringRunner.class)
  @SpringBootTest
  @AutoConfigureMockMvc
  public class SampleControllerTest {
  ~~~

  - @SpringBootTest(통합 테스트) : 모든 빈들을 등록.
  - @AutoConfigureMockMvc : MockMvc도 자동으로 등록.